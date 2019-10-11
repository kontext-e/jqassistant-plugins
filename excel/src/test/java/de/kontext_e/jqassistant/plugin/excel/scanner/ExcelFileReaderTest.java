package de.kontext_e.jqassistant.plugin.excel.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.ExcelCellDescriptor;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.ExcelFileDescriptor;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.ExcelRowDescriptor;
import de.kontext_e.jqassistant.plugin.excel.store.descriptor.ExcelSheetDescriptor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ExcelFileReaderTest {
    private Store mockStore = mock(Store.class);
    private ExcelFileDescriptor excelFileDescriptor = mock(ExcelFileDescriptor.class);
    private ExcelSheetDescriptor excelSheetDescriptor = mock(ExcelSheetDescriptor.class);
    private ExcelRowDescriptor excelRowDescriptor = mock(ExcelRowDescriptor.class);
    private ExcelCellDescriptor excelCellDescriptor = mock(ExcelCellDescriptor.class);
    private InputStream inputStream = mock(InputStream.class);

    private List<ExcelSheetDescriptor> sheets;
    private List<ExcelRowDescriptor> rows;
    private List<ExcelCellDescriptor> cells;

    @Before
    public void setUp() {
        sheets = new ArrayList<>();
        rows = new ArrayList<>();
        cells = new ArrayList<>();

        when(mockStore.create(ExcelSheetDescriptor.class)).thenReturn(excelSheetDescriptor);
        when(mockStore.create(ExcelRowDescriptor.class)).thenReturn(excelRowDescriptor);
        when(mockStore.create(ExcelCellDescriptor.class)).thenReturn(excelCellDescriptor);
        when(excelFileDescriptor.getSheets()).thenReturn(sheets);
        when(excelSheetDescriptor.getRows()).thenReturn(rows);
        when(excelRowDescriptor.getCells()).thenReturn(cells);
    }

    @Test
    public void testReadExcelFile() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        createWorkbook(outputStream);
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ExcelFileReader excelFileReader = new ExcelFileReader(mockStore, excelFileDescriptor, inputStream);

        excelFileReader.read();

        assertEquals(1, sheets.size());
        assertEquals(1, rows.size());
        assertEquals(4, cells.size());
        verify(excelSheetDescriptor).setName("Employee");
        verify(excelCellDescriptor).setStringValue("Name");
        verify(excelCellDescriptor).setStringValue("Email");
        verify(excelCellDescriptor).setStringValue("Date Of Birth");
        verify(excelCellDescriptor).setStringValue("Salary");
    }

    private void createWorkbook(OutputStream outputStream) throws IOException {
        String[] columns = {"Name", "Email", "Date Of Birth", "Salary"};
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employee");
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        workbook.write(outputStream);
        workbook.close();
    }


}
