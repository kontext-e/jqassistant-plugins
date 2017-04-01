package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Section")
public interface AsciidocSectionDescriptor extends AsciidocBlockDescriptor {

    @Property("index")
    void setIndex(Integer index);
    Integer getIndex();

    @Property("number")
    void setNumber(Integer number);
    Integer getNumber();

    @Property("numbered")
    void setNumbered(Integer numbered);
    Integer getNumbered();

    @Property("sectname")
    void setSectname(String sectname);
    String getSectname();

    @Property("special")
    void setSpecial(Boolean special);
    Boolean getSpecial();

}
