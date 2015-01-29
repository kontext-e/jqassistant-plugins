package de.kontext_e.jqassistant.plugin.pmd.store;

import java.util.Set;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.core.store.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;


@Label("PmdFile")
public interface FileDescriptor extends Descriptor, NamedDescriptor, FullQualifiedNameDescriptor {

    @Relation("PMD_VIOLATIONS")
    Set<ViolationDescriptor> getViolations();
}
