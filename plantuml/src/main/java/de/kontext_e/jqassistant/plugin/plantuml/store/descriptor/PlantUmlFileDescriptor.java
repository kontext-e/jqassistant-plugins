package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;

public interface PlantUmlFileDescriptor extends PlantUmlDescriptor, NamedDescriptor, FileDescriptor {

    @Relation("CONTAINS")
    Set<PlantUmlElement> getPlantUmlElements();

    @Relation("CONTAINS_DIAGRAM")
    Set<PlantUmlDiagramDescriptor> getPlantUmlDiagrams();
}
