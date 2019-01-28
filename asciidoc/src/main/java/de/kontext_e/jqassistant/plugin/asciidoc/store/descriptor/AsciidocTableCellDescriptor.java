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
    void setText(String text);
    String getText();

    @Property("colnumber")
    void setColnumber(Integer colnumber);
    Integer getColnumber();

    @Property("rownumber")
    void setRownumber(Integer rownumber);
    Integer getRownumber();
}
