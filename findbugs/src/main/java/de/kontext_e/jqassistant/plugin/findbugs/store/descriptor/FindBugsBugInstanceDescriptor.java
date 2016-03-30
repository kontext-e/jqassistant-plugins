package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;

import java.util.List;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@Label("BugInstance")
public interface FindBugsBugInstanceDescriptor extends Descriptor, FindBugsSourceLineContainingDescriptor {
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

    @Relation("CLASS")
    FindBugsBugInstanceClassDescriptor getBugInstanceClass();
    void setBugInstanceClass(FindBugsBugInstanceClassDescriptor findBugsBugInstanceClassDescriptor);

    @Relation("METHODS")
    List<FindBugsBugInstanceMethodDescriptor> getBugInstanceMethods();

    @Relation("FIELDS")
    List<FindBugsBugInstanceFieldDescriptor> getBugInstanceFields();
}
