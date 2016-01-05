package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Change")
public interface GitChangeDescriptor extends GitDescriptor {

    @Property("modificationKind")
    String getModificationKind();
    void setModificationKind(String modificationKind);

    @Relation("MODIFIES")
    GitFileDescriptor getModifies();
    void setModifies(GitFileDescriptor gitFileDescriptor);
}
