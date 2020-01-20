package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Block")
public interface AsciidocBlockDescriptor extends AsciidocDescriptor, BlockContainer, AsciidocCommonProperties {

    @Property("context")
    String getContext();
    void setContext(String context);

    @Property("role")
    String getRole();
    void setRole(String role);

    @Property("style")
    String getStyle();
    void setStyle(String style);

    @Property("title")
    String getTitle();
    void setTitle(String title);

    @Property("reftext")
    String getReftext();
    void setReftext(String reftext);

    @Property("level")
    Integer getLevel();
    void setLevel(Integer level);

}
