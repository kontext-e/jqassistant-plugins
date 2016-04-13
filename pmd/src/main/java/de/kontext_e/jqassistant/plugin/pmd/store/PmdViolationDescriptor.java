package de.kontext_e.jqassistant.plugin.pmd.store;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author aw, Kontext E GmbH, 29.01.15
 */
@Label("Violation")
public interface PmdViolationDescriptor extends PmdDescriptor {
    @Property("rule")
    String getRule();
    void setRule(String rule);

    @Property("priority")
    Byte getPriority();
    void setPriority(Byte priority);

    @Property("ruleset")
    String getRuleSet();
    void setRuleSet(String ruleset);

    @Property("method")
    String getMethod();
    void setMethod(String method);

}
