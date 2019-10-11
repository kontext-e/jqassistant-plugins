package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import java.util.Set;

import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

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

    @Relation("HAS_ATTRIBUTE")
    Set<AsciidocAttribute> getAttributes();

}
