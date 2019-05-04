package de.kontext_e.jqassistant.plugin.excel.scanner;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;
import org.apache.poi.ss.util.CellReference;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OdsFileReaderTest {

    @Test
    public void testReadOdf() throws IOException {
        final SpreadSheet spreadSheet = new SpreadSheet(new File("src/test/resources/formulas.ods"));
        List<Sheet> sheets = spreadSheet.getSheets();
        for (Sheet sheet : sheets) {
            for(int rowIndex = 0; rowIndex < sheet.getMaxRows(); rowIndex++) {
                for(int columnIndex = 0; columnIndex < sheet.getMaxColumns(); columnIndex++) {
                    final Range dataRange = sheet.getDataRange();
                    final Range cell = dataRange.getCell(rowIndex, columnIndex);
                    if(cell.getValue() == null) continue;

                    System.out.println("cell name: "+formatAsString(columnIndex, rowIndex));
                    System.out.println("valueType="+cell.getValue().getClass().getName());
                    final Object value = cell.getValue();
                    System.out.println("value: "+value);
                    System.out.println("Formula: "+cell.getFormula());
                }
            }
        }
    }

    public String formatAsString(int col, int row) {
        return CellReference.convertNumToColString(col)+(row+1);
    }
}
