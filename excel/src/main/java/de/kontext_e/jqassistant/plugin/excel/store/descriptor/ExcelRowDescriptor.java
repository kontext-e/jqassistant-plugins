package de.kontext_e.jqassistant.plugin.excel.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Row")
public interface ExcelRowDescriptor extends ExcelDescriptor {

    @Relation("HAS_CELL")
    List<ExcelCellDescriptor> getCells();

    void setRowNumber(int rowNum);
    int getRowNumber();
}
