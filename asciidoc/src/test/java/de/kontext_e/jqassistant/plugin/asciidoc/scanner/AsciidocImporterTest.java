package de.kontext_e.jqassistant.plugin.asciidoc.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.Document;
import org.junit.Test;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocAttribute;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocBlockDescriptor;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocTableCellDescriptor;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocTableColumnDescriptor;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocTableDescriptor;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocTableRowDescriptor;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.BlockContainer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsciidocImporterTest {

    @Test
    public void testScanTable() {
        File mockFile = mock(File.class);
        Store mockStore = mock(Store.class);
        AsciidocImporter asciidocImporter = new AsciidocImporter(mockFile, mockStore, 5);
        final Map<String, Object> parameters = new HashMap<>();
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        String content = ".Description of de.kontext_e.jqassistant.plugin.plantuml packages\n" +
                         "[options=\"header\", myAttribute, architecture=\"packages\"]\n" +
                         "|====\n" +
                         "| Package       | Purpose\n" +
                         "| scanner       | Scan a text file and create Neo4j nodes for packages and relationships for links.\n" +
                         "| store         | contains jQAssistant compliant descriptors for Neo4j nodes.\n" +
                         "|====\n";

        Document document = asciidoctor.load(content, parameters);
        final BlockContainer mockBlockContainer = mock(BlockContainer.class);
        final Set<AsciidocBlockDescriptor> asciidocBlocks = new HashSet<>();
        when(mockBlockContainer.getAsciidocBlocks()).thenReturn(asciidocBlocks);
        final AsciidocTableDescriptor mockTableDescriptor = mock(AsciidocTableDescriptor.class);
        when(mockStore.create(AsciidocTableDescriptor.class)).thenReturn(mockTableDescriptor);
        final AsciidocTableColumnDescriptor mockTableColumnDescriptor = mock(AsciidocTableColumnDescriptor.class);
        when(mockStore.create(AsciidocTableColumnDescriptor.class)).thenReturn(mockTableColumnDescriptor);
        final AsciidocAttribute mockAttribute = mock(AsciidocAttribute.class);
        when(mockStore.create(AsciidocAttribute.class)).thenReturn(mockAttribute);
        final AsciidocTableCellDescriptor mockAsciidocTableCellDescriptor = mock(AsciidocTableCellDescriptor.class);
        when(mockStore.create(AsciidocTableCellDescriptor.class)).thenReturn(mockAsciidocTableCellDescriptor);
        final AsciidocTableRowDescriptor mockAsciidocTableRowDescriptor = mock(AsciidocTableRowDescriptor.class);
        when(mockStore.create(AsciidocTableRowDescriptor.class)).thenReturn(mockAsciidocTableRowDescriptor);
        when(mockTableDescriptor.getAttributes()).thenReturn(new HashSet<>());
		List<AsciidocTableColumnDescriptor> columns = new ArrayList<>();
		when(mockTableDescriptor.getAsciidocTableColumns()).thenReturn(columns);
        asciidocImporter.scanBlocks(document.blocks(), mockBlockContainer);

        verify(mockAsciidocTableCellDescriptor).setText("Package");
        verify(mockAsciidocTableCellDescriptor).setText("Purpose");
        verify(mockAsciidocTableCellDescriptor).setText("scanner");
        verify(mockAsciidocTableCellDescriptor).setText("store");
        verify(mockAttribute).setName("header-option");
        verify(mockAttribute).setValue("myAttribute");
        verify(mockAttribute).setName("architecture");
        verify(mockAttribute).setValue("packages");
    }

}
