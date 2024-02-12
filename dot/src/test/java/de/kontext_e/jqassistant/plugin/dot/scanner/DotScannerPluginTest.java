package de.kontext_e.jqassistant.plugin.dot.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import com.khubla.dot4j.DOTMarshaller;
import com.khubla.dot4j.domain.Edge;
import com.khubla.dot4j.domain.Graph;
import com.khubla.dot4j.domain.GraphType;
import com.khubla.dot4j.domain.Node;
import de.kontext_e.jqassistant.plugin.dot.store.descriptor.DotFileDescriptor;
import de.kontext_e.jqassistant.plugin.dot.store.descriptor.DotGraphDescriptor;
import de.kontext_e.jqassistant.plugin.dot.store.descriptor.DotNodeDescriptor;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DotScannerPluginTest {
    private Store mockStore = mock(Store.class);
    private DotFileDescriptor mockDotFileDescriptor = mock(DotFileDescriptor.class);

    @Test
    public void testApi() throws IOException {
        InputStream inputStream = createExapmle1();
        Graph graph = DOTMarshaller.importGraph(inputStream);

        assertThat(graph.getId(), is("mygraph"));
        assertThat(graph.getGraphType(), is(GraphType.digraph));

        final Map<String, Node> nodes = graph.getNodes();
        assertTrue(nodes.containsKey("node2"));
        assertTrue(nodes.containsKey("n1"));
        Node node2 = nodes.get("node2");
        assertThat(node2.getId(), is("node2"));

        final List<Edge> edges = graph.getEdges();
        assertThat(edges.size(), is(1));
        Edge edge1 = edges.get(0);
        assertThat(edge1.getFrom().getNodeId().getId(), is("n1"));
        assertThat(edge1.getTo().getNodeId().getId(), is("node2"));
    }

    @Test
    public void testImportSimpleGraph() throws IOException {
        // Arrange
        InputStream inputStream = createExapmle1();
        DotScannerPlugin plugin = new DotScannerPlugin();
        Set<DotNodeDescriptor> nodes = new HashSet<>();
        Set<DotNodeDescriptor> connectedNodes = new HashSet<>();

        final DotGraphDescriptor mockDotGraphDescriptor = mock(DotGraphDescriptor.class);
        when(mockStore.create(DotGraphDescriptor.class)).thenReturn(mockDotGraphDescriptor);
        when(mockDotGraphDescriptor.getNodes()).thenReturn(nodes);

        final DotNodeDescriptor mockDotNodeDescriptor = mock(DotNodeDescriptor.class);
        when(mockStore.create(DotNodeDescriptor.class)).thenReturn(mockDotNodeDescriptor);
        when(mockDotNodeDescriptor.getConnectedNodes()).thenReturn(connectedNodes);

        // Act
        plugin.readStream(mockStore, mockDotFileDescriptor, inputStream);

        // Assert
        verify(mockStore).create(DotGraphDescriptor.class);
        verify(mockStore, times(2)).create(DotNodeDescriptor.class);
        verify(mockDotNodeDescriptor).setDotId("node2");
        verify(mockDotNodeDescriptor).setDotId("n1");
        verify(mockDotNodeDescriptor).getConnectedNodes();
        assertThat(connectedNodes.size(), is(1));
    }


    private InputStream createExapmle1() {
        String example = "digraph mygraph {\n" +
                "   node2;\n" +
                "   n1;\n" +
                "   n1 -> node2;\n" +
                "}";
        return new ByteArrayInputStream(example.getBytes(StandardCharsets.UTF_8));
    }

}
