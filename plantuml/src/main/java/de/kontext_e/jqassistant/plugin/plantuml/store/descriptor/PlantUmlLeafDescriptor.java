package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Leaf")
public interface PlantUmlLeafDescriptor extends PlantUmlDescriptor, PlantUmlElement {

    @Property("type")
    String getType();
    void setType(String name);

    @Property("stereotype")
    String getStereotype();
    void setStereotype(String stereotype);

    @Property("description")
    String getDescription();
    void setDescription(String description);
}
