package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Indexed;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Author")
public interface GitAuthorDescriptor extends GitDescriptor {

    @Indexed
    @Property("identString")
    String getIdentString();
    void setIdentString(String identString);

    @Property("name")
    String getName();
    void setName(String name);

    @Property("email")
    String getEmail();
    void setEmail(String email);

    @Relation("COMMITTED")
    List<GitCommitDescriptor> getCommits();

}
