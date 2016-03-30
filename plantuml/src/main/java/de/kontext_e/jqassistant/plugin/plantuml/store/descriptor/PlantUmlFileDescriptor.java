package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("PlantUml")
public interface PlantUmlFileDescriptor extends Descriptor, NamedDescriptor, FileDescriptor {

    @Relation("CONTAINS")
    Set<PlantUmlElement> getPlantUmlElements();
}
