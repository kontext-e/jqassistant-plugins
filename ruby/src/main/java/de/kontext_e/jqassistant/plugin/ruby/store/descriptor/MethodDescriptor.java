package de.kontext_e.jqassistant.plugin.ruby.store.descriptor;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Method")
public interface MethodDescriptor extends RubyDescriptor, NamedDescriptor, SignatureDescriptor {

    @Relation("HAS")
    List<ParameterDescriptor> getParameters();

    @Relation("CALLS")
    List<MethodDescriptor> getCalledMethods();
}
