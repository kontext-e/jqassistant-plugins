package de.kontext_e.jqassistant.plugin.javaparser.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;
import de.kontext_e.jqassistant.plugin.javaparser.store.descriptor.JavaSourceDescriptor;
import de.kontext_e.jqassistant.plugin.javaparser.store.descriptor.JavaSourceFileDescriptor;

import static com.github.javaparser.utils.Utils.assertNotNull;
import static java.util.stream.Collectors.toList;
import static org.neo4j.graphdb.DynamicLabel.label;

@ScannerPlugin.Requires(FileDescriptor.class)
public class JavaparserScannerPlugin extends AbstractScannerPlugin<FileResource, JavaSourceDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaparserScannerPlugin.class);
    private static List<String> suffixes = Collections.singletonList("java");

    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) {
        try {
            int beginIndex = path.lastIndexOf(".");
            if(beginIndex > 0) {
                final String suffix = path.substring(beginIndex + 1).toLowerCase();

                boolean accepted = suffixes.contains(suffix);
                if(accepted) {
                    LOGGER.debug("Javaparser accepted path "+path);
                }
                return accepted;
            }
            return false;
        } catch (NullPointerException e) {
            // could do a lengthy null check at beginning or do it the short dirty way
            return false;
        } catch (Exception e) {
            LOGGER.error("Error while checking path: "+e, e);
            return false;
        }
    }

    @Override
	public JavaSourceDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
		final Store store = scanner.getContext().getStore();
		FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
		final JavaSourceFileDescriptor javaSourceFileDescriptor = store.migrate(fileDescriptor, JavaSourceFileDescriptor.class);
        importStream(store, item.createStream(), path);
        return javaSourceFileDescriptor;
    }

    private void importStream(final Store store, final InputStream stream, final String path) {
		CompilationUnit cu = JavaParser.parse(stream);
        output(cu, path, new MyGraphDatabaseService(store), null, null);
    }

    private void output(com.github.javaparser.ast.Node node, String name, MyGraphDatabaseService graphDatabaseService, Node parent, String relName) {
        assertNotNull(node);
        NodeMetaModel metaModel = node.getMetaModel();
        List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
        List<PropertyMetaModel> attributes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute)
                .filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subNodes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode)
                .filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subLists = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList)
                .collect(toList());

        final Node compilationUnit = graphDatabaseService.createNode(label("JavaSource"), label(metaModel.getTypeName()));
        compilationUnit.setProperty("name", name);

        if(parent != null) {
            parent.createRelationshipTo(compilationUnit, () -> relName);
        }

        for (PropertyMetaModel a : attributes) {
            compilationUnit.setProperty(a.getName(), a.getValue(node).toString());
        }

        for (PropertyMetaModel sn : subNodes) {
            com.github.javaparser.ast.Node nd = (com.github.javaparser.ast.Node) sn.getValue(node);
            if (nd != null) {
                output(nd, sn.getName(), graphDatabaseService, compilationUnit, "SUBNODE");
            }
        }

        for (PropertyMetaModel sl : subLists) {
            NodeList<? extends com.github.javaparser.ast.Node> nl = (NodeList<? extends com.github.javaparser.ast.Node>) sl.getValue(node);
            if (nl != null && nl.isNonEmpty()) {
                String slName = sl.getName();
                slName = slName.endsWith("s") ? slName.substring(0, sl.getName().length() - 1) : slName;
                for (com.github.javaparser.ast.Node nd : nl) {
                    output(nd, slName, graphDatabaseService, compilationUnit, "SUBLIST");
                }
            }
        }
    }

}
