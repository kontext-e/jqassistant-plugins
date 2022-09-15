package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("List")
public interface AsciidocListDescriptor extends AsciidocBlockDescriptor {
    @Property("isItem")
    Boolean getIsItem();
    void setIsItem(Boolean isItem);

    @Relation("HAS_ITEM")
    Set<AsciidocBlockDescriptor> getListItems();

}
