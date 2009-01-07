package com.vodafone.gdma.web.command;

import java.util.List;

public class PaginatedResponse {

    private List records;

    private long totalRecords;

    private int startIndex;

    private String key;

    private String sortDir = PaginatedRequest.YUI_DESC;

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    private String fields[];

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public List getRecords() {
        return records;
    }

    public void setRecords(List records) {
        this.records = records;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

}
