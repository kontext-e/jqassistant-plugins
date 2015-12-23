package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * A Git Branch (either remote or local).
 *
 * @author Gerd Aschemann - gerd@aschemann.net - @GerdAschemann
 * @since 1.1.0
 */
@Label("GitTag")
public interface GitTagDescriptor extends Descriptor {
    @Property("label")
    public String getLabel();
    public void setLabel(String name);

    @Relation("commit")
    public GitCommitDescriptor getCommit();
    public void setCommit(GitCommitDescriptor commit);
}
