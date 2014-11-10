package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FileDescriptor;
import com.buschmais.jqassistant.core.store.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("PlantUmlSequenceDiagram")
public interface PlantUmlSequenceDiagramDescriptor extends Descriptor, NamedDescriptor, FileDescriptor {
}
