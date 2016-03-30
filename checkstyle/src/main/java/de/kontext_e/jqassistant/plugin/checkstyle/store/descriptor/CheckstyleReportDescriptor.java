package de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor;


import java.util.Set;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("Checkstyle")
public interface CheckstyleReportDescriptor extends Descriptor, FileDescriptor {

    @Relation("CHECKSTYLE_FILES")
    Set<CheckstyleFileDescriptor> getFiles();
}
