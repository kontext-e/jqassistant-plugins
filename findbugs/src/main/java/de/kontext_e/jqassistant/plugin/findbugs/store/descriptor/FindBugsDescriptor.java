package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;


import java.util.Set;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.jqassistant.core.store.api.model.FileDescriptor;
import com.buschmais.jqassistant.core.store.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@Label("FINDBUGS")
public interface FindBugsDescriptor extends Descriptor, NamedDescriptor, FileDescriptor {
    @Property("VERSION")
    String getVersion();
    void setVersion(String version);

    @Property("SEQUENCE")
    String getSequence();
    void setSequence(String sequence);

    @Property("ANALYSISTIMESTAMP")
    String getAnalysisTimestamp();
    void setAnalysisTimestamp(String analysisTimestamp);

    @Property("CONTAINS")
    Set<BugInstanceDescriptor> getContains();
}
