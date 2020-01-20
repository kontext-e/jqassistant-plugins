package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;


import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("Diagram")
public interface PlantUmlDiagramDescriptor extends PlantUmlDescriptor, PlantUmlGroupDescriptor {
    @Property("type")
    String getType();
    void setType(String type);

    @Property("namespaceSeparator")
    String getNamespaceSeparator();
    void setNamespaceSeparator(String namespaceSeparator);

    @Relation("CONTAINS_GROUP")
    Set<PlantUmlGroupDescriptor> getPlantUmlGroups();

    @Property("pictureFileName")
    String getPictureFileName();
    void setPictureFileName(String pictureFileName);

    @Property("pictureFileType")
    String getPictureFileType();
    void setPictureFileType(String pictureFileType);

    @Property("title")
    String getTitle();
    void setTitle(String title);

    @Property("caption")
    String getCaption();
    void setCaption(String caption);

    @Property("legend")
    String getLegend();
    void setLegend(String legend);
}
