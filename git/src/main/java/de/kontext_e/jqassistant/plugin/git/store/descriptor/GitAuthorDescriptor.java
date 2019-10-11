package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Indexed;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Author")
public interface GitAuthorDescriptor extends GitPersonDescriptor {

    // cannot be part of superinterface because of @Indexed
    @Indexed
    @Property("identString")
    String getIdentString();
    void setIdentString(String identString);
}
