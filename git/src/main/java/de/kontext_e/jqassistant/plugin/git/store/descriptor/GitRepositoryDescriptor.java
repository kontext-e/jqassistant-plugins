package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import java.util.List;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Repository")
public interface GitRepositoryDescriptor extends Descriptor, NamedDescriptor, FileDescriptor {

    @Relation("HAS_COMMIT")
    List<GitCommitDescriptor> getCommits();

    @Relation("HAS_AUTHOR")
    List<GitAuthorDescriptor> getAuthors();

    @Relation("HAS_FILE")
    List<GitFileDescriptor> getFiles();

    @Relation("HAS_BRANCH")
    List<GitBranchDescriptor> getBranches();

    @Relation("HAS_TAG")
    List<GitTagDescriptor> getTags();

    @Relation("HAS_HEAD")
    GitCommitDescriptor getHead();

    void setHead(GitCommitDescriptor headDescriptor);
}
