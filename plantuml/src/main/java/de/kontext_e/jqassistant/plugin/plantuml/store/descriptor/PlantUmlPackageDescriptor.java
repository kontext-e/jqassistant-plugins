package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("PlantUmlPackage")
public interface PlantUmlPackageDescriptor extends Descriptor, FullQualifiedNameDescriptor, PlantUmlElement {

    @Relation("MAY_DEPEND_ON")
    Set<PlantUmlPackageDescriptor> getMayDependOnPackages();
}
