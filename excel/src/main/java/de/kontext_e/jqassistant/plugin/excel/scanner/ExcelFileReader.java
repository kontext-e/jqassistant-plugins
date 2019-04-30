package de.kontext_e.jqassistant.plugin.excel.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.*;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
        final String cellName = createName(cell.getAddress());
        final ExcelCellDescriptor excelCellDescriptor;
        if (pre.get(cellName) != null) {
          excelCellDescriptor = pre.get(cellName);
        } else {
          excelCellDescriptor = store.create(ExcelCellDescriptor.class);
        }
        excelRowDescriptor.getCells().add(excelCellDescriptor);
        excelCellDescriptor.setColumn(cell.getAddress().getColumn());
        excelCellDescriptor.setRow(cell.getAddress().getRow());
        excelCellDescriptor.setName(cellName);
        excelCellDescriptor.setType(cell.getCellType().name());
        if(cell.getCellComment() != null && cell.getCellComment().getString() != null) {
          excelCellDescriptor.setComment(cell.getCellComment().getString().getString());
        }
        setStyle(cell.getCellStyle(), excelCellDescriptor);
        setCellValue(cell, excelCellDescriptor);
        nameToCellDescriptors.put(excelCellDescriptor.getName(), excelCellDescriptor);
      }
    }
  }

  private String createName(CellAddress address) {
    return address.formatAsString();
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
        if(cell.isPartOfArrayFormulaGroup() && cell.getArrayFormulaRange() != null) {
          LOGGER.info("getArrayFormulaRange(): " + cell.getArrayFormulaRange());
        }
        excelCellDescriptor.setStringValue(cell.getCellFormula());
        setDependencies(excelCellDescriptor, cell);
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

  private Map<String, ExcelCellDescriptor> nameToCellDescriptors = new HashMap<>();
  private Map<String, ExcelCellDescriptor> pre = new HashMap<>();
  void setDependencies(ExcelCellDescriptor excelCellDescriptor, Cell cell) {
    final Ptg[] parse = FormulaParser.parse(cell.getCellFormula(), null, FormulaType.CELL, 0);
    for (Ptg ptg : parse) {
      System.out.println("ptg="+ptg);
      if(ptg instanceof RefPtg) {
        final ExcelCellDescriptor excelCellDescriptor1 = nameToCellDescriptors.get(ptg.toFormulaString());
        setCellDependency(excelCellDescriptor, ptg.toFormulaString(), excelCellDescriptor1);
      }
      if(ptg instanceof AreaPtg) {
        final AreaPtg areaPtg = (AreaPtg) ptg;
        final int firstColumn = areaPtg.getFirstColumn();
        final int firstRow = areaPtg.getFirstRow();
        final int lastColumn = areaPtg.getLastColumn();
        final int lastRow = areaPtg.getLastRow();
        System.out.println(firstRow+":"+firstColumn+"-"+lastRow+":"+lastColumn);
        final Sheet sheet = cell.getSheet();
        for(int row = firstRow; row <= lastRow; row++) {
          final Row sheetRow = sheet.getRow(row);
          for(int col = firstColumn; col <= lastColumn; col++) {
            final Cell cell1 = sheetRow.getCell(col);
            final String name = createName(cell1.getAddress());
            System.out.println("In area cell name: "+name);
            final ExcelCellDescriptor excelCellDescriptor1 = nameToCellDescriptors.get(name);
            setCellDependency(excelCellDescriptor, name, excelCellDescriptor1);
          }
        }
      }
    }
  }

  private void setCellDependency(ExcelCellDescriptor excelCellDescriptor, String targetCellName, ExcelCellDescriptor excelCellDescriptor1) {
    if(excelCellDescriptor1 != null) {
      System.out.println("Found ExcelCellDescriptor " + excelCellDescriptor1.getName() + " for RefPtg");
      excelCellDescriptor.getDependencies().add(excelCellDescriptor1);
    } else {
      System.out.println("Create a pre cell descriptor for "+targetCellName);
      final ExcelCellDescriptor excelCellDescriptor2 = store.create(ExcelCellDescriptor.class);
      excelCellDescriptor2.setName(targetCellName);
      pre.put(excelCellDescriptor2.getName(), excelCellDescriptor2);
      excelCellDescriptor.getDependencies().add(excelCellDescriptor2);
    }
  }

}
