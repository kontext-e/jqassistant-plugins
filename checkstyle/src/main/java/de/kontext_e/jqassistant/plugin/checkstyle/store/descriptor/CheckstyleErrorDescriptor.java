package de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor;


import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("Error")
public interface CheckstyleErrorDescriptor extends CheckstyleDescriptor {

    @Property("line")
    String getLine();
    void setLine(String value);

    @Property("column")
    String getColumn();
    void setColumn(String value);

    @Property("severity")
    String getSeverity();
    void setSeverity(String value);

    @Property("message")
    String getMessage();
    void setMessage(String value);

    @Property("source")
    String getSource();
    void setSource(String value);
}
