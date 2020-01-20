package de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

@Label("Section")
public interface AsciidocSectionDescriptor extends AsciidocBlockDescriptor {

    @Property("index")
    Integer getIndex();
    void setIndex(Integer index);

    @Property("number")
    Integer getNumber();
    void setNumber(Integer number);

    @Property("numbered")
    boolean getNumbered();
    void setNumbered(boolean numbered);

    @Property("sectname")
    String getSectname();
    void setSectname(String sectname);

    @Property("special")
    Boolean getSpecial();
    void setSpecial(Boolean special);

}
