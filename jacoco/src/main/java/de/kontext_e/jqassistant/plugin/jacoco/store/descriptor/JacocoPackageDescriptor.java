package de.kontext_e.jqassistant.plugin.jacoco.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("Package")
public interface JacocoPackageDescriptor extends JacocoDescriptor, NamedDescriptor {
    @Relation("HAS_CLASS")
    Set<JacocoClassDescriptor> getJacocoClasses();
}
