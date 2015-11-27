package de.kontext_e.jqassistant.plugin.linecount.store.descriptor;


import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Linecount")
public interface LinecountDescriptor extends Descriptor, NamedDescriptor, FileDescriptor {

    @Property("linecount")
    int getLinecount();
    void setLinecount(int linecount);
}
