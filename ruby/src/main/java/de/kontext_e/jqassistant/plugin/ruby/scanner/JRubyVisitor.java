package de.kontext_e.jqassistant.plugin.ruby.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.*;
import org.jruby.ast.*;
import org.jruby.ast.types.INameNode;
import org.jruby.ast.visitor.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Arrays.asList;

public class JRubyVisitor implements NodeVisitor<Object> {
  private static final Logger LOGGER = LoggerFactory.getLogger(JRubyVisitor.class);

  private final RubyFileDescriptor rubyFileDescriptor;
  private final Store store;
  private final Node         myNode;
  private final JRubyVisitor parentVisitor;

  private static Map<String, ModuleDescriptor> fqnToModule = new HashMap<>();
  private static Map<String, ClassDescriptor> fqnToClass = new HashMap<>();
  private static Set<UnresolvedCallTarget> unresolvedCallTargets = new HashSet<>();
  private static Map<UnresolvedIncludeTarget, List<String>> unresolvedIncludeTargets = new HashMap<>();


  JRubyVisitor(RubyFileDescriptor rubyFileDescriptor, Store store, Node myNode, JRubyVisitor parentVisitor) {
    this.rubyFileDescriptor = rubyFileDescriptor;
    this.store = store;
    this.myNode = myNode;
    this.parentVisitor = parentVisitor;
  }

  @Override
  public Object visitAliasNode(AliasNode aliasNode) {
    return null;
  }

  @Override
  public Object visitAndNode(AndNode andNode) {
    return null;
  }

  @Override
  public Object visitArgsNode(ArgsNode argsNode) {
    return null;
  }

  @Override
  public Object visitArgsCatNode(ArgsCatNode argsCatNode) {
    return null;
  }

  @Override
  public Object visitArgsPushNode(ArgsPushNode argsPushNode) {
    return null;
  }

  @Override
  public Object visitArgumentNode(ArgumentNode argumentNode) {
    return null;
  }

  @Override
  public Object visitArrayNode(ArrayNode arrayNode) {
    return null;
  }

  @Override
  public Object visitAttrAssignNode(AttrAssignNode attrAssignNode) {
    return null;
  }

  @Override
  public Object visitBackRefNode(BackRefNode backRefNode) {
    return null;
  }

  @Override
  public Object visitBeginNode(BeginNode beginNode) {
    return null;
  }

  @Override
  public Object visitBignumNode(BignumNode bignumNode) {
    return null;
  }

  @Override
  public Object visitBlockArgNode(BlockArgNode blockArgNode) {
    return null;
  }

  @Override
  public Object visitBlockNode(BlockNode blockNode) {
    return null;
  }

  @Override
  public Object visitBlockPassNode(BlockPassNode blockPassNode) {
    return null;
  }

  @Override
  public Object visitBreakNode(BreakNode breakNode) {
    return null;
  }

  @Override
  public Object visitConstDeclNode(ConstDeclNode constDeclNode) {
    final ConstantDescriptor constantDescriptor = store.create(ConstantDescriptor.class);
    constantDescriptor.setName(constDeclNode.getName().asJavaString());
    final String fqn = getFqn(findParentIScopingNode());

    if("".equals(fqn)) {
      rubyFileDescriptor.getConstants().add(constantDescriptor);
    }
    if(fqnToModule.containsKey(fqn)) {
      fqnToModule.get(fqn).getConstants().add(constantDescriptor);
    }
    if(fqnToClass.containsKey(fqn)) {
      fqnToClass.get(fqn).getConstants().add(constantDescriptor);
    }

    return null;
  }

  @Override
  public Object visitClassVarAsgnNode(ClassVarAsgnNode classVarAsgnNode) {
    return null;
  }

  @Override
  public Object visitClassVarNode(ClassVarNode classVarNode) {
    return null;
  }

  @Override
  public Object visitCallNode(CallNode callNode) {
    addRelationshipFromCallerToCallee(callNode);

    return null;
  }

  private void addRelationshipFromCallerToCallee(CallNode callNode) {
    final Node receiver = callNode.getReceiverNode();
    final String receiverFqn = getReceiverFqn(receiver);
    if(receiverFqn == null) {
      // ignore some internal call (at least for now)
      return;
    }

    // FIXME has ruby overloaded methods?
    final MethodContainer targetMethodContainer = findByFqn(receiverFqn);
    if(targetMethodContainer != null) {
      addCallsRelationship(callNode, targetMethodContainer);
    } else {
      addUnresolvedCallTarget(callNode, receiverFqn);
    }
  }

  private void addCallsRelationship(CallNode callNode, MethodContainer targetMethodContainer) {
    String targetMethodName = callNode.getName().asJavaString();
    if("new".equals(targetMethodName)) {
      targetMethodName = "initialize";
    }

    for (MethodDescriptor targetMethodCandidate : targetMethodContainer.getDeclaredMethods()) {
      if(targetMethodCandidate.getName().equals(targetMethodName)) {
        final String fqn = getFqn(findParentIScopingNode());
        final MethodDefNode methodFor = findParentMethod();
        if(methodFor == null) {
          // call is not inside from a method
          continue;
        }
        final String normativeSignature = getNormativeSignature(methodFor);
        final MethodContainer callerMethodContainer = findByFqn(fqn);
        for (MethodDescriptor callerMethodCandidates : callerMethodContainer.getDeclaredMethods()) {
          if(callerMethodCandidates.getSignature().equals(normativeSignature)) {
            callerMethodCandidates.getCalledMethods().add(targetMethodCandidate);
          }
        }
      }
    }
  }

  private void addUnresolvedCallTarget(CallNode callNode, String receiverFqn) {
    UnresolvedCallTarget uct = new UnresolvedCallTarget(receiverFqn, callNode.getName().asJavaString());
    UnresolvedCallTarget cleanMeUp;
    if(unresolvedCallTargets.contains(uct)) {
      cleanMeUp = unresolvedCallTargets.stream().filter(x -> x.equals(uct)).findFirst().get();
    } else {
      unresolvedCallTargets.add(uct);
      cleanMeUp = uct;
    }
    final String fqn = getFqn(findParentIScopingNode());

    final MethodDefNode methodFor = findParentMethod();

    if(methodFor == null) {
      return;
    }

    final String normativeSignature = getNormativeSignature(methodFor);
    final MethodContainer methodContainer = findByFqn(fqn);
    if(methodContainer != null) {
      for (MethodDescriptor declaredMethod : methodContainer.getDeclaredMethods()) {
        if(declaredMethod.getSignature().equals(normativeSignature)) {
          cleanMeUp.addCallSource(declaredMethod);
        }
      }
    }
  }

  private String getNormativeSignature(MethodDefNode methodFor) {
    StringBuilder signature = new StringBuilder();

    signature.append(methodFor.getName());

    Node[] args = methodFor.getArgsNode().getArgs();
    int length = args.length;

    if (length > 0) {
      signature.append('(').append(args[0]);

      for (int i = 1; i < length; i++) {
        signature.append(',').append(args[i]);
      }

      signature.append(')');
    }

    return signature.toString();
  }

  @Override
  public Object visitCaseNode(CaseNode caseNode) {
    return null;
  }

  @Override
  public Object visitClassNode(ClassNode classNode) {
    final ClassDescriptor classDescriptor = store.create(ClassDescriptor.class);
    classDescriptor.setName(classNode.getCPath().getName().asJavaString());
    classDescriptor.setFullQualifiedName(getFqn(classNode));
    fqnToClass.put(classDescriptor.getFullQualifiedName(), classDescriptor);
    rubyFileDescriptor.getClasses().add(classDescriptor);

    final ModuleNode parentModule = findParentModule();
    if(parentModule != null) {
      final String parentFqn = getFqn(parentModule);
      final ModuleDescriptor parentModuleDescriptor = fqnToModule.get(parentFqn);
      if(parentModuleDescriptor != null) {
        parentModuleDescriptor.getClasses().add(classDescriptor);
      }
    }

    for (Node childNode : classNode.getBodyNode().childNodes()) {
      if (childNode instanceof MethodDefNode) {
        MethodDefNode methodDef = (MethodDefNode) childNode;
        final MethodDescriptor methodDescriptor = store.create(MethodDescriptor.class);
        classDescriptor.getDeclaredMethods().add(methodDescriptor);
        methodDescriptor.setName(methodDef.getName().asJavaString());
        methodDescriptor.setSignature(getNormativeSignature(methodDef));

        UnresolvedCallTarget uct = new UnresolvedCallTarget(classDescriptor.getFullQualifiedName(), methodDef.getName().asJavaString());
        if (unresolvedCallTargets.contains(uct)) {
          final UnresolvedCallTarget unresolvedCallTarget = unresolvedCallTargets.stream()
                  .filter(x -> x.equals(uct)).findFirst().get();
          unresolvedCallTargets.remove(uct);

          for (MethodDescriptor callSource : unresolvedCallTarget.getCallSources()) {
            callSource.getCalledMethods().add(methodDescriptor);
          }

        }

        final ArgsNode args = methodDef.getArgsNode();
        for (Node param : args.getArgs()) {
          if (param instanceof ArgumentNode) {
            ArgumentNode argumentNode = (ArgumentNode) param;
            final ParameterDescriptor parameterDescriptor = store.create(ParameterDescriptor.class);
            methodDescriptor.getParameters().add(parameterDescriptor);
            parameterDescriptor.setName(argumentNode.getName().asJavaString());
          }
        }
      }
    }

    // set relationship to super class
    final Node superNode = classNode.getSuperNode();
    if(superNode instanceof ConstNode) {
      ConstNode superClass = (ConstNode) superNode;
      final String superClassName = superClass.getName().asJavaString();
      if(superClassName.contains("::")) {
        // assume name is already fully qualified, FIXME maybe partly qualified within sibling module
        System.out.println("superClassName.contains ::");
        if (fqnToClass.containsKey(superClassName)) {
          System.out.println("create superclass relationship");
          final ClassDescriptor superClassDescriptor = fqnToClass.get(superClassName);
          classDescriptor.setSuperClass(superClassDescriptor);
        }
      } else {
        // super class is in the same namespace but cached with fqn
        System.out.println("superClassName in same namespace");
        final String inheritedClassFqn = getFqn(classNode);
        FullyQualifiedName fullyQualifiedName = new FullyQualifiedName(inheritedClassFqn);
        FullyQualifiedName replaced = fullyQualifiedName.replaceLastBy(superClassName);
        System.out.println("replaced: "+replaced);
        if (fqnToClass.containsKey(replaced.asString())) {
          System.out.println("create superclass relationship");
          final ClassDescriptor superClassDescriptor = fqnToClass.get(replaced.asString());
          classDescriptor.setSuperClass(superClassDescriptor);
        }
      }
    }


    return null;
  }

  @Override
  public Object visitColon2Node(Colon2Node colon2Node) {
    return null;
  }

  @Override
  public Object visitColon3Node(Colon3Node colon3Node) {
    return null;
  }

  @Override
  public Object visitComplexNode(ComplexNode complexNode) {
    return null;
  }

  @Override
  public Object visitConstNode(ConstNode constNode) {
    return null;
  }

  @Override
  public Object visitDAsgnNode(DAsgnNode dAsgnNode) {
    return null;
  }

  @Override
  public Object visitDRegxNode(DRegexpNode dRegexpNode) {
    return null;
  }

  @Override
  public Object visitDStrNode(DStrNode dStrNode) {
    return null;
  }

  @Override
  public Object visitDSymbolNode(DSymbolNode dSymbolNode) {
    return null;
  }

  @Override
  public Object visitDVarNode(DVarNode dVarNode) {
    return null;
  }

  @Override
  public Object visitDXStrNode(DXStrNode dxStrNode) {
    return null;
  }

  @Override
  public Object visitDefinedNode(DefinedNode definedNode) {
    return null;
  }

  @Override
  public Object visitDefnNode(DefnNode defnNode) {
    return null;
  }

  @Override
  public Object visitDefsNode(DefsNode defsNode) {
    return null;
  }

  @Override
  public Object visitDotNode(DotNode dotNode) {
    return null;
  }

  @Override
  public Object visitEncodingNode(EncodingNode encodingNode) {
    return null;
  }

  @Override
  public Object visitEnsureNode(EnsureNode ensureNode) {
    return null;
  }

  @Override
  public Object visitEvStrNode(EvStrNode evStrNode) {
    return null;
  }

  @Override
  public Object visitFCallNode(FCallNode fCallNode) {
    if("include".equals(fCallNode.getName().asJavaString())) {
      final ClassNode parentClass = findParentClass();
      if (fCallNode.getArgsNode() instanceof ArrayNode) {
        ArrayNode args = (ArrayNode) fCallNode.getArgsNode();
        for (Node childNode : args.childNodes()) {
          if (childNode instanceof ConstNode) {
            ConstNode constNode = (ConstNode) childNode;
            final String fqn = getFqn(parentClass);
            if (fqnToClass.containsKey(fqn)) {
              boolean connected = false;
              for (ModuleDescriptor module : fqnToModule.values()) {
                if (module.getFullQualifiedName().equals(constNode.getName().asJavaString())) {
                  fqnToClass.get(fqn).getIncludes().add(module);
                  connected = true;
                }
              }

              if(!connected) {
                UnresolvedIncludeTarget uit = new UnresolvedIncludeTarget(constNode.getName().asJavaString());
                if (unresolvedIncludeTargets.containsKey(uit)) {
                  final List list = unresolvedIncludeTargets.get(uit);
                  list.add(fqn);
                } else {
                  unresolvedIncludeTargets.put(uit, new ArrayList<>(asList(fqn)));
                }
              }

            } else {
              LOGGER.error("No class with fqn " + fqn + " found");
            }
          }
        }
      }

    } else if("require".equals(fCallNode.getName().asJavaString())) {
      if (fCallNode.getArgsNode() instanceof ArrayNode) {
        ArrayNode args = (ArrayNode) fCallNode.getArgsNode();
        for (Node childNode : args.childNodes()) {
          if (childNode instanceof StrNode) {
            StrNode strNode = (StrNode) childNode;
            final RequireDescriptor requireDescriptor = store.create(RequireDescriptor.class);
            requireDescriptor.setName(strNode.getValue().toByteString());
            rubyFileDescriptor.getRequires().add(requireDescriptor);
            // FIXME resolve symbol for require
          }
        }
      }

    } else if("attr".equals(fCallNode.getName().asJavaString())) {
      addAttribute(fCallNode);
    } else if(" attr_accessor".equals(fCallNode.getName().asJavaString())) {
      addAttribute(fCallNode);
    } else if("attr_reader".equals(fCallNode.getName().asJavaString())) {
      addAttribute(fCallNode);
    } else if("attr_writer".equals(fCallNode.getName().asJavaString())) {
      addAttribute(fCallNode);
    } else {
      final MethodDefNode parentMethod = findParentMethod();
      if(parentMethod != null) {
        // System.out.println(parentMethod.getNormativeSignature()+" calls "+iVisited.getName());
        // FIXME add a relationship between caller and callee
      } else {
        // FIXME System.out.println("!!! no method calls "+iVisited.getName());
      }
    }
    return null;
  }

  private void addAttribute(FCallNode iVisited) {
    final IScopingNode parentIScopingNode = findParentIScopingNode();
    if (iVisited.getArgsNode() instanceof ArrayNode) {
      ArrayNode args = (ArrayNode) iVisited.getArgsNode();
      for (Node childNode : args.childNodes()) {
        if (childNode instanceof SymbolNode) {
          SymbolNode symbolNode = (SymbolNode) childNode;
          final AttributeDescriptor attributeDescriptor = store.create(AttributeDescriptor.class);
          attributeDescriptor.setName(symbolNode.getName().asJavaString());
          AttributedDescriptor module = findParentDescriptor(parentIScopingNode);
          if(module != null) {
            module.getAttributes().add(attributeDescriptor);
          } else {
            LOGGER.error("No module for attribute " + symbolNode.getName());
          }
        }
      }
    }
  }


  @Override
  public Object visitFalseNode(FalseNode falseNode) {
    return null;
  }

  @Override
  public Object visitFixnumNode(FixnumNode fixnumNode) {
    return null;
  }

  @Override
  public Object visitFlipNode(FlipNode flipNode) {
    return null;
  }

  @Override
  public Object visitFloatNode(FloatNode floatNode) {
    return null;
  }

  @Override
  public Object visitForNode(ForNode forNode) {
    return null;
  }

  @Override
  public Object visitGlobalAsgnNode(GlobalAsgnNode globalAsgnNode) {
    return null;
  }

  @Override
  public Object visitGlobalVarNode(GlobalVarNode globalVarNode) {
    return null;
  }

  @Override
  public Object visitHashNode(HashNode hashNode) {
    return null;
  }

  @Override
  public Object visitInstAsgnNode(InstAsgnNode instAsgnNode) {
    return null;
  }

  @Override
  public Object visitInstVarNode(InstVarNode instVarNode) {
    return null;
  }

  @Override
  public Object visitIfNode(IfNode ifNode) {
    return null;
  }

  @Override
  public Object visitIterNode(IterNode iterNode) {
    return null;
  }

  @Override
  public Object visitKeywordArgNode(KeywordArgNode keywordArgNode) {
    return null;
  }

  @Override
  public Object visitKeywordRestArgNode(KeywordRestArgNode keywordRestArgNode) {
    return null;
  }

  @Override
  public Object visitLambdaNode(LambdaNode lambdaNode) {
    return null;
  }

  @Override
  public Object visitListNode(ListNode listNode) {
    return null;
  }

  @Override
  public Object visitLiteralNode(LiteralNode literalNode) {
    return null;
  }

  @Override
  public Object visitLocalAsgnNode(LocalAsgnNode localAsgnNode) {
    return null;
  }

  @Override
  public Object visitLocalVarNode(LocalVarNode localVarNode) {
    return null;
  }

  @Override
  public Object visitMultipleAsgnNode(MultipleAsgnNode multipleAsgnNode) {
    return null;
  }

  @Override
  public Object visitMatch2Node(Match2Node match2Node) {
    return null;
  }

  @Override
  public Object visitMatch3Node(Match3Node match3Node) {
    return null;
  }

  @Override
  public Object visitMatchNode(MatchNode matchNode) {
    return null;
  }

  @Override
  public Object visitModuleNode(ModuleNode moduleNode) {
    final ModuleDescriptor moduleDescriptor = store.create(ModuleDescriptor.class);
    moduleDescriptor.setName(moduleNode.getCPath().getName().asJavaString());
    moduleDescriptor.setFullQualifiedName(getFqn(moduleNode));
    fqnToModule.put(moduleDescriptor.getFullQualifiedName(), moduleDescriptor);

    rubyFileDescriptor.getModules().add(moduleDescriptor);
    final ModuleNode parentModule = findParentModule();
    if(parentModule != null) {
      final String parentFqn = getFqn(parentModule);
      final ModuleDescriptor parentModuleDescriptor = fqnToModule.get(parentFqn);
      if(parentModuleDescriptor != null) {
        parentModuleDescriptor.getModules().add(moduleDescriptor);
      }
    }

    UnresolvedIncludeTarget uit = new UnresolvedIncludeTarget(moduleNode.getCPath().getName().asJavaString());
    if(unresolvedIncludeTargets.containsKey(uit)) {
      final List<String> fqns = unresolvedIncludeTargets.get(uit);
      for (String fqn : fqns) {
        fqnToClass.get(fqn).getIncludes().add(moduleDescriptor);
      }
    }

    return null;
  }

  @Override
  public Object visitNewlineNode(NewlineNode newlineNode) {
    return null;
  }

  @Override
  public Object visitNextNode(NextNode nextNode) {
    return null;
  }

  @Override
  public Object visitNilNode(NilNode nilNode) {
    return null;
  }

  @Override
  public Object visitNthRefNode(NthRefNode nthRefNode) {
    return null;
  }

  @Override
  public Object visitOpElementAsgnNode(OpElementAsgnNode opElementAsgnNode) {
    return null;
  }

  @Override
  public Object visitOpAsgnNode(OpAsgnNode opAsgnNode) {
    return null;
  }

  @Override
  public Object visitOpAsgnAndNode(OpAsgnAndNode opAsgnAndNode) {
    return null;
  }

  @Override
  public Object visitOpAsgnConstDeclNode(OpAsgnConstDeclNode opAsgnConstDeclNode) {
    return null;
  }

  @Override
  public Object visitOpAsgnOrNode(OpAsgnOrNode opAsgnOrNode) {
    return null;
  }

  @Override
  public Object visitOptArgNode(OptArgNode optArgNode) {
    return null;
  }

  @Override
  public Object visitOrNode(OrNode orNode) {
    return null;
  }

  @Override
  public Object visitPreExeNode(PreExeNode preExeNode) {
    return null;
  }

  @Override
  public Object visitPostExeNode(PostExeNode postExeNode) {
    return null;
  }

  @Override
  public Object visitRationalNode(RationalNode rationalNode) {
    return null;
  }

  @Override
  public Object visitRedoNode(RedoNode redoNode) {
    return null;
  }

  @Override
  public Object visitRegexpNode(RegexpNode regexpNode) {
    return null;
  }

  @Override
  public Object visitRequiredKeywordArgumentValueNode(
      RequiredKeywordArgumentValueNode requiredKeywordArgumentValueNode) {
    return null;
  }

  @Override
  public Object visitRescueBodyNode(RescueBodyNode rescueBodyNode) {
    return null;
  }

  @Override
  public Object visitRescueNode(RescueNode rescueNode) {
    return null;
  }

  @Override
  public Object visitRestArgNode(RestArgNode restArgNode) {
    return null;
  }

  @Override
  public Object visitRetryNode(RetryNode retryNode) {
    return null;
  }

  @Override
  public Object visitReturnNode(ReturnNode returnNode) {
    return null;
  }

  @Override
  public Object visitRootNode(RootNode rootNode) {
    return null;
  }

  @Override
  public Object visitSClassNode(SClassNode sClassNode) {
    return null;
  }

  @Override
  public Object visitSelfNode(SelfNode selfNode) {
    return null;
  }

  @Override
  public Object visitSplatNode(SplatNode splatNode) {
    return null;
  }

  @Override
  public Object visitStarNode(StarNode starNode) {
    return null;
  }

  @Override
  public Object visitStrNode(StrNode strNode) {
    return null;
  }

  @Override
  public Object visitSuperNode(SuperNode superNode) {
    return null;
  }

  @Override
  public Object visitSValueNode(SValueNode sValueNode) {
    return null;
  }

  @Override
  public Object visitSymbolNode(SymbolNode symbolNode) {
    return null;
  }

  @Override
  public Object visitTrueNode(TrueNode trueNode) {
    return null;
  }

  @Override
  public Object visitUndefNode(UndefNode undefNode) {
    return null;
  }

  @Override
  public Object visitUntilNode(UntilNode untilNode) {
    return null;
  }

  @Override
  public Object visitVAliasNode(VAliasNode vAliasNode) {
    return null;
  }

  @Override
  public Object visitVCallNode(VCallNode vCallNode) {
    return null;
  }

  @Override
  public Object visitWhenNode(WhenNode whenNode) {
    return null;
  }

  @Override
  public Object visitWhileNode(WhileNode whileNode) {
    return null;
  }

  @Override
  public Object visitXStrNode(XStrNode xStrNode) {
    return null;
  }

  @Override
  public Object visitYieldNode(YieldNode yieldNode) {
    return null;
  }

  @Override
  public Object visitZArrayNode(ZArrayNode zArrayNode) {
    return null;
  }

  @Override
  public Object visitZSuperNode(ZSuperNode zSuperNode) {
    return null;
  }

  @Override
  public Object visitOther(Node node) {
    return null;
  }

  private <T> T findParentModule(Class<T> clazz) {
    if(clazz.isAssignableFrom(myNode.getClass())) {
      return (T) myNode;
    }
    if(parentVisitor == null) {
      return null;
    }
    return parentVisitor.findParentModule(clazz);
  }

  private ModuleNode findParentModule() {
    return findParentModule(ModuleNode.class);
  }

  private ClassNode findParentClass() {
    return findParentModule(ClassNode.class);
  }

  private MethodDefNode findParentMethod() {
    return findParentModule(MethodDefNode.class);
  }

  private IScopingNode findParentIScopingNode() {
    return findParentModule(IScopingNode.class);
  }

  private String getFqn(IScopingNode iVisited) {
    if(iVisited == null) return "";

    // initialize current visitor
    JRubyVisitor visitor = this;

    // search for parent with given iVisited
    while(visitor != null && visitor.myNode != iVisited) {
      visitor = visitor.parentVisitor;
    }
    if(visitor == null) return "";

    // build FQN from here
    StringBuilder fqn = new StringBuilder();
    while (visitor != null) {
      if(visitor.myNode instanceof IScopingNode) {
        if(fqn.length() > 0) {
          fqn.insert(0, "::");
        }
        fqn.insert(0, ((IScopingNode) visitor.myNode).getCPath().getName().asJavaString());
      }
      visitor = visitor.parentVisitor;
    }
    return fqn.toString();
  }

  private <T> T findParentDescriptor(IScopingNode parentIScopingNode) {
    final String fqn = getFqn(parentIScopingNode);
    return findByFqn(fqn);
  }

  private <T> T findByFqn(String fqn) {
    if(fqnToModule.containsKey(fqn)) {
      return (T) fqnToModule.get(fqn);
    }
    if(fqnToClass.containsKey(fqn)) {
      return (T) fqnToClass.get(fqn);
    }
    return null;
  }

  private String getReceiverFqn(Node receiver) {
    if(receiver instanceof Colon3Node) {
      String fqn = ((INameNode) receiver).getName().asJavaString();
      Node firstChild = receiver;
      while(true) {
        if (firstChild.childNodes().size() > 0) {
          firstChild = firstChild.childNodes().get(0);
          fqn = ((INameNode) firstChild).getName() + "::" + fqn;
          if(firstChild.getNodeType() == NodeType.COLON3NODE ) {
            fqn = "::"+fqn;
          }
        } else {
          break;
        }
      }
      return fqn;
    }
    return null;
  }
}
