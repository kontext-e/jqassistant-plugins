package de.kontext_e.jqassistant.plugin.spotbugs.store.descriptor;

import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("FindBugs")
@Abstract
public interface FindBugsDescriptor extends SpotBugsDescriptor {
}
