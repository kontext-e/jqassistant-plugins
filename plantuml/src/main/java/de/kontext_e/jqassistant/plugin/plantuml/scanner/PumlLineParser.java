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

class PumlLineParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PumlLineParser.class);

    private final Store store;
    private final Map<String, PlantUmlElement> mappingFromFqnToPackage = new HashMap<>();
    private final PlantUmlFileDescriptor plantUmlFileDescriptor;
    private ParsingState parsingState = ParsingState.ACCEPTING;
    private StringBuilder lineBuffer = new StringBuilder();

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
        if(!(diagram instanceof AbstractEntityDiagram)) {
            return;
        }

        AbstractEntityDiagram descriptionDiagram = (AbstractEntityDiagram) diagram;
        final DiagramDescription description = descriptionDiagram.getDescription();
        final String type = description.getType();
        final String namespaceSeparator = descriptionDiagram.getNamespaceSeparator();

        PlantUmlDiagramDescriptor diagramDescriptor = null;
        final UmlDiagramType umlDiagramType = descriptionDiagram.getUmlDiagramType();
        switch (umlDiagramType) {
            case ACTIVITY: break;
            case CLASS:
                diagramDescriptor = store.create(PlantUmlClassDiagramDescriptor.class);
                break;
            case COMPOSITE: break;
            case DESCRIPTION:
                diagramDescriptor = store.create(PlantUmlDescriptionDiagramDescriptor.class);
                break;
            case FLOW: break;
            case OBJECT: break;
            case SEQUENCE: break;
            case STATE:
                diagramDescriptor = store.create(PlantUmlStateDiagramDescriptor.class);
                break;
            case TIMING: break;
            default: break;
        }

        if(diagramDescriptor != null) {
            diagramDescriptor.setType(type);
            diagramDescriptor.setNamespaceSeparator(namespaceSeparator);
            plantUmlFileDescriptor.getPlantUmlDiagrams().add(diagramDescriptor);
        } else {
            LOGGER.warn("Diagram type not yet supported: "+umlDiagramType);
            return;
        }

        final Collection<IGroup> groups = descriptionDiagram.getRootGroup().getChildren();
        addGroups(diagramDescriptor, groups, null);

        final Collection<ILeaf> leafsvalues = descriptionDiagram.getRootGroup().getLeafsDirect();
        addLeafs(leafsvalues, null);

        final List<Link> links = descriptionDiagram.getLinks();
        addLinks(links);

        setOldRelationsForCompatibility(diagramDescriptor);
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
}
