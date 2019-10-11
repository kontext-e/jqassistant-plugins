package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Relation
public interface PlantUmlSequenceDiagramMessageDescriptor extends Descriptor {

    @Relation.Outgoing
    PlantUmlParticipantDescriptor getSourceParticipant();

    @Relation.Incoming
    PlantUmlParticipantDescriptor getTargetParticipant();

    void setMessage(String messageText);
    String getMessage();

    void setMessageNumber(String messageNumber);
    String getMessageNumber();
}
