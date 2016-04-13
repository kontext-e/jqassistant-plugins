package de.kontext_e.jqassistant.plugin.pmd.store;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Pmd")
@Abstract
public interface PmdDescriptor extends Descriptor {
}
