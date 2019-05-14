package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Indexed;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Person")
@Abstract
public interface GitPersonDescriptor extends GitDescriptor {

    @Property("name")
    String getName();
    void setName(String name);

    @Property("email")
    String getEmail();
    void setEmail(String email);

    @Relation("COMMITTED")
    List<GitCommitDescriptor> getCommits();
}
