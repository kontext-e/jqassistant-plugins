package de.kontext_e.jqassistant.plugin.jacoco.store.descriptor;

import java.util.Set;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 11.02.14
 */
@Label("JacocoMethod")
public interface JacocoMethodDescriptor extends Descriptor, NamedDescriptor {

    @Property("signature")
    String getSignature();
    void setSignature(String value);

    @Property("line")
    String getLine();
    void setLine(String value);

    @Relation("JACOCO_COUNTERS")
    Set<JacocoCounterDescriptor> getJacocoCounters();
}
