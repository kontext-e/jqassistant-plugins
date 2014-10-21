package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import java.util.List;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("GitCommit")
public interface GitCommitDescriptor extends Descriptor {

    @Property("sha")
    String getSha();
    void setSha(String sha);

    @Property("author")
    String getAuthor();
    void setAuthor(String author);

    @Property("date")
    String getDate();
    void setDate(String date);

    @Property("time")
    String getTime();
    void setTime(String time);

    @Property("epoch")
    Long getEpoch();
    void setEpoch(Long epoch);

    @Property("message")
    String getMessage();
    void setMessage(String message);

    @Relation("HAS_FILES")
    List<GitCommitFileDescriptor> getFiles();

}
