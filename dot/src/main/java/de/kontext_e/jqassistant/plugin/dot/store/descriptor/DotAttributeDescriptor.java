package de.kontext_e.jqassistant.plugin.dot.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Attribute")
public interface DotAttributeDescriptor extends DotDescriptor {

    @Property("name")
    String getName();
    void setName(String value);

    @Property("value")
    String getValue();
    void setValue(String value);

    @Property("type")
    String getType();
    void setType(String value);
}
