package de.kontext_e.jqassistant.plugin.excel.store.descriptor;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

public interface ExcelFileDescriptor extends ExcelDescriptor, FileDescriptor {

  @Relation("HAS_SHEET")
  List<ExcelSheetDescriptor> getSheets();
}
