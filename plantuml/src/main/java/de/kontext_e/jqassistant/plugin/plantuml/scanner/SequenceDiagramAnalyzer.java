package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlDiagramDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlParticipantDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlSequenceDiagramMessageDescriptor;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.sequencediagram.*;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.text.Guillemet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SequenceDiagramAnalyzer extends AbstractDiagramAnalyzer{

    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceDiagramAnalyzer.class);
    private final Map<String, PlantUmlParticipantDescriptor> participantDescriptors = new HashMap<>();

    public SequenceDiagramAnalyzer(Store store, String pictureFileName, String pictureFileType){
        this.store = store;
        this.pictureFileName = pictureFileName;
        this.pictureFileType = pictureFileType;
    }

    @Override
    protected void analyzeDiagramContent(UmlDiagram diagram, PlantUmlDiagramDescriptor diagramDescriptor) {
        SequenceDiagram sequenceDiagram = (SequenceDiagram) diagram;
        sequenceDiagram.participants().forEach(this::addParticipant);
        sequenceDiagram.events().forEach(this::addEvent);
    }

    private void addParticipant(final Participant participant) {
        final PlantUmlParticipantDescriptor plantUmlParticipantDescriptor = store.create(PlantUmlParticipantDescriptor.class);

        final ParticipantType type = participant.getType();
        plantUmlParticipantDescriptor.setType(type.name());
        plantUmlParticipantDescriptor.setName(participant.getCode());

        final Stereotype stereotype = participant.getStereotype();
        if(stereotype != null) {
            plantUmlParticipantDescriptor.setStereotype(stereotype.getLabel(Guillemet.GUILLEMET));
        }

        participantDescriptors.put(participant.getCode(), plantUmlParticipantDescriptor);
    }

    private void addEvent(final Event event) {
        if(event instanceof Message) {
            Message message = (Message) event;
            final String messageText = message.getLabel().toString();
            final String messageNumber = message.getMessageNumber();
            final Participant participant1 = message.getParticipant1();
            final Participant participant2 = message.getParticipant2();
            final String participant1Name = participant1.getCode();
            final String participant2Name = participant2.getCode();

            final PlantUmlParticipantDescriptor p1 = participantDescriptors.get(participant1Name);
            if(p1 == null) {
                LOGGER.warn("Participant "+participant1Name+" not registered.");
                return;
            }

            final PlantUmlParticipantDescriptor p2 = participantDescriptors.get(participant2Name);
            if(p2 == null) {
                LOGGER.warn("Participant "+participant2Name+" not registered.");
                return;
            }

            final PlantUmlSequenceDiagramMessageDescriptor messageDescriptor = store.create(p1, PlantUmlSequenceDiagramMessageDescriptor.class, p2);
            messageDescriptor.setMessage(messageText);
            messageDescriptor.setMessageNumber(messageNumber);
        }
    }
}
