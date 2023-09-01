package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.*;
import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.cucadiagram.*;
import net.sourceforge.plantuml.sequencediagram.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class PumlLineParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PumlLineParser.class);

    private final Store store;
    private final Map<String, PlantUmlElement> mappingFromFqnToPackage = new HashMap<>();
    private final PlantUmlFileDescriptor plantUmlFileDescriptor;
    private ParsingState parsingState;
    private StringBuilder lineBuffer = new StringBuilder();
    private final Map<String, PlantUmlParticipantDescriptor> participantDescriptors = new HashMap<>();
    private String pictureFileName;
    private String pictureFileType;

    PumlLineParser(final Store store, final PlantUmlFileDescriptor plantUmlFileDescriptor) {
        this.store = store;
        this.plantUmlFileDescriptor = plantUmlFileDescriptor;
        this.parsingState = ParsingState.SEARCHING;
    }

    void parseLine(String line){
        if (line == null) return;
        String normalizedLine = line.trim().toLowerCase();
        if (normalizedLine.startsWith("!include")) return;

        if (parsingState == ParsingState.SEARCHING && line.startsWith("[\"plantuml\"")){
            parsingState = ParsingState.PUMLFOUNDINADOC;
            evaluatePlantUMLHeader(normalizedLine);
        }

        if (parsingState == ParsingState.SEARCHING && line.startsWith("@startuml")){
            parsingState = ParsingState.ACCEPTING;
        }

        if (parsingState == ParsingState.PUMLFOUNDINADOC && line.startsWith("----")){
            lineBuffer.append("@startuml");
            lineBuffer.append("\n");
            parsingState = ParsingState.ACCEPTING;
            return;
        }

        if (parsingState == ParsingState.ACCEPTING && !line.startsWith("----")){
            lineBuffer.append(normalizedLine);
            lineBuffer.append("\n");
        }

        if (parsingState == ParsingState.ACCEPTING && (line.startsWith("@enduml") || line.startsWith("----"))){
            lineBuffer.append("@enduml");
            storeDiagram();
            resetLineParser();
        }

    }

    private void resetLineParser() {
        pictureFileName = null;
        pictureFileType = null;
        parsingState = ParsingState.SEARCHING;
        lineBuffer.setLength(0);
    }

    private void evaluatePlantUMLHeader(String normalizedLine) {
        final String[] parts = normalizedLine
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .replaceAll("\"", "")
                .replaceAll("\n", "")
                .split(",");
        if(parts != null && parts.length >= 3) {
            pictureFileName = parts[1];
            pictureFileType = parts[2];
        }
    }

    private void storeDiagram() {
        SourceStringReader reader = new SourceStringReader(lineBuffer.toString());
        List<BlockUml> blocks = reader.getBlocks();
        if(blocks.isEmpty()) return;

        final Diagram diagram = blocks.get(0).getDiagram();
        if(diagram instanceof PSystemError) {
            PSystemError error = (PSystemError) diagram;
            final int higherErrorPosition = error.getLineLocation().getPosition();
            LOGGER.warn("Error while parsing at postion "+higherErrorPosition+": "+error.getErrorsUml());
            return;
        }


        if(diagram instanceof SequenceDiagram) {
            final SequenceDiagram sequenceDiagram = (SequenceDiagram) diagram;
            final UmlDiagramType umlDiagramType = sequenceDiagram.getUmlDiagramType();
            final PlantUmlSequenceDiagramDescriptor diagramDescriptor = (PlantUmlSequenceDiagramDescriptor) createDiagramDescriptor(umlDiagramType);
            if(diagramDescriptor == null) return;

            diagramDescriptor.setPictureFileName(pictureFileName);
            diagramDescriptor.setPictureFileType(pictureFileType);

            final String type = umlDiagramType.name();
            diagramDescriptor.setType(type+"DIAGRAM");

            diagramDescriptor.setTitle(extractString(sequenceDiagram.getTitle()));
            diagramDescriptor.setCaption(extractString(sequenceDiagram.getCaption()));
            diagramDescriptor.setLegend(extractString(sequenceDiagram.getLegend()));

            sequenceDiagram.participants().forEach(this::addParticipant);
            sequenceDiagram.events().forEach(this::addEvent);

            for (PlantUmlParticipantDescriptor participant : participantDescriptors.values()) {
                diagramDescriptor.getPlantUmlParticipants().add(participant);
            }

            plantUmlFileDescriptor.getPlantUmlDiagrams().add(diagramDescriptor);
            setOldRelationsForCompatibility(diagramDescriptor);
        }
        else if(diagram instanceof AbstractEntityDiagram) {
            final AbstractEntityDiagram descriptionDiagram = (AbstractEntityDiagram) diagram;
            final UmlDiagramType umlDiagramType = descriptionDiagram.getUmlDiagramType();
            final PlantUmlDiagramDescriptor diagramDescriptor = createDiagramDescriptor(umlDiagramType);
            if(diagramDescriptor == null) return;

            diagramDescriptor.setPictureFileName(pictureFileName);
            diagramDescriptor.setPictureFileType(pictureFileType);

            final String type = umlDiagramType.name();
            diagramDescriptor.setType(type+"DIAGRAM");

            final String namespaceSeparator = descriptionDiagram.getNamespaceSeparator();
            diagramDescriptor.setNamespaceSeparator(namespaceSeparator);

            diagramDescriptor.setTitle(extractString(descriptionDiagram.getTitle()));
            diagramDescriptor.setCaption(extractString(descriptionDiagram.getCaption()));
            diagramDescriptor.setLegend(extractString(descriptionDiagram.getLegend()));

            final Collection<IGroup> groups = descriptionDiagram.getRootGroup().getChildren();
            addGroups(diagramDescriptor, groups, diagramDescriptor);

            final Collection<ILeaf> leafsvalues = descriptionDiagram.getRootGroup().getLeafsDirect();
            addLeafs(leafsvalues, diagramDescriptor);

            final List<Link> links = descriptionDiagram.getLinks();
            addLinks(links);

            plantUmlFileDescriptor.getPlantUmlDiagrams().add(diagramDescriptor);
            setOldRelationsForCompatibility(diagramDescriptor);
        }
    }

    private String extractString(final DisplayPositionned displayPositionned) {
        StringBuilder title = new StringBuilder();
        if (displayPositionned != null && displayPositionned.getDisplay() != null && !displayPositionned.isNull()) {
            displayPositionned.getDisplay().iterator().forEachRemaining(title::append);
        }
        return title.toString();
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
            final LinkType type = link.getType();
            final LinkStyle style = type.getStyle();
            // there are invisible links between otherwise unconnected
            // entities; dont import these internal links which are
            // not declared in the diagram
            if (style.toString().toLowerCase().contains("invis")) {
                continue;
            }

            String lhs = link.getEntity1().getCode().getName();
            String rhs = link.getEntity2().getCode().getName();
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
            final String fullName = iLeaf.getCode().getName();
            leafNode.setFullName(fullName);
            final LeafType entityType = iLeaf.getLeafType();
            leafNode.setType(entityType.name());
            mappingFromFqnToPackage.put(fullName, leafNode);
            final Stereotype stereotype = iLeaf.getStereotype();
            if(stereotype != null) {
                leafNode.setStereotype(stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
            }
            leafNode.setDescription(iteratorToText(iLeaf.getDisplay().iterator()));
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
                final String fullGroupName = iGroup.getCode().getName();
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

    private void addParticipant(final Participant participant) {
        final PlantUmlParticipantDescriptor plantUmlParticipantDescriptor = store.create(PlantUmlParticipantDescriptor.class);

        final ParticipantType type = participant.getType();
        plantUmlParticipantDescriptor.setType(type.name());
        plantUmlParticipantDescriptor.setName(participant.getCode());

        final Stereotype stereotype = participant.getStereotype();
        if(stereotype != null) {
            plantUmlParticipantDescriptor.setStereotype(stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));
        }

        participantDescriptors.put(participant.getCode(), plantUmlParticipantDescriptor);
    }

    private String iteratorToText(Iterator<CharSequence> it) {
        StringBuilder builder = new StringBuilder();
        it.forEachRemaining(line -> builder.append(line).append("\n"));
        return builder.toString();
    }
}
