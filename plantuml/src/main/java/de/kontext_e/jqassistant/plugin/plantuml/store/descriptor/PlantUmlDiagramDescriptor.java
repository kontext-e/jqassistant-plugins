package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;


import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("Diagram")
public interface PlantUmlDiagramDescriptor extends PlantUmlDescriptor, PlantUmlGroupDescriptor {
    @Property("type")
    void setType(String type);
    String getType();

    @Property("namespaceSeparator")
    void setNamespaceSeparator(String namespaceSeparator);
    String getNamespaceSeparator();

    @Relation("CONTAINS_GROUP")
    Set<PlantUmlGroupDescriptor> getPlantUmlGroups();

    @Property("pictureFileName")
    void setPictureFileName(String pictureFileName);
    String getPictureFileName();

    @Property("pictureFileType")
    void setPictureFileType(String pictureFileType);
    String getPictureFileType();

    @Property("title")
    void setTitle(String title);
    String getTitle();


    @Property("caption")
    void setCaption(String caption);
    String getCaption();

    @Property("legend")
    void setLegend(String legend);
    String getLegend();
}
