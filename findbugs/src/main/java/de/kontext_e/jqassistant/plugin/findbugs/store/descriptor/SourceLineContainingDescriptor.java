package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Relation;

public interface SourceLineContainingDescriptor {

    @Relation("SOURCELINE")
    SourceLineDescriptor getSourceLineDescriptor();
    void setSourceLineDescriptor(SourceLineDescriptor sourceLineDescriptor);

}
