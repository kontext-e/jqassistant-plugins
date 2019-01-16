package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("DescriptionList")
public interface AsciidocDescriptionListDescriptor extends AsciidocBlockDescriptor {
    @Property("isItem")
    void setIsItem(Boolean isItem);
    Boolean getIsItem();

    @Relation("HAS_ITEM")
    Set<AsciidocBlockDescriptor> getListItems();

}
