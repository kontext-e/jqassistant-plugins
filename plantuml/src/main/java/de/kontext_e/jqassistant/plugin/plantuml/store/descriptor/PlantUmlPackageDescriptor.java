package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("PlantUmlPackage")
public interface PlantUmlPackageDescriptor extends Descriptor, FullQualifiedNameDescriptor {
}
