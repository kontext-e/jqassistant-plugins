package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

public interface AsciidocCommonProperties {
    void setContext(String context);
    @Property("context")
    String getContext();

    void setRole(String role);
    @Property("role")
    String getRole();

    void setStyle(String style);
    @Property("style")
    String getStyle();

    void setReftext(String reftext);
    @Property("reftext")
    String getReftext();

    @Relation("HAS_ATTRIBUTE")
    Set<AsciidocAttribute> getAttributes();

}
