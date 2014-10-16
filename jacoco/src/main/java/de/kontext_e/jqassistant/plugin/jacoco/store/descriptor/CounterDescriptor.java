package de.kontext_e.jqassistant.plugin.jacoco.store.descriptor;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("JACOCO_COUNTER")
public interface CounterDescriptor extends Descriptor {

    @Property("TYPE")
    String getType();
    void setType(String value);

    @Property("MISSED")
    Long getMissed();
    void setMissed(Long value);

    @Property("COVERED")
    Long getCovered();
    void setCovered(Long value);
}
