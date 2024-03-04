package de.kontext_e.jqassistant.plugin.dot.store.descriptor;


import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Relation("IS_CONNECTED_WITH")
public interface DotRelationDescriptor extends Descriptor, AttributesContainer {

    @Relation.Outgoing
    DotNodeDescriptor getSourceNode();

    @Relation.Incoming
    DotNodeDescriptor getTargetNode();
}
