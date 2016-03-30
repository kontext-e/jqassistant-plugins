package de.kontext_e.jqassistant.plugin.jacoco.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Jacoco")
@Abstract
public interface JacocoDescriptor extends Descriptor {
}
