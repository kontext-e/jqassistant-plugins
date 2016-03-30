package de.kontext_e.jqassistant.plugin.findbugs.store.descriptor;


import java.util.List;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author jn4, Kontext E GmbH, 05.02.14
 */
@Label("Report")
public interface FindBugsReportDescriptor extends FindBugsDescriptor, NamedDescriptor, FileDescriptor {
    @Property("version")
    String getVersion();
    void setVersion(String version);

    @Property("sequence")
    String getSequence();
    void setSequence(String sequence);

    @Property("analysisTimestamp")
    String getAnalysisTimestamp();
    void setAnalysisTimestamp(String analysisTimestamp);

    @Relation("CONTAINS")
    List<FindBugsBugInstanceDescriptor> getContains();
}
