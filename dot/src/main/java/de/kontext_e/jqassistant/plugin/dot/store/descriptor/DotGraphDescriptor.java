package de.kontext_e.jqassistant.plugin.dot.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("Graph")
public interface DotGraphDescriptor extends DotDescriptor, HasGraph, AttributesContainer {

    @Property("strict")
    Boolean isStrict();
    void setStrict(Boolean value);

    @Property("type")
    String getType();
    void setType(String value);

    @Property("dotId")
    String getDotId();
    void setDotId(String value);

    @Relation("HAS_NODE")
    Set<DotNodeDescriptor> getNodes();
}
