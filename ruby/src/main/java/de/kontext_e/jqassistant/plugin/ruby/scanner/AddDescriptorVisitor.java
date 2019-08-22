package de.kontext_e.jqassistant.plugin.ruby.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.ClassDescriptor;
import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.MethodDescriptor;
import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.ModuleDescriptor;
import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.RubyFileDescriptor;
import org.jrubyparser.NodeVisitor;
import org.jrubyparser.ast.*;

class AddDescriptorVisitor implements NodeVisitor {
    private final RubyFileDescriptor rubyFileDescriptor;
    private final Store store;

    AddDescriptorVisitor(RubyFileDescriptor rubyFileDescriptor, Store store) {
        this.rubyFileDescriptor = rubyFileDescriptor;
        this.store = store;
    }

    @Override
    public Object visitAliasNode(AliasNode iVisited) {
        return null;
    }

    @Override
    public Object visitAndNode(AndNode iVisited) {
        return null;
    }

    @Override
    public Object visitArgsNode(ArgsNode iVisited) {
        return null;
    }

    @Override
    public Object visitArgsCatNode(ArgsCatNode iVisited) {
        return null;
    }

    @Override
    public Object visitArgsPushNode(ArgsPushNode iVisited) {
        return null;
    }

    @Override
    public Object visitArgumentNode(ArgumentNode iVisited) {
        return null;
    }

    @Override
    public Object visitArrayNode(ArrayNode iVisited) {
        return null;
    }

    @Override
    public Object visitAttrAssignNode(AttrAssignNode iVisited) {
        return null;
    }

    @Override
    public Object visitBackRefNode(BackRefNode iVisited) {
        return null;
    }

    @Override
    public Object visitBeginNode(BeginNode iVisited) {
        return null;
    }

    @Override
    public Object visitBignumNode(BignumNode iVisited) {
        return null;
    }

    @Override
    public Object visitBlockArgNode(BlockArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitBlockArg18Node(BlockArg18Node iVisited) {
        return null;
    }

    @Override
    public Object visitBlockNode(BlockNode iVisited) {
        return null;
    }

    @Override
    public Object visitBlockPassNode(BlockPassNode iVisited) {
        return null;
    }

    @Override
    public Object visitBreakNode(BreakNode iVisited) {
        return null;
    }

    @Override
    public Object visitConstDeclNode(ConstDeclNode iVisited) {
        return null;
    }

    @Override
    public Object visitClassVarAsgnNode(ClassVarAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitClassVarDeclNode(ClassVarDeclNode iVisited) {
        return null;
    }

    @Override
    public Object visitClassVarNode(ClassVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitCallNode(CallNode iVisited) {
        return null;
    }

    @Override
    public Object visitCaseNode(CaseNode iVisited) {
        return null;
    }

    @Override
    public Object visitClassNode(ClassNode iVisited) {
        final ClassDescriptor moduleDescriptor = store.create(ClassDescriptor.class);
        moduleDescriptor.setName(iVisited.getCPath().getName());
        return null;
    }

    @Override
    public Object visitCommentNode(CommentNode iVisited) {
        return null;
    }

    @Override
    public Object visitColon2Node(Colon2Node iVisited) {
        return null;
    }

    @Override
    public Object visitColon3Node(Colon3Node iVisited) {
        return null;
    }

    @Override
    public Object visitConstNode(ConstNode iVisited) {
        return null;
    }

    @Override
    public Object visitDAsgnNode(DAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitDRegxNode(DRegexpNode iVisited) {
        return null;
    }

    @Override
    public Object visitDStrNode(DStrNode iVisited) {
        return null;
    }

    @Override
    public Object visitDSymbolNode(DSymbolNode iVisited) {
        return null;
    }

    @Override
    public Object visitDVarNode(DVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitDXStrNode(DXStrNode iVisited) {
        return null;
    }

    @Override
    public Object visitDefinedNode(DefinedNode iVisited) {
        return null;
    }

    @Override
    public Object visitDefnNode(DefnNode iVisited) {
        return null;
    }

    @Override
    public Object visitDefsNode(DefsNode iVisited) {
        return null;
    }

    @Override
    public Object visitDotNode(DotNode iVisited) {
        return null;
    }

    @Override
    public Object visitEncodingNode(EncodingNode iVisited) {
        return null;
    }

    @Override
    public Object visitEnsureNode(EnsureNode iVisited) {
        return null;
    }

    @Override
    public Object visitEvStrNode(EvStrNode iVisited) {
        return null;
    }

    @Override
    public Object visitFCallNode(FCallNode iVisited) {
        return null;
    }

    @Override
    public Object visitFalseNode(FalseNode iVisited) {
        return null;
    }

    @Override
    public Object visitFixnumNode(FixnumNode iVisited) {
        return null;
    }

    @Override
    public Object visitFlipNode(FlipNode iVisited) {
        return null;
    }

    @Override
    public Object visitFloatNode(FloatNode iVisited) {
        return null;
    }

    @Override
    public Object visitForNode(ForNode iVisited) {
        return null;
    }

    @Override
    public Object visitGlobalAsgnNode(GlobalAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitGlobalVarNode(GlobalVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitHashNode(HashNode iVisited) {
        return null;
    }

    @Override
    public Object visitInstAsgnNode(InstAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitInstVarNode(InstVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitIfNode(IfNode iVisited) {
        return null;
    }

    @Override
    public Object visitImplicitNilNode(ImplicitNilNode visited) {
        return null;
    }

    @Override
    public Object visitIterNode(IterNode iVisited) {
        return null;
    }

    @Override
    public Object visitKeywordArgNode(KeywordArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitKeywordRestArgNode(KeywordRestArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitLambdaNode(LambdaNode visited) {
        return null;
    }

    @Override
    public Object visitListNode(ListNode iVisited) {
        return null;
    }

    @Override
    public Object visitLiteralNode(LiteralNode iVisited) {
        return null;
    }

    @Override
    public Object visitLocalAsgnNode(LocalAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitLocalVarNode(LocalVarNode iVisited) {
        return null;
    }

    @Override
    public Object visitMultipleAsgnNode(MultipleAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitMatch2Node(Match2Node iVisited) {
        return null;
    }

    @Override
    public Object visitMatch3Node(Match3Node iVisited) {
        return null;
    }

    @Override
    public Object visitMatchNode(MatchNode iVisited) {
        return null;
    }

    @Override
    public Object visitMethodNameNode(MethodNameNode iVisited) {
        final MethodDescriptor methodDescriptor = store.create(MethodDescriptor.class);
        methodDescriptor.setName(iVisited.getName());
        return null;
    }

    @Override
    public Object visitModuleNode(ModuleNode iVisited) {
        final ModuleDescriptor moduleDescriptor = store.create(ModuleDescriptor.class);
        moduleDescriptor.setName(iVisited.getCPath().getName());
        rubyFileDescriptor.getModules().add(moduleDescriptor);
        return null;
    }

    @Override
    public Object visitNewlineNode(NewlineNode iVisited) {
        return null;
    }

    @Override
    public Object visitNextNode(NextNode iVisited) {
        return null;
    }

    @Override
    public Object visitNilNode(NilNode iVisited) {
        return null;
    }

    @Override
    public Object visitNotNode(NotNode iVisited) {
        return null;
    }

    @Override
    public Object visitNthRefNode(NthRefNode iVisited) {
        return null;
    }

    @Override
    public Object visitOpElementAsgnNode(OpElementAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitOpAsgnNode(OpAsgnNode iVisited) {
        return null;
    }

    @Override
    public Object visitOpAsgnAndNode(OpAsgnAndNode iVisited) {
        return null;
    }

    @Override
    public Object visitOpAsgnOrNode(OpAsgnOrNode iVisited) {
        return null;
    }

    @Override
    public Object visitOptArgNode(OptArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitOrNode(OrNode iVisited) {
        return null;
    }

    @Override
    public Object visitPreExeNode(PreExeNode iVisited) {
        return null;
    }

    @Override
    public Object visitPostExeNode(PostExeNode iVisited) {
        return null;
    }

    @Override
    public Object visitRedoNode(RedoNode iVisited) {
        return null;
    }

    @Override
    public Object visitRegexpNode(RegexpNode iVisited) {
        return null;
    }

    @Override
    public Object visitRescueBodyNode(RescueBodyNode iVisited) {
        return null;
    }

    @Override
    public Object visitRescueNode(RescueNode iVisited) {
        return null;
    }

    @Override
    public Object visitRestArgNode(RestArgNode iVisited) {
        return null;
    }

    @Override
    public Object visitRetryNode(RetryNode iVisited) {
        return null;
    }

    @Override
    public Object visitReturnNode(ReturnNode iVisited) {
        return null;
    }

    @Override
    public Object visitRootNode(RootNode iVisited) {
        return null;
    }

    @Override
    public Object visitSClassNode(SClassNode iVisited) {
        return null;
    }

    @Override
    public Object visitSelfNode(SelfNode iVisited) {
        return null;
    }

    @Override
    public Object visitSplatNode(SplatNode iVisited) {
        return null;
    }

    @Override
    public Object visitStrNode(StrNode iVisited) {
        return null;
    }

    @Override
    public Object visitSuperNode(SuperNode iVisited) {
        return null;
    }

    @Override
    public Object visitSValueNode(SValueNode iVisited) {
        return null;
    }

    @Override
    public Object visitSymbolNode(SymbolNode iVisited) {
        return null;
    }

    @Override
    public Object visitSyntaxNode(SyntaxNode iVisited) {
        return null;
    }

    @Override
    public Object visitToAryNode(ToAryNode iVisited) {
        return null;
    }

    @Override
    public Object visitTrueNode(TrueNode iVisited) {
        return null;
    }

    @Override
    public Object visitUndefNode(UndefNode iVisited) {
        return null;
    }

    @Override
    public Object visitUnaryCallNode(UnaryCallNode iVisited) {
        return null;
    }

    @Override
    public Object visitUntilNode(UntilNode iVisited) {
        return null;
    }

    @Override
    public Object visitVAliasNode(VAliasNode iVisited) {
        return null;
    }

    @Override
    public Object visitVCallNode(VCallNode iVisited) {
        return null;
    }

    @Override
    public Object visitWhenNode(WhenNode iVisited) {
        return null;
    }

    @Override
    public Object visitWhileNode(WhileNode iVisited) {
        return null;
    }

    @Override
    public Object visitXStrNode(XStrNode iVisited) {
        return null;
    }

    @Override
    public Object visitYieldNode(YieldNode iVisited) {
        return null;
    }

    @Override
    public Object visitZArrayNode(ZArrayNode iVisited) {
        return null;
    }

    @Override
    public Object visitZSuperNode(ZSuperNode iVisited) {
        return null;
    }
}
