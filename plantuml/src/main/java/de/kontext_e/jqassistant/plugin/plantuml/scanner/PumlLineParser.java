package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlClassDiagramDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlDescriptionDiagramDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlDiagramDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlElement;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlFileDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlGroupDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlLeafDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlPackageDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlParticipantDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlSequenceDiagramDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlSequenceDiagramMessageDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlStateDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlStateDiagramDescriptor;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

class PumlLineParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PumlLineParser.class);

    private final Store store;
    private final Map<String, PlantUmlElement> mappingFromFqnToPackage = new HashMap<>();
    private final PlantUmlFileDescriptor plantUmlFileDescriptor;
    private ParsingState parsingState = ParsingState.ACCEPTING;
    private StringBuilder lineBuffer = new StringBuilder();
    private final Map<String, PlantUmlParticipantDescriptor> participantDescriptors = new HashMap<>();

    PumlLineParser(final Store store, final PlantUmlFileDescriptor plantUmlFileDescriptor, final ParsingState parsingState) {
        this.store = store;
        this.plantUmlFileDescriptor = plantUmlFileDescriptor;
        this.parsingState = parsingState;
    }

    void parseLine(final String line) {
        if(line == null) return;

        String normalizedLine = line.trim().toLowerCase();

        if(parsingState == ParsingState.IGNORING && normalizedLine.startsWith("[\"plantuml\"")) {
            parsingState = ParsingState.PLANTUMLFOUND;
        }

        if(parsingState == ParsingState.ACCEPTING && (normalizedLine.startsWith("----") || normalizedLine.startsWith("@enduml"))) {
            parsingState = ParsingState.IGNORING;
            lineBuffer.append("\n");
            lineBuffer.append("@enduml");
            storeDiagram();
        }

        if(parsingState == ParsingState.PLANTUMLFOUND && normalizedLine.startsWith("----")) {
            parsingState = ParsingState.ACCEPTING;
            lineBuffer = new StringBuilder();
            lineBuffer.append("@startuml");
            lineBuffer.append("\n");
            return;
        }

        if(parsingState == ParsingState.ACCEPTING) {
            lineBuffer.append(normalizedLine);
            lineBuffer.append("\n");
        }
    }

    private void storeDiagram() {
        SourceStringReader reader = new SourceStringReader(lineBuffer.toString());
        List<BlockUml> blocks = reader.getBlocks();
        if(blocks.isEmpty()) return;

        final Diagram diagram = blocks.get(0).getDiagram();
        if(diagram instanceof PSystemError) {
            PSystemError error = (PSystemError) diagram;
            final int higherErrorPosition = error.getHigherErrorPosition();
            LOGGER.warn("Error while parsing at postion "+higherErrorPosition+": "+error.getErrorsUml());
            return;
        }


        if(diagram instanceof SequenceDiagram) {
            final SequenceDiagram sequenceDiagram = (SequenceDiagram) diagram;
            final UmlDiagramType umlDiagramType = sequenceDiagram.getUmlDiagramType();
            final PlantUmlDiagramDescriptor diagramDescriptor = createDiagramDescriptor(umlDiagramType);
            if(diagramDescriptor == null) return;

            final DiagramDescription description = sequenceDiagram.getDescription();
            final String type = description.getType();
            diagramDescriptor.setType(type);

            sequenceDiagram.participants().forEach(this::addParticipant);
            sequenceDiagram.events().forEach(this::addEvent);

            plantUmlFileDescriptor.getPlantUmlDiagrams().add(diagramDescriptor);
            setOldRelationsForCompatibility(diagramDescriptor);
        }
        else if(diagram instanceof AbstractEntityDiagram) {
            final AbstractEntityDiagram descriptionDiagram = (AbstractEntityDiagram) diagram;
            final UmlDiagramType umlDiagramType = descriptionDiagram.getUmlDiagramType();
            final PlantUmlDiagramDescriptor diagramDescriptor = createDiagramDescriptor(umlDiagramType);
            if(diagramDescriptor == null) return;

            final DiagramDescription description = descriptionDiagram.getDescription();
            final String type = description.getType();
            diagramDescriptor.setType(type);

            final String namespaceSeparator = descriptionDiagram.getNamespaceSeparator();
            diagramDescriptor.setNamespaceSeparator(namespaceSeparator);

            final Collection<IGroup> groups = descriptionDiagram.getRootGroup().getChildren();
            addGroups(diagramDescriptor, groups, null);

            final Collection<ILeaf> leafsvalues = descriptionDiagram.getRootGroup().getLeafsDirect();
            addLeafs(leafsvalues, null);

            final List<Link> links = descriptionDiagram.getLinks();
            addLinks(links);

            plantUmlFileDescriptor.getPlantUmlDiagrams().add(diagramDescriptor);
            setOldRelationsForCompatibility(diagramDescriptor);
        }
    }

    private PlantUmlDiagramDescriptor createDiagramDescriptor(final UmlDiagramType umlDiagramType) {
        switch (umlDiagramType) {
            case ACTIVITY: break;
            case CLASS:
                return store.create(PlantUmlClassDiagramDescriptor.class);
            case COMPOSITE: break;
            case DESCRIPTION:
                return store.create(PlantUmlDescriptionDiagramDescriptor.class);
            case FLOW: break;
            case OBJECT: break;
            case SEQUENCE:
                return store.create(PlantUmlSequenceDiagramDescriptor.class);
            case STATE:
                return store.create(PlantUmlStateDiagramDescriptor.class);
            case TIMING: break;
            default: break;
        }

        LOGGER.warn("Diagram type not yet supported: "+umlDiagramType);
        return null;
    }

    private void setOldRelationsForCompatibility(final PlantUmlDiagramDescriptor diagramDescriptor) {
        if(diagramDescriptor instanceof PlantUmlClassDiagramDescriptor) {
            PlantUmlClassDiagramDescriptor pucdd = (PlantUmlClassDiagramDescriptor) diagramDescriptor;
            pucdd.getPlantUmlGroups().forEach(g -> {
                if(g instanceof PlantUmlPackageDescriptor) {
                    PlantUmlPackageDescriptor pupd = (PlantUmlPackageDescriptor) g;
                    pupd.getLinkTargets().forEach(lt -> {
                        if(lt instanceof PlantUmlPackageDescriptor) {
                            pupd.getMayDependOnPackages().add((PlantUmlPackageDescriptor) lt);
                        }
                    });
                    pupd.getChildGroups().forEach(cg -> {
                        if(cg instanceof PlantUmlPackageDescriptor) {
                            pupd.getContainedPackages().add((PlantUmlPackageDescriptor) cg);
                        }
                    });
                }
            });
        }
    }

    private void addLinks(final List<Link> links) {
        for (Link link : links) {
            String lhs = link.getEntity1().getCode().getFullName();
            String rhs = link.getEntity2().getCode().getFullName();
            if("ARROW".equalsIgnoreCase(link.getType().getDecor2().name())) {
                String swap = rhs;
                rhs = lhs;
                lhs = swap;
            }

            final PlantUmlElement lhsDescriptor = mappingFromFqnToPackage.get(lhs);
            if(lhsDescriptor == null) {
                LOGGER.warn("No jQAssistant node created for left side of link from "+lhs+" to "+rhs);
                continue;
            }

            final PlantUmlElement rhsDescriptor = mappingFromFqnToPackage.get(rhs);
            if(rhsDescriptor == null) {
                LOGGER.warn("No jQAssistant node created for right side of link from "+lhs+" to "+rhs);
                continue;
            }

            lhsDescriptor.getLinkTargets().add(rhsDescriptor);
        }
    }

    private void addLeafs(final Collection<ILeaf> leafsvalues, final PlantUmlGroupDescriptor plantUmlGroupDescriptor) {
        for (final ILeaf iLeaf : leafsvalues) {
            PlantUmlLeafDescriptor leafNode = store.create(PlantUmlLeafDescriptor.class);
            final String fullName = iLeaf.getCode().getFullName();
            leafNode.setFullName(fullName);
            final LeafType entityType = iLeaf.getEntityType();
            leafNode.setType(entityType.name());
            plantUmlFileDescriptor.getPlantUmlElements().add(leafNode);
            mappingFromFqnToPackage.put(fullName, leafNode);
            final Stereotype stereotype = iLeaf.getStereotype();
            if(stereotype != null) {
                leafNode.setStereotype(stereotype.getLabel(true));
            }
            if(plantUmlGroupDescriptor != null) {
                plantUmlGroupDescriptor.getLeafs().add(leafNode);
            }
        }
    }

    private void addGroups(final PlantUmlDiagramDescriptor diagramDescriptor, final Collection<IGroup> groups, PlantUmlGroupDescriptor parent) {
        for (IGroup iGroup : groups) {
            final GroupType groupType = iGroup.getGroupType();
            PlantUmlGroupDescriptor plantUmlGroupDescriptor = null;
            switch (groupType) {
                case CONCURRENT_ACTIVITY: break;
                case CONCURRENT_STATE: break;
                case INNER_ACTIVITY: break;
                case PACKAGE:
                    plantUmlGroupDescriptor = store.create(PlantUmlPackageDescriptor.class);
                    break;
                case STATE:
                    plantUmlGroupDescriptor = store.create(PlantUmlStateDescriptor.class);
                    break;
                default: break;
            }

            if(plantUmlGroupDescriptor != null) {
                final String fullGroupName = iGroup.getCode().getFullName();
                plantUmlGroupDescriptor.setFullName(fullGroupName);
                diagramDescriptor.getPlantUmlGroups().add(plantUmlGroupDescriptor);
                mappingFromFqnToPackage.put(fullGroupName, plantUmlGroupDescriptor);
                if(parent != null) {
                    parent.getChildGroups().add(plantUmlGroupDescriptor);
                }

                addGroups(diagramDescriptor, iGroup.getChildren(), plantUmlGroupDescriptor);
                addLeafs(iGroup.getLeafsDirect(), plantUmlGroupDescriptor);
            } else {
                LOGGER.warn("Not handled group type: "+groupType);
            }
        }
    }

    private void addEvent(final Event event) {
        if(event instanceof Message) {
            Message message = (Message) event;
            final String messageText = message.getLabel().asStringWithHiddenNewLine();
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

    private void addParticipant(final String actorName, final Participant participant) {
        final PlantUmlParticipantDescriptor plantUmlParticipantDescriptor = store.create(PlantUmlParticipantDescriptor.class);

        final ParticipantType type = participant.getType();
        plantUmlParticipantDescriptor.setType(type.name());
        plantUmlParticipantDescriptor.setName(actorName);

        final Stereotype stereotype = participant.getStereotype();
        if(stereotype != null) {
            plantUmlParticipantDescriptor.setStereotype(stereotype.getLabel(true));
        }

        participantDescriptors.put(actorName, plantUmlParticipantDescriptor);
    }
}
