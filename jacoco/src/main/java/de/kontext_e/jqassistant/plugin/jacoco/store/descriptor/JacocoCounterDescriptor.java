package de.kontext_e.jqassistant.plugin.jacoco.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("JacocoCounter")
public interface JacocoCounterDescriptor extends Descriptor {

    @Property("type")
    String getType();
    void setType(String value);

    @Property("missed")
    Long getMissed();
    void setMissed(Long value);

    @Property("covered")
    Long getCovered();
    void setCovered(Long value);
}
