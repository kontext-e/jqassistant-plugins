package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("ListItem")
public interface AsciidocListItemDescriptor extends AsciidocBlockDescriptor {
    @Property("marker")
    String getMarker();
    void setMarker(String marker);

    @Property("text")
    String getText();
    void setText(String text);

    @Property("hasText")
    Boolean getHasText();
    void setHasText(Boolean hasText);
}
