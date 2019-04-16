package de.kontext_e.jqassistant.plugin.excel.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.*;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ExcelFileReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileReader.class);
  private final Store store;
  private final ExcelFileDescriptor excelFileDescriptor;
  private InputStream inputStream;

  public ExcelFileReader(Store store, ExcelFileDescriptor excelFileDescriptor, InputStream inputStream) {
    this.store = store;
    this.excelFileDescriptor = excelFileDescriptor;
    this.inputStream = inputStream;
  }

  void read() throws IOException {
    try(Workbook workbook = WorkbookFactory.create(inputStream)) {
      for (Sheet sheet : workbook) {
        readSheet(sheet);
      }
    }
  }

  private void readSheet(Sheet sheet) {
    final ExcelSheetDescriptor excelSheetDescriptor = store.create(ExcelSheetDescriptor.class);
    excelFileDescriptor.getSheets().add(excelSheetDescriptor);
    excelSheetDescriptor.setName(sheet.getSheetName());

    for (Row row : sheet) {
      final ExcelRowDescriptor excelRowDescriptor = store.create(ExcelRowDescriptor.class);
      excelSheetDescriptor.getRows().add(excelRowDescriptor);
      excelRowDescriptor.setRowNumber(row.getRowNum());
      setStyle(row.getRowStyle(), excelRowDescriptor);

      for (Cell cell : row) {
        final ExcelCellDescriptor excelCellDescriptor = store.create(ExcelCellDescriptor.class);
        excelRowDescriptor.getCells().add(excelCellDescriptor);
        excelCellDescriptor.setColumn(cell.getAddress().getColumn());
        excelCellDescriptor.setRow(cell.getAddress().getRow());
        excelCellDescriptor.setType(cell.getCellType().name());
        excelCellDescriptor.setComment(cell.getCellComment().getString().getString());
        setStyle(cell.getCellStyle(), excelCellDescriptor);
        setCellValue(cell, excelCellDescriptor);
      }
    }
  }

  private void setStyle(CellStyle cellStyle, ExcelStyleDescriptor excelStyleDescriptor) {
    if(cellStyle == null) return;

    excelStyleDescriptor.setDataFormat(cellStyle.getDataFormat());
    excelStyleDescriptor.setDataFormatString(cellStyle.getDataFormatString());
    excelStyleDescriptor.setBottomBorderColor(cellStyle.getBottomBorderColor());
    excelStyleDescriptor.setLeftBorderColor(cellStyle.getLeftBorderColor());
    excelStyleDescriptor.setRightBorderColor(cellStyle.getRightBorderColor());
    excelStyleDescriptor.setTopBorderColor(cellStyle.getTopBorderColor());
    excelStyleDescriptor.setBackgroundColor(cellStyle.getFillBackgroundColor());
    excelStyleDescriptor.setForegroundColor(cellStyle.getFillForegroundColor());
    excelStyleDescriptor.setFillPattern(cellStyle.getFillPattern().getCode());
    excelStyleDescriptor.setHidden(cellStyle.getHidden());
    excelStyleDescriptor.setLocked(cellStyle.getLocked());
    excelStyleDescriptor.setQuotePrefix(cellStyle.getQuotePrefixed());
  }

  private void setCellValue(Cell cell, ExcelCellDescriptor excelCellDescriptor) {
    switch (cell.getCellType()) {
      case BOOLEAN:
        excelCellDescriptor.setBoolValue(cell.getBooleanCellValue());
        break;
      case STRING:
        excelCellDescriptor.setStringValue(cell.getRichStringCellValue().getString());
        break;
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          excelCellDescriptor.setDateValue(cell.getDateCellValue());
        } else {
          excelCellDescriptor.setNumericValue(cell.getNumericCellValue());
        }
        break;
      case FORMULA:
        excelCellDescriptor.setStringValue(cell.getCellFormula());
        break;
      case BLANK:
        excelCellDescriptor.setStringValue("");
        excelCellDescriptor.setBlank(true);
        break;
      default:
        LOGGER.error("Unknown cell type: "+cell.getCellType());
        excelCellDescriptor.setStringValue("");
    }
  }

}
