package de.kontext_e.jqassistant.plugin.jacoco.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("Report")
public interface JacocoReportDescriptor extends JacocoDescriptor, NamedDescriptor, FileDescriptor {
    @Relation("HAS_PACKAGE")
    Set<JacocoPackageDescriptor> getJacocoPackages();
}
