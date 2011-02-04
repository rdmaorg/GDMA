package ie.clients.gdma.web.command;

import ie.clients.gdma.domain.UserAccess;

import java.util.ArrayList;
import java.util.List;

public class PaginatedRequest {
    public static final String YUI_ASC = "yui-dt-asc";

    public static final String YUI_DESC = "yui-dt-desc";

    private Long serverId;

    private Long tableId;

    private int rowsPerPage;

    private int recordOffset;

    private Long sortedByColumnId;

    private String dir = YUI_ASC;

    private List<Filter> filters = new ArrayList<Filter>();
    
    
    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public int getRecordOffset() {
        return recordOffset;
    }

    public void setRecordOffset(int recordOffset) {
        this.recordOffset = recordOffset;
    }

    public Long getSortedByColumnId() {
        return sortedByColumnId;
    }

    public void setSortedByColumnId(Long sortedByColumnId) {
        this.sortedByColumnId = sortedByColumnId;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

}
