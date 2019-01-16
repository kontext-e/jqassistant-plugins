package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("DescriptionListEntry")
public interface AsciidocDescriptionListEntryDescriptor extends AsciidocBlockDescriptor {
    @Property("isItem")
    void setIsItem(Boolean isItem);
    Boolean getIsItem();

    @Relation("HAS_ITEM")
    Set<AsciidocBlockDescriptor> getListItems();

    void setDescription(AsciidocListItemDescriptor scanListItemBlock);
    AsciidocListItemDescriptor getDescription();
}
