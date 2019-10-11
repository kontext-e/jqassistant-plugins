package de.kontext_e.jqassistant.plugin.pmd.store;


import java.util.Set;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Property;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * @author aw, Kontext E GmbH, 29.01.15
 */
@Label("Report")
public interface PmdReportDescriptor extends PmdDescriptor, FileDescriptor {

    @Relation("HAS_FILE")
    Set<PmdFileDescriptor> getFiles();

	@Property("version")
	String getVersion();
    void setVersion(String version);

	@Property("timestamp")
	String getTimestamp();
    void setTimestamp(String timestamp);
}
