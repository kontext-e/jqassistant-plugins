package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;


import java.util.Set;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FileDescriptor;
import com.buschmais.jqassistant.core.store.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@Label("FindBugs")
public interface FindBugsDescriptor extends Descriptor, NamedDescriptor, FileDescriptor {
    @Property("version")
    String getVersion();
    void setVersion(String version);

    @Property("sequence")
    String getSequence();
    void setSequence(String sequence);

    @Property("analysistimestamp")
    String getAnalysisTimestamp();
    void setAnalysisTimestamp(String analysisTimestamp);

    @Relation("CONTAINS")
    Set<BugInstanceDescriptor> getContains();
}
