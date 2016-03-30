package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("PlantUml")
@Abstract
public interface PlantUmlDescriptor extends Descriptor {
}
