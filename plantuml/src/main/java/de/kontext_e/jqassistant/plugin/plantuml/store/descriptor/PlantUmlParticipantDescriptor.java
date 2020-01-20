package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import java.util.Set;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Participant")
public interface PlantUmlParticipantDescriptor extends PlantUmlDescriptor {

    @Relation.Outgoing
    Set<PlantUmlSequenceDiagramMessageDescriptor> getSource();

    @Relation.Incoming
    Set<PlantUmlSequenceDiagramMessageDescriptor> getTarget();

    @Property("type")
    String getType();
    void setType(String name);

    @Property("name")
    String getName();
    void setName(String actorName);

    @Property("stereotype")
    String getStereotype();
    void setStereotype(String stereotype);
}
