package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Table")
public interface AsciidocTableDescriptor extends AsciidocBlockDescriptor {

    @Relation("HAS_COLUMN")
	List<AsciidocTableColumnDescriptor> getAsciidocTableColumns();

    @Relation("HAS_HEADER_ROW")
    List<AsciidocTableRowDescriptor> getAsciidocTableHeaderRows();

    @Relation("HAS_ROW")
    List<AsciidocTableRowDescriptor> getAsciidocTableBodyRows();

    @Relation("HAS_FOOTER_ROW")
    List<AsciidocTableRowDescriptor> getAsciidocTableFooterRows();

    @Property("frame")
    String getFrame();
    void setFrame(String frame);

    @Property("grid")
    String getGrid();
    void setGrid(String grid);

}
