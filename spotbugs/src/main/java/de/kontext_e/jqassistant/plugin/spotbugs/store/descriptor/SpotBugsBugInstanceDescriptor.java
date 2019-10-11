package de.kontext_e.jqassistant.plugin.spotbugs.store.descriptor;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@Label("BugInstance")
public interface SpotBugsBugInstanceDescriptor extends FindBugsDescriptor, FindBugsSourceLineContainingDescriptor {
    @Property("type")
    String getType();
    void setType(String type);

    @Property("priority")
    String getPriority();
    void setPriority(String priority);

    @Property("abbrev")
    String getAbbrev();
    void setAbbrev(String abbrev);

    @Property("category")
    String getCategory();
    void setCategory(String category);

    @Property("message")
    String getMessage();
    void setMessage(String message);

    @Property("className")
    String getClassName();
    void setClassName(String className);

    @Property("lineNumber")
    Integer getLineNumber();
    void setLineNumber(Integer lineNumber);

    @Relation("HAS_CLASS")
    FindBugsBugInstanceClassDescriptor getBugInstanceClass();
    void setBugInstanceClass(FindBugsBugInstanceClassDescriptor findBugsBugInstanceClassDescriptor);

    @Relation("HAS_METHOD")
    List<FindBugsBugInstanceMethodDescriptor> getBugInstanceMethods();

    @Relation("HAS_FIELD")
    List<FindBugsBugInstanceFieldDescriptor> getBugInstanceFields();
}
