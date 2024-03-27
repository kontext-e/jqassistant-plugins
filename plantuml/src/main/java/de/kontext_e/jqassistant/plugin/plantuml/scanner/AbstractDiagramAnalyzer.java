package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.*;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDiagramAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDiagramAnalyzer.class);
    protected Store store;

    protected String pictureFileName;
    protected String pictureFileType;

    public PlantUmlDiagramDescriptor analyzeDiagram(UmlDiagram diagram){
        final UmlDiagramType umlDiagramType = diagram.getUmlDiagramType();
        final PlantUmlDiagramDescriptor diagramDescriptor = createDiagramDescriptor(umlDiagramType);
        if(diagramDescriptor == null) return null;

        analyzeDiagramMetadata(diagramDescriptor, umlDiagramType, diagram);
        analyzeDiagramContent(diagram, diagramDescriptor);
        return diagramDescriptor;
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

    protected abstract void analyzeDiagramContent(UmlDiagram diagram, PlantUmlDiagramDescriptor diagramDescriptor);


}
