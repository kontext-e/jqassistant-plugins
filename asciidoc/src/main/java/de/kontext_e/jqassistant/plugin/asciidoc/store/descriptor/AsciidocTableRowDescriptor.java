package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.Set;

@Label("Row")
public interface AsciidocTableRowDescriptor extends AsciidocDescriptor {

    @Relation("CONTAINS_CELL")
    Set<AsciidocTableCellDescriptor> getAsciidocTableCells();

	@Property("rownumber")
	Integer getRownumber();
	void setRownumber(Integer rownumber);

}
