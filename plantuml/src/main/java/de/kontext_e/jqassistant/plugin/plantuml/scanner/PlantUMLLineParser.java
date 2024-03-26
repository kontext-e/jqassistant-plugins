package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.*;
import net.sourceforge.plantuml.*;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.cucadiagram.*;
import net.sourceforge.plantuml.sequencediagram.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class PlantUMLLineParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantUMLLineParser.class);

    private final Store store;
    private final Map<String, PlantUmlElement> mappingFromFqnToPackage = new HashMap<>();
    private final PlantUmlFileDescriptor plantUmlFileDescriptor;
    private ParsingState parsingState;
    private StringBuilder lineBuffer = new StringBuilder();
    private final Map<String, PlantUmlParticipantDescriptor> participantDescriptors = new HashMap<>();
    private String pictureFileName;
    private String pictureFileType;

    PlantUMLLineParser(final Store store, final PlantUmlFileDescriptor plantUmlFileDescriptor, final ParsingState parsingState) {
        this.store = store;
        this.plantUmlFileDescriptor = plantUmlFileDescriptor;
        this.parsingState = parsingState;
    }

    void parseLine(final String line) {
        if(line == null) return;

        String normalizedLine = line.trim().toLowerCase();

        // inlcudes cannot be handled because of working directory mismatch while scanning
        if(normalizedLine.startsWith("!include")) { return; }

        if(parsingState == ParsingState.IGNORING && normalizedLine.startsWith("[\"plantuml\"")) {
            parsingState = ParsingState.PLANTUMLFOUND;
            analyzeHeader(normalizedLine);
        }

        if(parsingState == ParsingState.ACCEPTING && (normalizedLine.startsWith("----") || normalizedLine.startsWith("@enduml"))) {
            parsingState = ParsingState.IGNORING;
            lineBuffer.append("\n").append("@enduml");
            storeDiagram();
            prepareScannerForNextDiagram();
        }

        if(parsingState == ParsingState.PLANTUMLFOUND && normalizedLine.startsWith("----")) {
            parsingState = ParsingState.ACCEPTING;
            startNewLineBuffer();
            return;
        }

        if(parsingState == ParsingState.ACCEPTING) {
            lineBuffer.append(normalizedLine).append("\n");
        }
    }

    private void startNewLineBuffer() {
        lineBuffer = new StringBuilder();
        lineBuffer.append("@startuml");
        lineBuffer.append("\n");
    }

    private void prepareScannerForNextDiagram() {
        pictureFileName = null;
        pictureFileType = null;
    }

    private void analyzeHeader(String normalizedLine) {
        final String[] parts = normalizedLine
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .replaceAll("\"", "")
                .replaceAll("\n", "")
                .split(",");
        if(parts.length >= 3) {
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
            LOGGER.warn("Error while parsing at position: "+error.getErrorsUml());
            return;
        }


        if(diagram instanceof SequenceDiagram) {
            final PlantUmlDiagramDescriptor diagramDescriptor = analyzeSequenceDiagram((SequenceDiagram) diagram);
            if (diagramDescriptor == null) return;

            plantUmlFileDescriptor.getPlantUmlDiagrams().add(diagramDescriptor);
            setOldRelationsForCompatibility(diagramDescriptor);
        }
        else if(diagram instanceof AbstractEntityDiagram) {
            final PlantUmlDiagramDescriptor diagramDescriptor = analyzeAbstractentityDiagram((AbstractEntityDiagram) diagram);
            if (diagramDescriptor == null) return;

            plantUmlFileDescriptor.getPlantUmlDiagrams().add(diagramDescriptor);
            setOldRelationsForCompatibility(diagramDescriptor);
        }
    }

    private PlantUmlDiagramDescriptor analyzeSequenceDiagram(SequenceDiagram diagram) {
        final UmlDiagramType umlDiagramType = diagram.getUmlDiagramType();
        final PlantUmlDiagramDescriptor diagramDescriptor = createDiagramDescriptor(umlDiagramType);
        if(diagramDescriptor == null) return null;

        analyzeDiagramMetadata(diagramDescriptor, umlDiagramType, diagram);
        analyzeSequenceDiagramContent(diagram);
        return diagramDescriptor;
    }

    private PlantUmlDiagramDescriptor analyzeAbstractentityDiagram(AbstractEntityDiagram diagram) {
        final UmlDiagramType umlDiagramType = diagram.getUmlDiagramType();
        final PlantUmlDiagramDescriptor diagramDescriptor = createDiagramDescriptor(umlDiagramType);
        if(diagramDescriptor == null) return null;

        analyzeDiagramMetadata(diagramDescriptor, umlDiagramType, diagram);
        analyzeAbstractEntityDiagramContent(diagram, diagramDescriptor);
        return diagramDescriptor;
    }

    private void analyzeAbstractEntityDiagramContent(AbstractEntityDiagram descriptionDiagram, PlantUmlDiagramDescriptor diagramDescriptor) {
        final String namespaceSeparator = descriptionDiagram.getNamespaceSeparator();
        diagramDescriptor.setNamespaceSeparator(namespaceSeparator);

        final Collection<IGroup> groups = descriptionDiagram.getRootGroup().getChildren();
        addGroups(diagramDescriptor, groups, diagramDescriptor);

        final Collection<ILeaf> leafsvalues = descriptionDiagram.getRootGroup().getLeafsDirect();
        addLeafs(leafsvalues, diagramDescriptor);

        final List<Link> links = descriptionDiagram.getLinks();
        addLinks(links);
    }

    private void analyzeSequenceDiagramContent(SequenceDiagram sequenceDiagram) {
        sequenceDiagram.participants().forEach((s, p) -> addParticipant(p));
        sequenceDiagram.events().forEach(this::addEvent);
    }

    private void analyzeDiagramMetadata(PlantUmlDiagramDescriptor diagramDescriptor, UmlDiagramType umlDiagramType, UmlDiagram descriptionDiagram) {
        diagramDescriptor.setPictureFileName(pictureFileName);
        diagramDescriptor.setPictureFileType(pictureFileType);

        final String type = umlDiagramType.name();
        diagramDescriptor.setType(type+"DIAGRAM");

        if (!(descriptionDiagram.getTitle().getDisplay().size() == 0)){
            String title = String.join(" ", descriptionDiagram.getTitle().getDisplay());
            diagramDescriptor.setTitle(title);
        }
        if (!(descriptionDiagram.getCaption().getDisplay().size() == 0)){
            String caption = String.join(" ", descriptionDiagram.getCaption().getDisplay());
            diagramDescriptor.setCaption(caption);
        }
        if (!(descriptionDiagram.getLegend().getDisplay().size() == 0)){
            String legend = String.join("", descriptionDiagram.getLegend().getDisplay());
            diagramDescriptor.setLegend(legend);
        }
    }

    private PlantUmlDiagramDescriptor createDiagramDescriptor(final UmlDiagramType umlDiagramType) {
        switch (umlDiagramType) {
            case CLASS:
                return store.create(PlantUmlClassDiagramDescriptor.class);
            case DESCRIPTION:
                return store.create(PlantUmlDescriptionDiagramDescriptor.class);
            case SEQUENCE:
                return store.create(PlantUmlSequenceDiagramDescriptor.class);
            case STATE:
                return store.create(PlantUmlStateDiagramDescriptor.class);
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
            mappingFromFqnToPackage.put(fullName, leafNode);
            final Stereotype stereotype = iLeaf.getStereotype();
            if(stereotype != null) {
                leafNode.setStereotype(stereotype.getLabel(true));
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
            plantUmlParticipantDescriptor.setStereotype(stereotype.getLabel(true));
        }

        participantDescriptors.put(participant.getCode(), plantUmlParticipantDescriptor);
    }

    private String iteratorToText(Iterator<CharSequence> it) {
        StringBuilder builder = new StringBuilder();
        it.forEachRemaining(line -> builder.append(line).append("\n"));
        return builder.toString();
    }
}
