package de.kontext_e.jqassistant.plugin.pmd.store;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author aw, Kontext E GmbH, 29.01.15
 */
@Label("Violation")
public interface PmdViolationDescriptor extends PmdDescriptor {
    @Property("beginline")
    void setBeginLine(Integer beginLine);
	Integer getBeginLine();

    @Property("endline")
    void setEndLine(Integer endLine);
	Integer getEndLine();

    @Property("begincolumn")
    void setBeginColumn(Integer beginColumn);
	Integer getBeginColumn();

    @Property("endcolumn")
    void setEndColumn(Integer endColumn);
	Integer getEndColumn();

    @Property("rule")
    void setRule(String rule);
    String getRule();

	@Property("ruleset")
	void setRuleSet(String ruleset);
	String getRuleSet();

    @Property("package")
    void setPackage(String aPackage);
	String getPackage();

    @Property("className")
    void setClassName(String className);
	String getClassName();

    @Property("method")
    void setMethod(String method);
	String getMethod();

    @Property("variable")
    void setVariable(String variable);
	String getVariable();

    @Property("externalInfoUrl")
    void setExternalInfoUrl(String externalInfoUrl);
	String getExternalInfoUrl();

	@Property("priority")
	void setPriority(Integer priority);
	Integer getPriority();

	@Property("message")
	void setMessage(String message);
	String getMessage();
}
