package de.kontext_e.jqassistant.plugin.dot.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.khubla.dot4j.DOTMarshaller;
import com.khubla.dot4j.domain.*;
import de.kontext_e.jqassistant.plugin.dot.store.descriptor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ScannerPlugin.Requires(FileDescriptor.class)
public class DotScannerPlugin extends AbstractScannerPlugin<FileResource, DotDescriptor>  {
    private static final Logger LOGGER = LoggerFactory.getLogger(DotScannerPlugin.class);

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {
        final boolean accepted = path != null &&
                (
                        path.toLowerCase().endsWith(".dot")
                                || path.toLowerCase().endsWith(".gv")
                )
                ;
        if (accepted) {
            LOGGER.debug("Dot Scanner Plugin accepted path " + path);
        }

        return accepted;
    }

    @Override
    public DotDescriptor scan(final FileResource file, String path, Scope scope, Scanner scanner) throws IOException {
        LOGGER.info("Dot Scanner Plugin scans file "+path);
        FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
        final Store store = scanner.getContext().getStore();
        final DotFileDescriptor checkstyleReportDescriptor = store.addDescriptorType(fileDescriptor, DotFileDescriptor.class);
        readStream(store, checkstyleReportDescriptor, file.createStream());
        return checkstyleReportDescriptor;
    }

    void readStream(Store store, DotFileDescriptor dotFileDescriptor, InputStream inputStream) throws IOException {
        Graph graph = DOTMarshaller.importGraph(inputStream);
        importGraph(store, dotFileDescriptor, graph);
    }

    void importGraph(Store store, HasGraph graphParent, Graph graph) {
        final DotGraphDescriptor dotGraphDescriptor = store.create(DotGraphDescriptor.class);
        graphParent.getGraphs().add(dotGraphDescriptor);

        final Map<String, Node> nodes = graph.getNodes();
        final Map<String, DotNodeDescriptor> nodeDescriptors = new HashMap<>();
        for (Node node : nodes.values()) {
            final DotNodeDescriptor dotNodeDescriptor = store.create(DotNodeDescriptor.class);
            dotNodeDescriptor.setDotId(node.getId());
            dotGraphDescriptor.getNodes().add(dotNodeDescriptor);
            nodeDescriptors.put(node.getId(), dotNodeDescriptor);
        }

        final List<Edge> edges = graph.getEdges();
        for (Edge edge : edges) {
            final EdgeConnectionPoint from = edge.getFrom();
            final EdgeConnectionPoint to = edge.getTo();
            final NodeId fromNodeId = from.getNodeId();
            if (fromNodeId != null) {
                DotNodeDescriptor fromDescriptor = nodeDescriptors.get(fromNodeId.getId());
                if (fromDescriptor != null) {
                    final NodeId toNodeId = to.getNodeId();
                    if (toNodeId != null) {
                        DotNodeDescriptor toDescriptor = nodeDescriptors.get(toNodeId.getId());
                        if (toDescriptor != null) {
                            fromDescriptor.getConnectedNodes().add(toDescriptor);
                        }
                    }
                }
            }
        }


        final List<Graph> subGraphs = graph.getSubGraphs();
        for (Graph subGraph : subGraphs) {
            importGraph(store, dotGraphDescriptor, subGraph);
        }
    }
}
