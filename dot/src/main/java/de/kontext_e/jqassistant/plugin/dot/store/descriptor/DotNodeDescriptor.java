package de.kontext_e.jqassistant.plugin.dot.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Node")
public interface DotNodeDescriptor extends DotDescriptor, AttributesContainer {

    @Property("dotId")
    String getDotId();
    void setDotId(String value);

/*
    @Relation("IS_CONNECTED_WITH")
    Set<DotNodeDescriptor> getConnectedNodes();
*/

    @Relation.Outgoing
    List<DotRelationDescriptor> getConnectedTo();

    @Relation.Incoming
    List<DotRelationDescriptor> getConnectedFrom();
}
