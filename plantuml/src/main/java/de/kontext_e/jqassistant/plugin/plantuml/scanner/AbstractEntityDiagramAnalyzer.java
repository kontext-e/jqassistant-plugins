package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.*;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.decoration.LinkStyle;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.text.Guillemet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AbstractEntityDiagramAnalyzer extends AbstractDiagramAnalyzer{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityDiagramAnalyzer.class);
    private final Map<String, PlantUmlElement> mappingFromFqnToPackage = new HashMap<>();

    public AbstractEntityDiagramAnalyzer(Store store, String pictureFileName, String pictureFiletype){
        this.store = store;
        this.pictureFileName = pictureFileName;
        this.pictureFileType = pictureFiletype;
    }

    @Override
    protected void analyzeDiagramContent(UmlDiagram diagram, PlantUmlDiagramDescriptor diagramDescriptor) {
        AbstractEntityDiagram descriptionDiagram = (AbstractEntityDiagram) diagram;
        final String namespaceSeparator = descriptionDiagram.getNamespaceSeparator();
        diagramDescriptor.setNamespaceSeparator(namespaceSeparator);

        final Collection<Quark<Entity>> groups = getAllGroups(descriptionDiagram);
        addGroups(diagramDescriptor, groups, diagramDescriptor);

        final Collection<Entity> leafsvalues = descriptionDiagram.getRootGroup().leafs();
        addLeafs(leafsvalues, diagramDescriptor);

        final List<Link> links = descriptionDiagram.getLinks();
        addLinks(links);
    }

    private List<Quark<Entity>> getAllGroups(AbstractEntityDiagram diagram){
        List<Quark<Entity>> result = new ArrayList<>();
        findGroups(diagram.getRootGroup().getQuark(), result);
        return result;
    }

    private void findGroups(Quark<Entity> quark, List<Quark<Entity>> result) {
        if (quark.getChildren().isEmpty()) {
            result.add(quark);
            return;
        }

        for (Quark<Entity> child : quark.getChildren()){
            findGroups(child, result);
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

            String lhs = link.getEntity1().getName();
            String rhs = link.getEntity2().getName();
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

    private void addLeafs(final Collection<Entity> leafsvalues, final PlantUmlGroupDescriptor plantUmlGroupDescriptor) {
        for (final Entity iLeaf : leafsvalues) {
            PlantUmlLeafDescriptor leafNode = store.create(PlantUmlLeafDescriptor.class);
            final String fullName = iLeaf.getName();
            leafNode.setFullName(fullName);
            final LeafType entityType = iLeaf.getLeafType();
            leafNode.setType(entityType.name());
            mappingFromFqnToPackage.put(fullName, leafNode);
            final Stereotype stereotype = iLeaf.getStereotype();
            if(stereotype != null) {
                leafNode.setStereotype(stereotype.getLabel(Guillemet.GUILLEMET));
            }
            leafNode.setDescription(iteratorToText(iLeaf.getDisplay().iterator()));
            if(plantUmlGroupDescriptor != null) {
                plantUmlGroupDescriptor.getLeafs().add(leafNode);
            }
        }
    }

    private void addGroups(final PlantUmlDiagramDescriptor diagramDescriptor, final Collection<Quark<Entity>> groups, PlantUmlGroupDescriptor parent) {
        for (Quark<Entity> iGroup : groups) {
            final GroupType groupType = iGroup.getData().getGroupType();
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
                final String fullGroupName = iGroup.getName();
                plantUmlGroupDescriptor.setFullName(fullGroupName);
                diagramDescriptor.getPlantUmlGroups().add(plantUmlGroupDescriptor);
                mappingFromFqnToPackage.put(fullGroupName, plantUmlGroupDescriptor);
                if(parent != null) {
                    parent.getChildGroups().add(plantUmlGroupDescriptor);
                }

                addGroups(diagramDescriptor, iGroup.getChildren(), plantUmlGroupDescriptor);
                addLeafs(iGroup.getData().leafs(), plantUmlGroupDescriptor);
            } else {
                LOGGER.warn("Not handled group type: "+groupType);
            }
        }
    }

    private String iteratorToText(Iterator<CharSequence> it) {
        StringBuilder builder = new StringBuilder();
        it.forEachRemaining(line -> builder.append(line).append("\n"));
        return builder.toString();
    }
}
