package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Repository")
public interface GitRepositoryDescriptor extends GitDescriptor, NamedDescriptor, FileDescriptor {

    @Relation("HAS_COMMIT")
    List<GitCommitDescriptor> getCommits();

    @Relation("HAS_AUTHOR")
    List<GitAuthorDescriptor> getAuthors();

    @Relation("HAS_COMMITTER")
    List<GitCommitterDescriptor> getCommitters();

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
