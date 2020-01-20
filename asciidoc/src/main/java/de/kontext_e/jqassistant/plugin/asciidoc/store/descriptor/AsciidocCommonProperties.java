package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import java.util.Set;

import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

public interface AsciidocCommonProperties {
    @Property("context")
    String getContext();
    void setContext(String context);

    @Property("role")
    String getRole();
    void setRole(String role);

    @Property("style")
    String getStyle();
    void setStyle(String style);

    @Property("reftext")
    String getReftext();
    void setReftext(String reftext);

    @Relation("HAS_ATTRIBUTE")
    Set<AsciidocAttribute> getAttributes();

}
