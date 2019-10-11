package de.kontext_e.jqassistant.plugin.excel.store.descriptor;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Sheet")
public interface ExcelSheetDescriptor extends ExcelDescriptor, NamedDescriptor {

  @Relation("HAS_ROW")
  List<ExcelRowDescriptor> getRows();
}
