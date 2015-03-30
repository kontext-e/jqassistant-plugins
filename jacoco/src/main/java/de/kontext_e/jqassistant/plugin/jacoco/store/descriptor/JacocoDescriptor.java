package de.kontext_e.jqassistant.plugin.jacoco.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.NamedDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("Jacoco")
public interface JacocoDescriptor extends Descriptor, NamedDescriptor, FileDescriptor {
    @Relation("JACOCO_PACKAGES")
    Set<PackageDescriptor> getJacocoPackages();
}
