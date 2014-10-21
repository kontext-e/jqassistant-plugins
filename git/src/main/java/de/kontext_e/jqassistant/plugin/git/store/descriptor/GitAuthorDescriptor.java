package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import java.util.List;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("GitAuthor")
public interface GitAuthorDescriptor extends Descriptor {

    @Property("identString")
    String getIdentString();
    void setIdentString(String identString);

    @Property("name")
    String getName();
    void setName(String name);

    @Property("email")
    String getEmail();
    void setEmail(String email);

    @Relation("COMMITED")
    List<GitCommitDescriptor> getCommits();

}
