package de.kontext_e.jqassistant.plugin.spotbugs.store.descriptor;


import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@Label("SourceLine")
public interface FindBugsSourceLineDescriptor extends FindBugsDescriptor {

    @Property("fqn")
    String getClassname();
    void setClassname(String value);

    @Property("start")
    String getStart();
    void setStart(String value);

    @Property("end")
    String getEnd();
    void setEnd(String value);

    @Property("sourceFile")
    String getSourcefile();
    void setSourcefile(String value);

    @Property("sourcePath")
    String getSourcepath();
    void setSourcepath(String value);
}
