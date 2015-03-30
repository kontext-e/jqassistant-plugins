package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import java.util.List;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.NamedDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Git")
public interface GitDescriptor extends Descriptor, NamedDescriptor, FileDescriptor {

    @Relation("HAS_COMMITS")
    List<GitCommitDescriptor> getCommits();

    @Relation("HAS_AUTHORS")
    List<GitAuthorDescriptor> getAuthors();

    @Relation("HAS_FILES")
    List<GitFileDescriptor> getFiles();

}
