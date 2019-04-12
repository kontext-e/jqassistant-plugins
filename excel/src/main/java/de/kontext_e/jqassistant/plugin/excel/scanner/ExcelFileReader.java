package de.kontext_e.jqassistant.plugin.excel.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.ExcelCellDescriptor;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.ExcelFileDescriptor;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.ExcelRowDescriptor;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.ExcelSheetDescriptor;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;

public class ExcelFileReader {
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

      for (Cell cell : row) {
        final ExcelCellDescriptor excelCellDescriptor = store.create(ExcelCellDescriptor.class);
        excelRowDescriptor.getCells().add(excelCellDescriptor);
        excelCellDescriptor.setColumn(cell.getAddress().getColumn());
        excelCellDescriptor.setRow(cell.getAddress().getRow());
        excelCellDescriptor.setType(cell.getCellType().name());
        setCellValue(cell, excelCellDescriptor);
      }
    }
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
        break;
      default:
        excelCellDescriptor.setStringValue("");
    }
  }

}
