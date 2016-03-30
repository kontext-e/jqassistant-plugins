package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;


import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@Label("SourceLine")
public interface FindBugsSourceLineDescriptor extends Descriptor {

    @Property("fqn")
    String getClassname();
    void setClassname(String value);

    @Property("start")
    String getStart();
    void setStart(String value);

    @Property("end")
    String getEnd();
    void setEnd(String value);

    @Property("sourcefile")
    String getSourcefile();
    void setSourcefile(String value);

    @Property("sourcepath")
    String getSourcepath();
    void setSourcepath(String value);
}
