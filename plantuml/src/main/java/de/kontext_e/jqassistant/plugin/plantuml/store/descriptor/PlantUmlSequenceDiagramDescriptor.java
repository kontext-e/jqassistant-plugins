package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("SequenceDiagram")
public interface PlantUmlSequenceDiagramDescriptor extends PlantUmlDiagramDescriptor {

    @Relation("HAS_PARTICIPANTS")
    Set<PlantUmlParticipantDescriptor> getPlantUmlParticipants();
}
