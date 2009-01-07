package com.vodafone.gdma.web.command;

import java.util.List;

public class PaginatedSqlResponse {

    private List records;

    private long totalRecords;

    private int startIndex;

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

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

}
