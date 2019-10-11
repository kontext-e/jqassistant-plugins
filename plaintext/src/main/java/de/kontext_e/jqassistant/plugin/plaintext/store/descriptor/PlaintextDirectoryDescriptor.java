package de.kontext_e.jqassistant.plugin.plaintext.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Directory")
public interface PlaintextDirectoryDescriptor extends PlaintextDescriptor, NamedDescriptor {

    @Relation("HAS_FILE")
    Set<PlaintextFileDescriptor> getFiles();

    @Relation("HAS_DIRECTORY")
    Set<PlaintextDirectoryDescriptor> getDirectories();

    PlaintextDirectoryDescriptor getParent();
    void setParent(PlaintextDirectoryDescriptor parent);
}
