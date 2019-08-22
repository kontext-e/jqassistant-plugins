package de.kontext_e.jqassistant.plugin.ruby.store.descriptor;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("Module")
public interface ModuleDescriptor extends RubyDescriptor, NamedDescriptor {

    @Relation("CONTAINS")
    Set<ClassDescriptor> getClasses();

    @Relation("CONTAINS")
    Set<ModuleDescriptor> getModules();

}
