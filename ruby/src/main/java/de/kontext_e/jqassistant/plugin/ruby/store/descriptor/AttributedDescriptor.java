package de.kontext_e.jqassistant.plugin.ruby.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

public interface AttributedDescriptor {

    @Relation("HAS_ATTRIBUTE")
    List<AttributeDescriptor> getAttributes();

}
