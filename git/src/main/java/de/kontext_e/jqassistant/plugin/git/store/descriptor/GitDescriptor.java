package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Git")
@Abstract
public interface GitDescriptor extends Descriptor {
}
