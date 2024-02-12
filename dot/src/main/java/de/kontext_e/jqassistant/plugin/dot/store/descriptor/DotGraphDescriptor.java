package de.kontext_e.jqassistant.plugin.dot.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("Graph")
public interface DotGraphDescriptor extends DotDescriptor, HasGraph {

    @Relation("HAS_NODE")
    Set<DotNodeDescriptor> getNodes();
}
