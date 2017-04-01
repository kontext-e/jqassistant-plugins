package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Asciidoc")
@Abstract
public interface AsciidocDescriptor extends Descriptor {
}
