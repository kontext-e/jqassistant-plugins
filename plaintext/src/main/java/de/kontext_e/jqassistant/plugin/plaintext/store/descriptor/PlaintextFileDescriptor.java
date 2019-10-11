package de.kontext_e.jqassistant.plugin.plaintext.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;

public interface PlaintextFileDescriptor extends PlaintextDescriptor, NamedDescriptor, FileDescriptor {

    @Relation("HAS_LINE")
    Set<PlaintextLineDescriptor> getLines();
}
