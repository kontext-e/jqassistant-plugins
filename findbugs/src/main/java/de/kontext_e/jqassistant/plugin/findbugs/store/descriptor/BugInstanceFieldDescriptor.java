package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.core.store.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("BugInstanceField")
public interface BugInstanceFieldDescriptor extends Descriptor, NamedDescriptor, FullQualifiedNameDescriptor, SourceLineContainingDescriptor {

    @Property("signature")
    String getSignature();
    void setSignature(String signature);

    @Property("isStatic")
    Boolean getIsStatic();
    void setIsStatic(Boolean signature);
}
