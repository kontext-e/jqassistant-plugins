package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;


import com.buschmais.jqassistant.core.store.api.descriptor.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@Label("SOURCELINE")
public interface SourceLineDescriptor extends Descriptor {

    @Property("FQN")
    String getClassname();
    void setClassname(String value);

    @Property("START")
    String getStart();
    void setStart(String value);

    @Property("END")
    String getEnd();
    void setEnd(String value);

    @Property("SOURCEFILE")
    String getSourcefile();
    void setSourcefile(String value);

    @Property("SOURCEPATH")
    String getSourcepath();
    void setSourcepath(String value);
}
