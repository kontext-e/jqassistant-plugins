package de.kontext_e.jqassistant.plugin.asciidoc.scanner;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.Cell;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.Row;
import org.asciidoctor.ast.Table;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocBlockDescriptor;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocTableCellDescriptor;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocTableDescriptor;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocTableRowDescriptor;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.BlockContainer;

class AsciidocImporter {
    private static final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

    private final File file;
    private final Store store;
    private final Map<String, Object> parameters = new HashMap<>();

    AsciidocImporter(final File file, final Store store) {
        this.file = file;
        this.store = store;
        parameters.put(Asciidoctor.STRUCTURE_MAX_LEVEL, 20);
    }

    void importDocument(BlockContainer asciidocFile) {
        Document document = asciidoctor.loadFile(file, parameters);
        scanBlocks(document.getBlocks(), asciidocFile);
    }

    private void scanBlocks(List<AbstractBlock> blocks, BlockContainer blockContainer) {
        for (AbstractBlock block : blocks) {
            AsciidocBlockDescriptor blockDescriptor;
            if(block instanceof Table) {
                blockDescriptor = scanTableBlock((Table)block);
            } else {
                blockDescriptor = store.create(AsciidocBlockDescriptor.class);
            }

            setCommonBlockProperties(block, blockDescriptor);
            blockContainer.getAsciidocBlocks().add(blockDescriptor);
            scanBlocks(block.getBlocks(), blockDescriptor);
        }
    }

    private void setCommonBlockProperties(final AbstractBlock block, final AsciidocBlockDescriptor blockDescriptor) {
        blockDescriptor.setContext(block.getContext());
        blockDescriptor.setLevel(block.getLevel());
        blockDescriptor.setRole(block.getRole());
        blockDescriptor.setStyle(block.getStyle());
        blockDescriptor.setTitle(block.getTitle());
        blockDescriptor.setReftext(block.getReftext());
    }

    private AsciidocTableDescriptor scanTableBlock(final Table table) {
        final AsciidocTableDescriptor tableDescriptor = store.create(AsciidocTableDescriptor.class);
        tableDescriptor.setFrame(table.getFrame());
        tableDescriptor.setGrid(table.getGrid());

        for (Row row : table.getHeader()) {
            tableDescriptor.getAsciidocTableHeaderRows().add(scanTableRow(row));
        }

        for (Row row : table.getBody()) {
            tableDescriptor.getAsciidocTableBodyRows().add(scanTableRow(row));
        }

        for (Row row : table.getFooter()) {
            tableDescriptor.getAsciidocTableFooterRows().add(scanTableRow(row));
        }

        return tableDescriptor;
    }

    private AsciidocTableRowDescriptor scanTableRow(final Row row) {
        int colNumber = 0;
        AsciidocTableRowDescriptor rowDescriptor = store.create(AsciidocTableRowDescriptor.class);
        for (Cell cell : row.getCells()) {
            AsciidocTableCellDescriptor cellDescriptor = store.create(AsciidocTableCellDescriptor.class);
            rowDescriptor.getAsciidocTableCells().add(cellDescriptor);
            cellDescriptor.setText(cell.getText());
            cellDescriptor.setStyle(cell.getStyle());
// does not work because of
// java.lang.ClassCastException: org.jruby.gen.InterfaceImpl1670529912 cannot be cast to org.asciidoctor.ast.Column
// try again with later asciidoctorj version
//            cellDescriptor.setColnumber(cell.getColumn().getColnumber());

            // instead this workaround is used
            cellDescriptor.setColnumber(colNumber++);

        }
        return rowDescriptor;
    }

    public static void main(String[] args) {
        new AsciidocImporter(new File("asciidoc/src/test/asciidoc/testfile.adoc"), null).importDocument(null);
    }
}
