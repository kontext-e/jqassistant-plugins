package de.kontext_e.jqassistant.plugin.spotbugs.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("BugInstanceMethod")
public interface FindBugsBugInstanceMethodDescriptor extends FindBugsDescriptor, NamedDescriptor, FullQualifiedNameDescriptor, FindBugsSourceLineContainingDescriptor {

    @Property("signature")
    String getSignature();
    void setSignature(String signature);

    @Property("isStatic")
    Boolean getIsStatic();
    void setIsStatic(Boolean signature);
}
