package de.kontext_e.jqassistant.plugin.asciidoc.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.*;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.ast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

class AsciidocImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsciidocImporter.class);
    private static final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

    private final File file;
    private final Store store;
    private final Options parameters = Options.builder().build();

    AsciidocImporter(final File file, final Store store, int structureMaxLevel) {
        this.file = file;
        this.store = store;
    }

    void importDocument(BlockContainer asciidocFile) {
        Document document = asciidoctor.loadFile(file, parameters);
        scanBlocks(document.getBlocks(), asciidocFile);
    }

    void scanBlocks(List<StructuralNode> blocks, BlockContainer blockContainer) {
        // was for(AbstractBlock block : blocks) but there is a bug in Asciidoctor 1.5.4.1
        // which causes a ClassCastException
        // so check first if it is really an StructuralNode
        for (Object o: blocks) {
            if(o instanceof StructuralNode) {
                StructuralNode block = (StructuralNode) o;
                // ListItem is reported twice: as first class and as part of ListNode
                if (block instanceof ListItem) continue;

                try {
                    AsciidocBlockDescriptor blockDescriptor = scanOneBlock(block);
                    blockContainer.getAsciidocBlocks().add(blockDescriptor);
                    scanBlocks(block.getBlocks(), blockDescriptor);
                } catch (Exception e) {
                    LOGGER.warn("Error while scanning Asciidoc block " + block.getNodeName() + "; reason is: " + e, e);
                }
            }
        }
    }

    private AsciidocBlockDescriptor scanOneBlock(final StructuralNode block) {
        AsciidocBlockDescriptor blockDescriptor;
        if(block instanceof Table) {
            blockDescriptor = scanTableBlock((Table) block);
        } else if(block instanceof org.asciidoctor.ast.List) {
            blockDescriptor = scanListBlock((org.asciidoctor.ast.List) block);
        } else if(block instanceof ListItem) {
            blockDescriptor = scanListItemBlock((ListItem) block);
        } else if(block instanceof Section) {
            blockDescriptor = scanSectionBlock((Section) block);
        } else if(block instanceof Block) {
            blockDescriptor = store.create(AsciidocBlockDescriptor.class);
        } else {
            blockDescriptor = store.create(AsciidocBlockDescriptor.class);
        }

        setCommonBlockProperties(block, blockDescriptor);
        return blockDescriptor;
    }

    private AsciidocBlockDescriptor scanListItemBlock(final ListItem listItem) {
        final AsciidocListItemDescriptor listItemDescriptor = store.create(AsciidocListItemDescriptor.class);
        listItemDescriptor.setMarker(listItem.getMarker());
        listItemDescriptor.setText(listItem.getText());
        // currently disabled because of
        // org.jruby.exceptions.RaiseException: (NoMethodError) undefined method `hasText' for #<Asciidoctor::ListItem:0x3e592f7f>
        // listItemDescriptor.setHasText(listItem.hasText());
        return listItemDescriptor;
    }

    private AsciidocBlockDescriptor scanListBlock(final org.asciidoctor.ast.List list) {
        final AsciidocListDescriptor listDescriptor = store.create(AsciidocListDescriptor.class);
        for (Object o: list.getItems()) {
            if(o instanceof StructuralNode) {
                StructuralNode StructuralNode = (StructuralNode) o;
                listDescriptor.getListItems().add(scanOneBlock(StructuralNode));
            }
        }

        addAttributes(listDescriptor, list.getAttributes());

        return listDescriptor;
    }

    private AsciidocBlockDescriptor scanSectionBlock(final Section section) {
        final AsciidocSectionDescriptor sectionDescriptor = store.create(AsciidocSectionDescriptor.class);
        sectionDescriptor.setIndex(section.index());
        sectionDescriptor.setNumber(section.number());
        sectionDescriptor.setNumbered(section.numbered());
        sectionDescriptor.setSectname(section.sectname());
        sectionDescriptor.setSpecial(section.special());

        addAttributes(sectionDescriptor, section.getAttributes());

        return sectionDescriptor;
    }

    private AsciidocTableDescriptor scanTableBlock(final Table table) {
        final AsciidocTableDescriptor tableDescriptor = store.create(AsciidocTableDescriptor.class);
        tableDescriptor.setFrame(table.getFrame());
        tableDescriptor.setGrid(table.getGrid());

        addAttributes(tableDescriptor, table.getAttributes());

        int colnumber = 0;
        for (Column column : table.getColumns()) {
            tableDescriptor.getAsciidocTableColumns().add(scanTableColumn(column, colnumber++));
        }

        int rownumber = 0;
        for (Row row : table.getHeader()) {
        	tableDescriptor.getAsciidocTableHeaderRows().add(scanTableRow(row, rownumber, tableDescriptor.getAsciidocTableColumns()));
        	rownumber++;
        }

		rownumber = 0;
        for (Row row : table.getBody()) {
            tableDescriptor.getAsciidocTableBodyRows().add(scanTableRow(row, rownumber, tableDescriptor.getAsciidocTableColumns()));
			rownumber++;
        }

		rownumber = 0;
        for (Row row : table.getFooter()) {
            tableDescriptor.getAsciidocTableFooterRows().add(scanTableRow(row, rownumber, tableDescriptor.getAsciidocTableColumns()));
			rownumber++;
        }

        return tableDescriptor;
    }

    private void addAttributes(final AsciidocCommonProperties descriptor, Map<String, Object> attributes) {
        // forEach does not work because of asciidocj bug:
        // java.lang.ClassCastException: org.jruby.RubySymbol cannot be cast to java.lang.String
        for (Map.Entry<String, Object> stringObjectEntry : attributes.entrySet()) {
            final AsciidocAttribute asciidocAttribute = store.create(AsciidocAttribute.class);
            final Object key = stringObjectEntry.getKey();
            if(key instanceof String) {
                asciidocAttribute.setName((String)key);
            } else {
                asciidocAttribute.setName(key.toString());
            }
            asciidocAttribute.setValue("" + stringObjectEntry.getValue());
            descriptor.getAttributes().add(asciidocAttribute);
        }
    }

    private AsciidocTableColumnDescriptor scanTableColumn(final Column column, final int colnumber) {
        final AsciidocTableColumnDescriptor columnDescriptor = store.create(AsciidocTableColumnDescriptor.class);
        // not yet implemented in Asciidoctor columnDescriptor.setColnumber(column.getColnumber());
        // use this workaround:
        columnDescriptor.setColnumber(colnumber);
        addCommonProperties(column, columnDescriptor);
        return columnDescriptor;
    }

    private void setCommonBlockProperties(final StructuralNode block, final AsciidocBlockDescriptor blockDescriptor) {
        blockDescriptor.setContext(block.getContext());
        blockDescriptor.setLevel(block.getLevel());
        blockDescriptor.setRole(block.getRole());
        blockDescriptor.setStyle(block.getStyle());
        blockDescriptor.setTitle(block.getTitle());
        blockDescriptor.setReftext(block.getReftext());
        addCommonProperties(block, blockDescriptor);
    }

    private AsciidocTableRowDescriptor scanTableRow(final Row row, final int rownumber, final List<AsciidocTableColumnDescriptor> columns) {
        int colNumber = 0;
        AsciidocTableRowDescriptor rowDescriptor = store.create(AsciidocTableRowDescriptor.class);
		rowDescriptor.setRownumber(rownumber);
        for (Cell cell : row.getCells()) {
            AsciidocTableCellDescriptor cellDescriptor = store.create(AsciidocTableCellDescriptor.class);
            rowDescriptor.getAsciidocTableCells().add(cellDescriptor);
            cellDescriptor.setText(cell.getText());
			cellDescriptor.setColumn(columns.get(colNumber));

			// does not work because of
// java.lang.ClassCastException: org.jruby.gen.InterfaceImpl1670529912 cannot be cast to org.asciidoctor.ast.Column
// try again with later asciidoctorj version
//            cellDescriptor.setColnumber(cell.getColumn().getColnumber());

            // instead this workaround is used
            cellDescriptor.setColnumber(colNumber++);

            addCommonProperties(cell, cellDescriptor);
        }
        return rowDescriptor;
    }

    private void addCommonProperties(final ContentNode abstractNode, final AsciidocCommonProperties descriptor) {
        descriptor.setContext(abstractNode.getContext());
        descriptor.setReftext(abstractNode.getReftext());
        descriptor.setRole(abstractNode.getRole());
        if(abstractNode instanceof StructuralNode) {
            StructuralNode structuralNode = (StructuralNode) abstractNode;
            descriptor.setStyle(structuralNode.getStyle());
        }
    }

    public static void main(String[] args) {
        new AsciidocImporter(new File("asciidoc/src/test/asciidoc/testfile.adoc"), null, 20).importDocument(null);
    }
}
