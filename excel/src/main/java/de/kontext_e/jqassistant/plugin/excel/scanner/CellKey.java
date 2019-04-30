package de.kontext_e.jqassistant.plugin.excel.scanner;

import java.util.Objects;

class CellKey {
    private final String sheetName;
    private final String cellName;

    CellKey(String sheetName, String cellName) {
        this.sheetName = sheetName;
        this.cellName = cellName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellKey cellKey = (CellKey) o;
        return Objects.equals(sheetName, cellKey.sheetName) &&
                Objects.equals(cellName, cellKey.cellName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheetName, cellName);
    }

    @Override
    public String toString() {
        return "CellKey{" +
                "sheetName='" + sheetName + '\'' +
                ", cellName='" + cellName + '\'' +
                '}';
    }


}
