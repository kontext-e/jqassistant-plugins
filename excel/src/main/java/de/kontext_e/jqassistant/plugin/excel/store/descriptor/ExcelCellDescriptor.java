package de.kontext_e.jqassistant.plugin.excel.store.descriptor;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Cell")
public interface ExcelCellDescriptor extends ExcelDescriptor, ExcelStyleDescriptor, NamedDescriptor {
    void setColumn(int column);
    int getColumn();

    void setRow(int row);
    int getRow();

    void setType(String type);
    String getType();

    void setBoolValue(boolean booleanCellValue);
    boolean getBoolValue();

    void setStringValue(String value);
    String getStringValue();

    void setNumericValue(double numericCellValue);
    double getNumericValue();

    void setComment(String comment);
    String getComment();

    void setBlank(boolean blank);
    boolean getBlank();

    @Relation("DEPENDS_ON")
    List<ExcelCellDescriptor> getDependencies();
}
