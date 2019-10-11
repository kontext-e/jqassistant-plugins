package de.kontext_e.jqassistant.plugin.ods.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OdsFileReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(OdsFileReader.class);
  private final Store store;
  private final ExcelFileDescriptor excelFileDescriptor;
  private InputStream inputStream;
  private Map<CellKey, ExcelCellDescriptor> nameToCellDescriptors = new HashMap<>();
  private Map<CellKey, ExcelCellDescriptor> preCreatedCells = new HashMap<>();

  OdsFileReader(Store store, ExcelFileDescriptor excelFileDescriptor, InputStream inputStream) {
    this.store = store;
    this.excelFileDescriptor = excelFileDescriptor;
    this.inputStream = inputStream;
  }

  void read() throws IOException {
    SpreadSheet spread = new SpreadSheet(inputStream);
    List<Sheet> sheets = spread.getSheets();
    for (Sheet sheet : sheets) {
      readSheet(sheet);
    }
  }

  private void readSheet(Sheet sheet) {
    final String sheetName = sheet.getName();
    final ExcelSheetDescriptor excelSheetDescriptor = store.create(ExcelSheetDescriptor.class);
    excelFileDescriptor.getSheets().add(excelSheetDescriptor);
    excelSheetDescriptor.setName(sheetName);

    final Range dataRange = sheet.getDataRange();
    boolean isNewRow;
    ExcelRowDescriptor excelRowDescriptor = null;
    for(int rowIndex = 0; rowIndex < dataRange.getLastRow(); rowIndex++) {
      isNewRow = true;
      for(int columnIndex = 0; columnIndex < dataRange.getLastColumn(); columnIndex++) {
        final Range cell = dataRange.getCell(rowIndex, columnIndex);
        if(cell.getValue() == null) continue;

        if(isNewRow) {
          isNewRow = false;
          excelRowDescriptor = store.create(ExcelRowDescriptor.class);
          excelSheetDescriptor.getRows().add(excelRowDescriptor);
          excelRowDescriptor.setRowNumber(rowIndex);
        }

        final String cellName = formatAsString(columnIndex, rowIndex);
        final ExcelCellDescriptor excelCellDescriptor;
        final CellKey cellKey = new CellKey(sheetName, cellName);
        if (preCreatedCells.get(cellKey) != null) {
          excelCellDescriptor = preCreatedCells.get(cellKey);
        } else {
          excelCellDescriptor = store.create(ExcelCellDescriptor.class);
        }
        excelRowDescriptor.getCells().add(excelCellDescriptor);

        excelCellDescriptor.setColumn(columnIndex);
        excelCellDescriptor.setRow(rowIndex);
        excelCellDescriptor.setName(cellName);
//        excelCellDescriptor.setType(cell.get);
//        setStyle(cell.getCellStyle(), excelCellDescriptor);
        setCellValue(cell, excelCellDescriptor);
        final String targetCellName = excelCellDescriptor.getName();
        nameToCellDescriptors.put(new CellKey(sheetName, targetCellName), excelCellDescriptor);
      }
    }
  }

  private String formatAsString(int col, int row) {
    return ""; // FIXME or copy from excel poi
  }

  private void setCellValue(Range cell, ExcelCellDescriptor excelCellDescriptor) {
    final Object value = cell.getValue();
    switch (value.getClass().getName()) {
      default:
        LOGGER.error("Unknown cell type: "+value.getClass().getName());
        excelCellDescriptor.setStringValue(value.toString());
    }
    if(cell.getFormula() != null) {
      excelCellDescriptor.setStringValue(cell.getFormula());
      setDependencies(excelCellDescriptor, cell);
    }
  }

  private void setDependencies(ExcelCellDescriptor excelCellDescriptor, Range cell) {
    // FIXME jopendocument lacks of a formula parser,
    //  so something other has to be implemented
    //  maybe use ANTLR
  }
}
