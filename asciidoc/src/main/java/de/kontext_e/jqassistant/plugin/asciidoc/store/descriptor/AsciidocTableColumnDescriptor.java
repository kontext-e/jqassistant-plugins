package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Column")
public interface AsciidocTableColumnDescriptor extends AsciidocDescriptor, AsciidocCommonProperties {

    @Property("colnumber")
    Integer getColnumber();
    void setColnumber(Integer colnumber);
}
