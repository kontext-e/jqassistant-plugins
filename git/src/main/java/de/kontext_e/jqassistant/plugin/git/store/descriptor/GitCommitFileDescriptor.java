package de.kontext_e.jqassistant.plugin.git.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("GitCommitFile")
public interface GitCommitFileDescriptor extends Descriptor {

    @Property("modificationKind")
    String getModificationKind();
    void setModificationKind(String modificationKind);

    @Property("relativePath")
    String getRelativePath();
    void setRelativePath(String relativePath);

}
