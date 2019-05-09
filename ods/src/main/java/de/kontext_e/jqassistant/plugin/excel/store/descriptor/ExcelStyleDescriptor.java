package de.kontext_e.jqassistant.plugin.excel.store.descriptor;

public interface ExcelStyleDescriptor {
    void setDataFormat(short dataFormat);
    short getDataFormat();

    void setDataFormatString(String dataFormatString);
    String getDataFormatString();

    void setBottomBorderColor(short bottomBorderColor);
    short getBottomBorderColor();

    void setLeftBorderColor(short leftBorderColor);
    short getLeftBorderColor();

    void setRightBorderColor(short rightBorderColor);
    short getRightBorderColor();

    void setTopBorderColor(short topBorderColor);
    short getTopBorderColor();

    void setBackgroundColor(short fillBackgroundColor);
    short getBackgroundColor();

    void setForegroundColor(short fillForegroundColor);
    short getForegroundColor();

    void setFillPattern(short fillPattern);
    short getFillPattern();

    void setHidden(boolean hidden);
    boolean getHidden();

    void setLocked(boolean locked);
    boolean getLocked();

    void setQuotePrefix(boolean quotePrefixed);
    boolean getQuotePrefix();
}
