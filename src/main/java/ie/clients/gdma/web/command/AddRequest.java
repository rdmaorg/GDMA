package ie.clients.gdma.web.command;

import java.util.ArrayList;
import java.util.List;

public class AddRequest {
    private Long serverId;

    private Long tableId;

    private List<ColumnUpdate> updates = new ArrayList<ColumnUpdate>();
}
