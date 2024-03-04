package de.kontext_e.jqassistant.plugin.dot.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import com.khubla.dot4j.DOTMarshaller;
import com.khubla.dot4j.domain.*;
import de.kontext_e.jqassistant.plugin.dot.store.descriptor.*;
import org.junit.Before;
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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class DotScannerPluginTest {
    private Store mockStore = mock(Store.class);
    private DotFileDescriptor mockDotFileDescriptor = mock(DotFileDescriptor.class);
    private DotRelationDescriptor mockDotRelationshipDescriptor = mock(DotRelationDescriptor.class);

    @Before
    public void setUp() {
        when(mockStore.create(any(DotNodeDescriptor.class), eq(DotRelationDescriptor.class), any(DotNodeDescriptor.class))).thenReturn(mockDotRelationshipDescriptor);
    }

    @Test
    public void testImportSimpleGraph() throws IOException {
        // Arrange
        InputStream inputStream = createExapmle1();
        DotScannerPlugin plugin = new DotScannerPlugin();
        Set<DotNodeDescriptor> nodes = new HashSet<>();

        final DotGraphDescriptor mockDotGraphDescriptor = mock(DotGraphDescriptor.class);
        when(mockStore.create(DotGraphDescriptor.class)).thenReturn(mockDotGraphDescriptor);
        when(mockDotGraphDescriptor.getNodes()).thenReturn(nodes);

        final DotNodeDescriptor mockDotNodeDescriptor = mock(DotNodeDescriptor.class);
        when(mockStore.create(DotNodeDescriptor.class)).thenReturn(mockDotNodeDescriptor);

        // Act
        plugin.readStream(mockStore, mockDotFileDescriptor, inputStream);

        // Assert
        verify(mockStore).create(DotGraphDescriptor.class);
        verify(mockStore, times(2)).create(DotNodeDescriptor.class);
        verify(mockDotNodeDescriptor).setDotId("node2");
        verify(mockDotNodeDescriptor).setDotId("n1");
        verify(mockDotNodeDescriptor).setAttribute("att1","\"val1\"","node");
        verify(mockDotNodeDescriptor).setAttribute("att2","\"val2\"","node");
        verify(mockDotRelationshipDescriptor, times(2)).setAttribute("style","dotted","edge");
    }

    @Test
    public void testApi() throws IOException {
        InputStream inputStream = createExapmle2();
        Graph graph = DOTMarshaller.importGraph(inputStream);

        assertThat(graph.getId(), is("\"acc\""));
        assertThat(graph.getGraphType(), is(GraphType.digraph));

        final Map<String, Node> nodes = graph.getNodes();
        assertTrue(nodes.containsKey("\"node4\""));
        Node node4 = nodes.get("\"node4\"");
        final Attributes attributes = node4.getAttributes();
        final AttributeType attributeType = attributes.getAttributeType();
        assertThat(attributeType, is(AttributeType.node));
        final List<Attribute> attributeList = attributes.getAttributes();
        Attribute attribute1 = attributeList.get(0);
        assertThat(attribute1.getLhs(), is("label"));
        assertThat(attribute1.getRhs(), is("\"acc\""));
    }


    private InputStream createExapmle1() {
        String example = "digraph mygraph {\n" +
                "   node2 [att1 = \"val1\", att2 = \"val2\"];\n" +
                "   n1;\n" +
                "   n1 -> node2 [ style = dotted ]\n" +
                "}";
        return new ByteArrayInputStream(example.getBytes(StandardCharsets.UTF_8));
    }

    private InputStream createExapmle2() {
        String example = "digraph \"acc\" {\n" +
                "node [\n" +
                "  fontsize = \"12\"\n" +
                "];\n" +
                "    \"node4\" [ label = \"acc\", shape = octagon ];\n" +
                "    \"node5\" [ label = \"acc-asrc\", shape = octagon ];\n" +
                "    \"node4\" -> \"node5\" [ attr = \"val\"] // acc -> acc-asrc\n" +
                "    \"node6\" [ label = \"cstublib-hifi5\", shape = septagon ];\n" +
                "    \"node5\" -> \"node6\"  // acc-asrc -> cstublib-hifi5\n" +
                "    \"node7\" [ label = \"src-test\", shape = egg ];\n" +
                "    \"node4\" -> \"node7\"  // acc -> src-test\n" +
                "    \"node5\" [ label = \"acc-asrc\", shape = octagon ];\n" +
                "    \"node7\" -> \"node5\" [ style = dotted ] // src-test -> acc-asrc\n" +
                "}\n";
        return new ByteArrayInputStream(example.getBytes(StandardCharsets.UTF_8));
    }

}
