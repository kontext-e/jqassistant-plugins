package de.kontext_e.jqassistant.plugin.ruby.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

public interface ConstantContainer {

    @Relation("HAS")
    List<ConstantDescriptor> getConstants();

}
