package de.kontext_e.jqassistant.plugin.asciidoc.scanner;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.*;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.store.api.Store;

class AsciidocImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsciidocImporter.class);
    private static final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

    private final File file;
    private final Store store;
    private final Map<String, Object> parameters = new HashMap<>();

    AsciidocImporter(final File file, final Store store, int structureMaxLevel) {
        this.file = file;
        this.store = store;
        parameters.put(Asciidoctor.STRUCTURE_MAX_LEVEL, structureMaxLevel);
    }

    void importDocument(BlockContainer asciidocFile) {
        Document document = asciidoctor.loadFile(file, parameters);
        scanBlocks(document.getBlocks(), asciidocFile);
    }

    void scanBlocks(List<StructuralNode> blocks, BlockContainer blockContainer) {
        // FIXME in asciidoctor 1.6.0 this can be NULL
        if(blocks == null) return;

        for (StructuralNode block: blocks) {
            try {
                AsciidocBlockDescriptor blockDescriptor = scanOneBlock(block);
                blockContainer.getAsciidocBlocks().add(blockDescriptor);
                scanBlocks(block.getBlocks(), blockDescriptor);
            } catch (Exception e) {
                LOGGER.warn("Error while scanning Asciidoc block " + block.getNodeName() + "; reason is: " + e, e);
            }
        }
    }

    private AsciidocBlockDescriptor scanOneBlock(final StructuralNode block) {
        AsciidocBlockDescriptor blockDescriptor;
        if(block instanceof Table) {
            blockDescriptor = scanTableBlock((Table) block);
        } else if(block instanceof org.asciidoctor.ast.List) {
            blockDescriptor = scanListBlock((org.asciidoctor.ast.List) block); // FIXME not happy with FQN
        } else if(block instanceof ListItem) {
            blockDescriptor = scanListItemBlock((ListItem) block);
        } else if(block instanceof DescriptionList) {
            blockDescriptor = scanDescriptionListBlock((DescriptionList) block);
        } else if(block instanceof DescriptionListEntry) {
            blockDescriptor = scanDescriptionListEntryBlock((DescriptionListEntry) block);
        } else if(block instanceof Section) {
            blockDescriptor = scanSectionBlock((Section) block);
        } else if(block instanceof Block) {
            blockDescriptor = store.create(AsciidocBlockDescriptor.class);
        } else {
            LOGGER.warn(" -------------------------> Unhandled case for "+block.getClass().getName());
            LOGGER.warn("Assume Generic Block");
            blockDescriptor = store.create(AsciidocBlockDescriptor.class);
        }

        setCommonBlockProperties(block, blockDescriptor);
        return blockDescriptor;
    }

    private AsciidocListItemDescriptor scanListItemBlock(final ListItem listItem) {
        final AsciidocListItemDescriptor listItemDescriptor = store.create(AsciidocListItemDescriptor.class);
        listItemDescriptor.setMarker(listItem.getMarker());
        listItemDescriptor.setSource(listItem.getSource());
        listItemDescriptor.setText(listItem.getText());
        listItemDescriptor.setHasText(listItem.hasText());
        return listItemDescriptor;
    }

    private AsciidocBlockDescriptor scanListBlock(final org.asciidoctor.ast.List list) {
        final AsciidocListDescriptor listDescriptor = store.create(AsciidocListDescriptor.class);
        for (StructuralNode abstractBlock: list.getItems()) {
            listDescriptor.getListItems().add(scanOneBlock(abstractBlock));
        }

        addAttributes(listDescriptor, list.getAttributes());

        return listDescriptor;
    }

    private AsciidocBlockDescriptor scanDescriptionListBlock(final DescriptionList list) {
        final AsciidocDescriptionListDescriptor listDescriptor = store.create(AsciidocDescriptionListDescriptor.class);
        for (DescriptionListEntry abstractBlock: list.getItems()) {
            listDescriptor.getListItems().add(scanDescriptionListEntryBlock(abstractBlock));
        }

        addAttributes(listDescriptor, list.getAttributes());

        return listDescriptor;
    }

    private AsciidocDescriptionListEntryDescriptor scanDescriptionListEntryBlock(final DescriptionListEntry listEntry) {
        final AsciidocDescriptionListEntryDescriptor listEntryDescriptor = store.create(AsciidocDescriptionListEntryDescriptor.class);
        for (ListItem abstractBlock: listEntry.getTerms()) {
            listEntryDescriptor.getListItems().add(scanListItemBlock(abstractBlock));
        }

        listEntryDescriptor.setDescription(scanListItemBlock(listEntry.getDescription()));

        return listEntryDescriptor;
    }

    private AsciidocBlockDescriptor scanSectionBlock(final Section section) {
        final AsciidocSectionDescriptor sectionDescriptor = store.create(AsciidocSectionDescriptor.class);
        sectionDescriptor.setIndex(section.getIndex());
        sectionDescriptor.setNumber(section.getNumber());
        sectionDescriptor.setNumbered(section.isNumbered());
        sectionDescriptor.setSectname(section.getSectionName());
        sectionDescriptor.setSpecial(section.isSpecial());

        addAttributes(sectionDescriptor, section.getAttributes());

        return sectionDescriptor;
    }

    private AsciidocTableDescriptor scanTableBlock(final Table table) {
        final AsciidocTableDescriptor tableDescriptor = store.create(AsciidocTableDescriptor.class);
        tableDescriptor.setFrame(table.getFrame());
        tableDescriptor.setGrid(table.getGrid());

        addAttributes(tableDescriptor, table.getAttributes());

        for (Column column : table.getColumns()) {
            tableDescriptor.getAsciidocTableColumns().add(scanTableColumn(column));
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
        attributes.forEach((key, value) -> {
            final AsciidocAttribute asciidocAttribute = store.create(AsciidocAttribute.class);
            asciidocAttribute.setName(key);
            asciidocAttribute.setValue("" + value);
            descriptor.getAttributes().add(asciidocAttribute);
        });
    }

    private AsciidocTableColumnDescriptor scanTableColumn(final Column column) {
        final AsciidocTableColumnDescriptor columnDescriptor = store.create(AsciidocTableColumnDescriptor.class);
        columnDescriptor.setColnumber(column.getColumnNumber());
        addCommonProperties(column, columnDescriptor);
        return columnDescriptor;
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
            cellDescriptor.setColnumber(cell.getColumn().getColumnNumber());
            cellDescriptor.setRownumber(rownumber);

            addCommonProperties(cell, cellDescriptor);
        }
        return rowDescriptor;
    }

    private void setCommonBlockProperties(final StructuralNode block, final AsciidocBlockDescriptor blockDescriptor) {
        blockDescriptor.setTitle(block.getTitle());
        blockDescriptor.setCaption(block.getCaption());
        blockDescriptor.setStyle(block.getStyle());
        blockDescriptor.setLevel(block.getLevel());
        blockDescriptor.setContext(block.getContext());
        blockDescriptor.setRole(block.getRole());
        blockDescriptor.setReftext(block.getReftext());
        addCommonProperties(block, blockDescriptor);
    }

    private void addCommonProperties(final StructuralNode abstractNode, final AsciidocCommonProperties descriptor) {
        addCommonProperties((ContentNode)abstractNode, descriptor);
        descriptor.setStyle(abstractNode.getStyle());
    }

    private void addCommonProperties(final ContentNode abstractNode, final AsciidocCommonProperties descriptor) {
        descriptor.setContext(abstractNode.getContext());
        descriptor.setReftext(abstractNode.getReftext());
        descriptor.setRole(abstractNode.getRole());
    }
}
