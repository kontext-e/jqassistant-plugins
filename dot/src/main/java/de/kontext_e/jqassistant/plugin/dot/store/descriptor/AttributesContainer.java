package de.kontext_e.jqassistant.plugin.dot.store.descriptor;

import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Abstract
public interface AttributesContainer {

    @Relation("HAS_ATTRIBUTE")
    Set<DotAttributeDescriptor> getAttributes();

}
