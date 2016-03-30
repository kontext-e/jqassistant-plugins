package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Relation;

public interface FindBugsSourceLineContainingDescriptor {

    @Relation("SOURCELINE")
    FindBugsSourceLineDescriptor getSourceLineDescriptor();
    void setSourceLineDescriptor(FindBugsSourceLineDescriptor sourceLineDescriptor);

}
