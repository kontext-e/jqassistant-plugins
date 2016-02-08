package de.kontext_e.jqassistant.plugin.cpp.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Cpp")
public interface CppDescriptor extends Descriptor, FileDescriptor {
}
