package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;

import com.buschmais.jqassistant.core.store.api.descriptor.Descriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@Label("BUGINSTANCE")
public interface BugInstanceDescriptor extends Descriptor {
    @Property("TYPE")
    String getType();
    void setType(String type);

    @Property("PRIORITY")
    String getPriority();
    void setPriority(String priority);

    @Property("ABBREV")
    String getAbbrev();
    void setAbbrev(String abbrev);

    @Property("CATEGORY")
    String getCategory();
    void setCategory(String category);

    @Relation("SOURCELINE")
    SourceLineDescriptor getSourceLineDescriptor();
    void setSourceLineDescriptor(SourceLineDescriptor sourceLineDescriptor);
}
