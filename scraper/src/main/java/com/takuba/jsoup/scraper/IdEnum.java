package com.takuba.jsoup.scraper;

import java.util.HashMap;
import java.util.Map;

public enum IdEnum {
    EDITORIALS("editorials","editorial"),
    AUTHORS("authors","author"),
    BINDINGS("bindings","binding");
    private static final Map<String, IdEnum> BY_LABEL = new HashMap<>();
    private static final Map<String , IdEnum> BY_COLUMN_NAME = new HashMap<>();
    static {
        for (IdEnum e : values()) {
            BY_LABEL.put(e.tableName, e);
            BY_COLUMN_NAME.put(e.columnName, e);
        }
    }
    public final String tableName;
    public final String columnName;
    IdEnum(String tableName,String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }
    @Override
    public String toString() {
        return this.tableName;
    }
}
