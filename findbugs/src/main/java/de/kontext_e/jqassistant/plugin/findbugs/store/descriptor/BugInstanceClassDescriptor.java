package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("BugInstanceClass")
public interface BugInstanceClassDescriptor extends Descriptor, FullQualifiedNameDescriptor, SourceLineContainingDescriptor {

}
