package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;


import java.util.Set;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Diagram")
public interface PlantUmlDiagramDescriptor extends PlantUmlDescriptor {
    @Property("type")
    void setType(String type);
    String getType();

    @Property("namespaceSeparator")
    void setNamespaceSeparator(String namespaceSeparator);
    String getNamespaceSeparator();

    @Relation("CONTAINS_GROUP")
    Set<PlantUmlGroupDescriptor> getPlantUmlGroups();
}
