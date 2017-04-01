package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import java.util.Set;

import com.buschmais.xo.neo4j.api.annotation.Relation;

public interface BlockContainer {

    @Relation("CONTAINS")
    Set<AsciidocBlockDescriptor> getAsciidocBlocks();

}
