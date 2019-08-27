package de.kontext_e.jqassistant.plugin.ruby.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.FullQualifiedNameDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Class")
public interface ClassDescriptor extends RubyDescriptor, NamedDescriptor, FullQualifiedNameDescriptor,
        ConstantContainer, AttributedDescriptor, MethodContainer {

    @Relation("INCLUDES")
    List<ModuleDescriptor> getIncludes();

    @Relation("EXTENDS")
    ClassDescriptor getSuperClass();
    void setSuperClass(ClassDescriptor superClass);
}
