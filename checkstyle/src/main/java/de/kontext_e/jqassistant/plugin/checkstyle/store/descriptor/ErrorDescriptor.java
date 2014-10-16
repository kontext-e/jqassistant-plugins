package de.kontext_e.jqassistant.plugin.checkstyle.store.descriptor;


import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("CHECKSTYLE_ERROR")
public interface ErrorDescriptor extends Descriptor {

    @Property("LINE")
    String getLine();
    void setLine(String value);

    @Property("COLUMN")
    String getColumn();
    void setColumn(String value);

    @Property("SEVERITY")
    String getSeverity();
    void setSeverity(String value);

    @Property("MESSAGE")
    String getMessage();
    void setMessage(String value);

    @Property("SOURCE")
    String getSource();
    void setSource(String value);
}
