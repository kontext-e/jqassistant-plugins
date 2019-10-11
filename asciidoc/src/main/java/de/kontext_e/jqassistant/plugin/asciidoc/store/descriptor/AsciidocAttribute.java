package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Attribute")
public interface AsciidocAttribute extends AsciidocDescriptor, NamedDescriptor {

    @Property("value")
    void setValue(String value);
    String getValue();
}
