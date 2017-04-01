package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import java.util.Set;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Table")
public interface AsciidocTableDescriptor extends AsciidocBlockDescriptor {

    @Relation("HEADER")
    Set<AsciidocTableRowDescriptor> getAsciidocTableHeaderRows();

    @Relation("BODY")
    Set<AsciidocTableRowDescriptor> getAsciidocTableBodyRows();

    @Relation("FOOTER")
    Set<AsciidocTableRowDescriptor> getAsciidocTableFooterRows();

    @Property("frame")
    void setFrame(String frame);
    String getFrame();

    @Property("grid")
    void setGrid(String grid);
    String getGrid();

}
