package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Leaf")
public interface PlantUmlLeafDescriptor extends PlantUmlDescriptor, PlantUmlElement {

    @Property("type")
    void setType(String name);
    String getType();

    @Property("stereotype")
    void setStereotype(String stereotype);
    String getStereotype();

    @Property("description")
    void setDescription(String description);
    String getDescription();
}
