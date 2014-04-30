package de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor;


import java.util.Set;

import com.buschmais.jqassistant.core.store.api.descriptor.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("CHECKSTYLE")
public interface CheckstyleDescriptor extends Descriptor, com.buschmais.jqassistant.core.store.api.descriptor.FileDescriptor {

    @Relation("CHECKSTYLE_FILES")
    Set<FileDescriptor> getFiles();
}
