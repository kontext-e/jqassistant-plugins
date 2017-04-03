package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Property;

public interface AsciidocCommonProperties {
    @Property("context")
    void setContext(String context);
    String getContext();

    @Property("role")
    void setRole(String role);
    String getRole();

    @Property("style")
    void setStyle(String style);
    String getStyle();

    @Property("reftext")
    void setReftext(String reftext);
    String getReftext();
}
