package ie.clients.gdma.web.command;

import java.util.ArrayList;
import java.util.List;

public class UpdateRequest {
    private Long serverId;

    private Long tableId;

    private List<List<ColumnUpdate>> updates = new ArrayList(new ArrayList<ColumnUpdate>());

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

    public List<List<ColumnUpdate>> getUpdates() {
        return updates;
    }

    public void setUpdates(List<List<ColumnUpdate>> updates) {
        this.updates = updates;
    }

}
