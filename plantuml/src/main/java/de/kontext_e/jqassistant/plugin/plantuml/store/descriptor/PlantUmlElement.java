package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import java.util.Set;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Element")
public interface PlantUmlElement extends PlantUmlDescriptor {

    @Property("fullName")
    String getFullName();
    void setFullName(String fullName);

    @Relation("LINK_TO")
    Set<PlantUmlElement> getLinkTargets();
}
