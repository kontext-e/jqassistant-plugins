package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Cell")
public interface AsciidocTableCellDescriptor extends AsciidocDescriptor, AsciidocCommonProperties {

	@Relation("OF_COLUMN")
	AsciidocTableColumnDescriptor getColumn();
	void setColumn(AsciidocTableColumnDescriptor column);

    @Property("text")
    String getText();
    void setText(String text);

    @Property("colnumber")
    Integer getColnumber();
    void setColnumber(Integer colnumber);
}
