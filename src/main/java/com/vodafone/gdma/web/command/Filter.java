package com.vodafone.gdma.web.command;

import org.springframework.util.StringUtils;

public class Filter {

    private Long columnId;

    private String columnName;

    private int columnType;

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    private String filterValue;

    private boolean blank = false;

    private boolean nullValue = false;

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public boolean isBlank() {
        return blank;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }

    public boolean isNullValue() {
        return nullValue;
    }

    public void setNullValue(boolean nullValue) {
        this.nullValue = nullValue;
    }

    public boolean isValid() {
        return isBlank() || isNullValue() || StringUtils.hasText(filterValue);
    }
}
