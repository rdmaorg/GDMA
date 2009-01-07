package com.vodafone.gdma.web.command;

public class ColumnUpdate {
    private Long columnId;

    private String newColumnValue;

    private String oldColumnValue;

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getNewColumnValue() {
        return newColumnValue;
    }

    public void setNewColumnValue(String newColumnValue) {
        this.newColumnValue = newColumnValue;
    }

    public String getOldColumnValue() {
        return oldColumnValue;
    }

    public void setOldColumnValue(String oldColumnValue) {
        this.oldColumnValue = oldColumnValue;
    }

}
