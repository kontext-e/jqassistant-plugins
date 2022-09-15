package de.kontext_e.jqassistant.plugin.pmd.store;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author aw, Kontext E GmbH, 29.01.15
 */
@Label("Violation")
public interface PmdViolationDescriptor extends PmdDescriptor {
    @Property("beginline")
    Integer getBeginLine();
    void setBeginLine(Integer beginLine);

    @Property("endline")
    Integer getEndLine();
    void setEndLine(Integer endLine);

    @Property("begincolumn")
    Integer getBeginColumn();
    void setBeginColumn(Integer beginColumn);

    @Property("endcolumn")
    Integer getEndColumn();
    void setEndColumn(Integer endColumn);

    @Property("rule")
    String getRule();
    void setRule(String rule);

	@Property("ruleset")
    String getRuleSet();
	void setRuleSet(String ruleset);

    @Property("package")
    String getPackage();
    void setPackage(String aPackage);

    @Property("className")
    String getClassName();
    void setClassName(String className);

    @Property("method")
    String getMethod();
    void setMethod(String method);

    @Property("variable")
    String getVariable();
    void setVariable(String variable);

    @Property("externalInfoUrl")
    String getExternalInfoUrl();
    void setExternalInfoUrl(String externalInfoUrl);

	@Property("priority")
    Integer getPriority();
	void setPriority(Integer priority);

	@Property("message")
    String getMessage();
	void setMessage(String message);
}
