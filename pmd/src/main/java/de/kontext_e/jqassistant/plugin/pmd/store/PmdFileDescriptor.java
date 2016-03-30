package de.kontext_e.jqassistant.plugin.pmd.store;

import java.util.Set;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author aw, Kontext E GmbH, 29.01.15
 */
@Label("PmdFile")
public interface PmdFileDescriptor extends Descriptor, NamedDescriptor, FullQualifiedNameDescriptor {

    @Relation("PMD_VIOLATIONS")
    Set<PmdViolationDescriptor> getViolations();
}
