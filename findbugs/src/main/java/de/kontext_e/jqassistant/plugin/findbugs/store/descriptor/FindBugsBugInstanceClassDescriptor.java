package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("BugInstanceClass")
public interface FindBugsBugInstanceClassDescriptor extends FindBugsDescriptor, FullQualifiedNameDescriptor, FindBugsSourceLineContainingDescriptor {

}
