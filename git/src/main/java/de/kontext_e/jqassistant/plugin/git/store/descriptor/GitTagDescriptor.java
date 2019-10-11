package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * A Git Branch (either remote or local).
 *
 * @author Gerd Aschemann - gerd@aschemann.net - @GerdAschemann
 * @since 1.1.0
 */
@Label("Tag")
public interface GitTagDescriptor extends GitDescriptor {
    @Property("label")
    String getLabel();
    void setLabel(String name);

    @Relation("ON_COMMIT")
    GitCommitDescriptor getCommit();
    void setCommit(GitCommitDescriptor commit);
}
