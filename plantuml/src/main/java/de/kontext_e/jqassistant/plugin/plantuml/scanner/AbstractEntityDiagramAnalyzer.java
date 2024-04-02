package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.*;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
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

        final Collection<Quark<Entity>> groups = descriptionDiagram.getRootGroup().getQuark().getChildren();
        analyzeEntities(diagramDescriptor, groups, diagramDescriptor);

        final List<Link> links = descriptionDiagram.getLinks();
        addLinks(links);
    }

    private void addLinks(final List<Link> links) {
        for (Link link : links) {
            final LinkType type = link.getType();
            final LinkStyle style = type.getStyle();
            // there are invisible links between otherwise unconnected
            // entities; don't import these internal links which are
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

    private void analyzeleaf(final Entity leaf, final PlantUmlGroupDescriptor plantUmlGroupDescriptor) {
        PlantUmlLeafDescriptor leafNode = store.create(PlantUmlLeafDescriptor.class);
        mappingFromFqnToPackage.put(leaf.getName(), leafNode);
        describeLeaf(leaf, leafNode);
        if(plantUmlGroupDescriptor != null) {
            plantUmlGroupDescriptor.getLeafs().add(leafNode);
        }
    }

    private void describeLeaf(Entity leaf, PlantUmlLeafDescriptor leafDescriptor) {
        leafDescriptor.setFullName(leaf.getQuark().getQualifiedName());
        leafDescriptor.setType(leaf.getLeafType().name());
        leafDescriptor.setDescription(iteratorToText(leaf.getDisplay().iterator()));
        final Stereotype stereotype = leaf.getStereotype();
        if(stereotype != null) {
            leafDescriptor.setStereotype(stereotype.getLabel(Guillemet.GUILLEMET));
        }
    }

    private void analyzeEntities(final PlantUmlDiagramDescriptor diagramDescriptor, final Collection<Quark<Entity>> entities, PlantUmlGroupDescriptor parent) {
        for (Quark<Entity> entity : entities) {
            if (entity.getData() == null){
                analyzeEntities(diagramDescriptor, entity.getChildren(), parent);
            } else if (entity.getData().isGroup()){
                anazalyzeGroup(diagramDescriptor, parent, entity);
            } else {
                analyzeleaf(entity.getData(), diagramDescriptor);
            }
        }
    }

    private void anazalyzeGroup(PlantUmlDiagramDescriptor diagramDescriptor, PlantUmlGroupDescriptor parent, Quark<Entity> iGroup) {
        Optional<PlantUmlGroupDescriptor> plantUmlGroupDescriptor = createGroupDescriptor(iGroup.getData().getGroupType());

        if(plantUmlGroupDescriptor.isPresent()) {
            describeGroup(diagramDescriptor, parent, iGroup, plantUmlGroupDescriptor.get());
            analyzeEntities(diagramDescriptor, iGroup.getChildren(), plantUmlGroupDescriptor.get());
        } else {
            LOGGER.warn("Not handled group type: "+ iGroup.getData().getGroupType());
        }
    }

    private void describeGroup(PlantUmlDiagramDescriptor diagramDescriptor, PlantUmlGroupDescriptor parent, Quark<Entity> group, PlantUmlGroupDescriptor groupDescriptor) {
        groupDescriptor.setFullName(group.getQualifiedName());
        diagramDescriptor.getPlantUmlGroups().add(groupDescriptor);
        mappingFromFqnToPackage.put(group.getName(), groupDescriptor);
        if(parent != null) {
            parent.getChildGroups().add(groupDescriptor);
        }
    }

    private Optional<PlantUmlGroupDescriptor> createGroupDescriptor(GroupType groupType) {
        switch (groupType) {
            case PACKAGE:
                return Optional.of(store.create(PlantUmlPackageDescriptor.class));
            case STATE:
                return Optional.of(store.create(PlantUmlStateDescriptor.class));
            default: return Optional.empty();
        }
    }

    private String iteratorToText(Iterator<CharSequence> it) {
        StringBuilder builder = new StringBuilder();
        it.forEachRemaining(line -> builder.append(line).append("\n"));
        return builder.toString();
    }
}
