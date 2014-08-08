package de.kontext_e.jqassistant.plugin.jacoco.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.core.store.api.type.Descriptor;
import com.buschmais.jqassistant.core.store.api.type.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("JACOCO_METHOD")
public interface MethodDescriptor extends Descriptor, NamedDescriptor {

    @Property("SIGNATURE")
    String getSignature();
    void setSignature(String value);

    @Property("LINE")
    String getLine();
    void setLine(String value);

    @Relation("JACOCO_COUNTERS")
    Set<CounterDescriptor> getJacocoCounters();
}
