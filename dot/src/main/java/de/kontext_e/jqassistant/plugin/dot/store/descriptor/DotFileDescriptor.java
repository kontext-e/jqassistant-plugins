package de.kontext_e.jqassistant.plugin.dot.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("File")
public interface DotFileDescriptor extends DotDescriptor, NamedDescriptor, FullQualifiedNameDescriptor, HasGraph {


}
