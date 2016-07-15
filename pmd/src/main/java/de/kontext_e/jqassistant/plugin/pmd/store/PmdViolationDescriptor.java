package de.kontext_e.jqassistant.plugin.pmd.store;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author aw, Kontext E GmbH, 29.01.15
 */
@Label("Violation")
public interface PmdViolationDescriptor extends PmdDescriptor {
    @Property("beginline")
    void setBeginLine(Short beginLine);

    @Property("endline")
    void setEndLine(Short endLine);

    @Property("begincolumn")
    void setBeginColumn(Byte beginColumn);

    @Property("endcolumn")
    void setEndColumn(Short endColumn);

    @Property("rule")
    void setRule(String rule);

	@Property("ruleset")
	void setRuleSet(String ruleset);

    @Property("package")
    void setPackage(String aPackage);

    @Property("class")
    void setClass(String clazz);

    @Property("method")
    void setMethod(String method);

    @Property("variable")
    void setVariable(String variable);

    @Property("externalInfoUrl")
    void setExternalInfoUrl(String externalInfoUrl);

	@Property("priority")
	void setPriority(Byte priority);

	@Property("message")
	void setMessage(String message);

}
