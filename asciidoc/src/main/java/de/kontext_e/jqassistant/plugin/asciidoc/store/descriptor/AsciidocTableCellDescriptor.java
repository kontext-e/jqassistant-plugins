package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Cell")
public interface AsciidocTableCellDescriptor extends AsciidocDescriptor {
    @Property("text")
    void setText(String text);
    String getText();

    @Property("style")
    void setStyle(String style);
    String getStyle();

    @Property("colnumber")
    void setColnumber(Integer colnumber);
    Integer getColnumber();
}
