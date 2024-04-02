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

        final Collection<Quark<Entity>> groups = descriptionDiagram.getRootGroup().getQuark().getPlasma().quarks();
        addGroups(diagramDescriptor, groups, diagramDescriptor);

        final Collection<Entity> leafsvalues = descriptionDiagram.getRootGroup().leafs();
        addLeafs(leafsvalues, diagramDescriptor);

        final List<Link> links = descriptionDiagram.getLinks();
        addLinks(links);
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
        for (final Entity leaf : leafsvalues) {
            PlantUmlLeafDescriptor leafNode = store.create(PlantUmlLeafDescriptor.class);
            mappingFromFqnToPackage.put(leaf.getName(), leafNode);
            describeLeaf(leaf, leafNode);
            if(plantUmlGroupDescriptor != null) {
                plantUmlGroupDescriptor.getLeafs().add(leafNode);
            }
        }
    }

    private void describeLeaf(Entity iLeaf, PlantUmlLeafDescriptor leafNode) {
        leafNode.setFullName(iLeaf.getName());
        leafNode.setType(iLeaf.getLeafType().name());
        leafNode.setDescription(iteratorToText(iLeaf.getDisplay().iterator()));
        final Stereotype stereotype = iLeaf.getStereotype();
        if(stereotype != null) {
            leafNode.setStereotype(stereotype.getLabel(Guillemet.GUILLEMET));
        }
    }

    private void addGroups(final PlantUmlDiagramDescriptor diagramDescriptor, final Collection<Quark<Entity>> groups, PlantUmlGroupDescriptor parent) {
        for (Quark<Entity> iGroup : groups) {
            if (!iGroup.getData().isGroup()) continue;
            final GroupType groupType = iGroup.getData().getGroupType();
            Optional<PlantUmlGroupDescriptor> plantUmlGroupDescriptor = createGroupDescriptor(groupType);

            if(plantUmlGroupDescriptor.isPresent()) {
                describeGroup(diagramDescriptor, parent, iGroup, plantUmlGroupDescriptor.get());
                addGroups(diagramDescriptor, iGroup.getChildren(), plantUmlGroupDescriptor.get());
                addLeafs(iGroup.getData().leafs(), plantUmlGroupDescriptor.get());
            } else {
                LOGGER.warn("Not handled group type: "+groupType);
            }
        }
    }

    private void describeGroup(PlantUmlDiagramDescriptor diagramDescriptor, PlantUmlGroupDescriptor parent, Quark<Entity> iGroup, PlantUmlGroupDescriptor groupDescriptor) {
        groupDescriptor.setFullName(iGroup.getName());
        diagramDescriptor.getPlantUmlGroups().add(groupDescriptor);
        mappingFromFqnToPackage.put(iGroup.getName(), groupDescriptor);
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
