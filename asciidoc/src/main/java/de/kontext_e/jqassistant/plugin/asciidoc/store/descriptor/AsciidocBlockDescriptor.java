package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Block")
public interface AsciidocBlockDescriptor extends AsciidocDescriptor, BlockContainer {

    @Property("context")
    void setContext(String context);
    String getContext();

    @Property("role")
    void setRole(String role);
    String getRole();

    @Property("style")
    void setStyle(String style);
    String getStyle();

    @Property("title")
    void setTitle(String title);
    String getTitle();

    @Property("reftext")
    void setReftext(String reftext);
    String getReftext();

    @Property("level")
    void setLevel(Integer level);
    Integer getLevel();

}
