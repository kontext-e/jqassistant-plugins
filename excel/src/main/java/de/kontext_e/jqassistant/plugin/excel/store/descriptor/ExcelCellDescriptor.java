package de.kontext_e.jqassistant.plugin.excel.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;

import java.util.Date;

@Label("Cell")
public interface ExcelCellDescriptor extends ExcelDescriptor, ExcelStyleDescriptor {
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

    void setDateValue(Date dateCellValue);
    Date getDateValue();

    void setNumericValue(double numericCellValue);
    double getNumericValue();

    void setComment(String comment);
    String getComment();

    void setBlank(boolean blank);
    boolean getBlack();
}
