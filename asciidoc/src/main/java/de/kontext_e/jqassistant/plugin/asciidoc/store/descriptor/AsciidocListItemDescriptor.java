package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("ListItem")
public interface AsciidocListItemDescriptor extends AsciidocBlockDescriptor {
    @Property("marker")
    void setMarker(String marker);
    String getMarker();

    @Property("text")
    void setText(String text);
    String getText();

    @Property("hasText")
    void setHasText(Boolean hasText);
    Boolean getHasText();

    @Property("source")
    void setSource(String source);
    String getSource();
}
