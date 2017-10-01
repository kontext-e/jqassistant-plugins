package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;

import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Abstract
@Label("SourceLineContainer")
public interface FindBugsSourceLineContainingDescriptor extends FindBugsDescriptor {

    @Relation("HAS_SOURCELINE")
    FindBugsSourceLineDescriptor getSourceLineDescriptor();
    void setSourceLineDescriptor(FindBugsSourceLineDescriptor sourceLineDescriptor);

}
