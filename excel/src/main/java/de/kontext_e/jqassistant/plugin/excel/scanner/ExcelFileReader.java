package de.kontext_e.jqassistant.plugin.excel.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.*;
import org.apache.poi.hssf.usermodel.HSSFEvaluationWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.FormulaParser;
import org.apache.poi.ss.formula.FormulaParsingWorkbook;
import org.apache.poi.ss.formula.FormulaType;
import org.apache.poi.ss.formula.ptg.AreaPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.apache.poi.ss.formula.ptg.Ref3DPxg;
import org.apache.poi.ss.formula.ptg.RefPtg;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFEvaluationWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ExcelFileReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileReader.class);
  private final Store store;
  private final ExcelFileDescriptor excelFileDescriptor;
  private InputStream inputStream;
  private Map<CellKey, ExcelCellDescriptor> nameToCellDescriptors = new HashMap<>();
  private Map<CellKey, ExcelCellDescriptor> preCreatedCells = new HashMap<>();

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
    final String sheetName = sheet.getSheetName();
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
        final CellKey cellKey = new CellKey(sheetName, cellName);
        if (preCreatedCells.get(cellKey) != null) {
          excelCellDescriptor = preCreatedCells.get(cellKey);
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
        final String targetCellName = excelCellDescriptor.getName();
        nameToCellDescriptors.put(new CellKey(sheetName, targetCellName), excelCellDescriptor);
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
          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          final String formattedDate = simpleDateFormat.format(cell.getDateCellValue());
          excelCellDescriptor.setStringValue(formattedDate);
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

  private void setDependencies(ExcelCellDescriptor excelCellDescriptor, Cell cell) {
    final Ptg[] ptgs = FormulaParser.parse(cell.getCellFormula(), createFpb(cell), FormulaType.CELL, -1);
    for (Ptg ptg : ptgs) {
      caseLocalRef(excelCellDescriptor, cell, ptg);
      caseRefToOtherSheet(excelCellDescriptor, ptg);
      caseArea(excelCellDescriptor, cell, ptg);
    }
  }

  private void caseArea(ExcelCellDescriptor excelCellDescriptor, Cell cell, Ptg ptg) {
    if(ptg instanceof AreaPtg) {
      final AreaPtg areaPtg = (AreaPtg) ptg;
      final int firstColumn = areaPtg.getFirstColumn();
      final int firstRow = areaPtg.getFirstRow();
      final int lastColumn = areaPtg.getLastColumn();
      final int lastRow = areaPtg.getLastRow();
      final Sheet sheet = cell.getSheet();
      for(int row = firstRow; row <= lastRow; row++) {
        final Row sheetRow = sheet.getRow(row);
        for(int col = firstColumn; col <= lastColumn; col++) {
          final Cell cell1 = sheetRow.getCell(col);
          final String targetCellName = createName(cell1.getAddress());
          final String targetSheetName = cell1.getSheet().getSheetName();
          final ExcelCellDescriptor excelCellDescriptor1 = nameToCellDescriptors.get(new CellKey(targetSheetName, targetCellName));
          setCellDependency(excelCellDescriptor, targetSheetName, targetCellName, excelCellDescriptor1);
        }
      }
    }
  }

  private void caseRefToOtherSheet(ExcelCellDescriptor excelCellDescriptor, Ptg ptg) {
    if(ptg instanceof Ref3DPxg) {
      Ref3DPxg ref3DPxg = (Ref3DPxg) ptg;
      final String sheetName = ref3DPxg.getSheetName();
      final String cellName = ref3DPxg.format2DRefAsString();
      CellKey cellKey = new CellKey(sheetName, cellName);
      final ExcelCellDescriptor excelCellDescriptor1 = nameToCellDescriptors.get(cellKey);
      setCellDependency(excelCellDescriptor, sheetName, ptg.toFormulaString(), excelCellDescriptor1);
    }
  }

  private void caseLocalRef(ExcelCellDescriptor excelCellDescriptor, Cell cell, Ptg ptg) {
    if(ptg instanceof RefPtg) {
      final String sheetName = cell.getSheet().getSheetName();
      final ExcelCellDescriptor excelCellDescriptor1 = nameToCellDescriptors.get(new CellKey(sheetName, ptg.toFormulaString()));
      setCellDependency(excelCellDescriptor, sheetName, ptg.toFormulaString(), excelCellDescriptor1);
    }
  }

  private void setCellDependency(ExcelCellDescriptor sourceCell, String targetSheetName, String targetCellName, ExcelCellDescriptor targetCell) {
    if(targetCell != null) {
      sourceCell.getDependencies().add(targetCell);
    } else {
      final ExcelCellDescriptor excelCellDescriptor2 = store.create(ExcelCellDescriptor.class);
      excelCellDescriptor2.setName(targetCellName);
      CellKey cellKey = new CellKey(targetSheetName, targetCellName);
      preCreatedCells.put(cellKey, excelCellDescriptor2);
      sourceCell.getDependencies().add(excelCellDescriptor2);
    }
  }

  private FormulaParsingWorkbook createFpb(Cell cell) {
    FormulaParsingWorkbook fpb;
    if(cell instanceof XSSFCell) {
      XSSFWorkbook wb = (XSSFWorkbook) cell.getSheet().getWorkbook();
      fpb = XSSFEvaluationWorkbook.create(wb);
    } else {
      final HSSFWorkbook workbook = (HSSFWorkbook) cell.getSheet().getWorkbook();
      fpb = HSSFEvaluationWorkbook.create(workbook);
    }
    return fpb;
  }

}
