package ie.clients.gdma.domain;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class UserAccess implements Serializable {

	private static final long serialVersionUID = -2834870515554339937L;

	private Long userId;
	
	private Long tableId;
	
	private boolean allowDisplay = false;
	
	private boolean allowUpdate = false;
	
	private boolean allowInsert = false;
	
	private boolean allowDelete = false;
	
	private User user;
	
	private Table table;
	
	public Long  getUserId() {
        return userId;
    }

    public void setUserId(Long  userId) {
        this.userId = userId;
    }
    
    public Long  getTableId() {
        return tableId;
    }

    public void setTableId(Long  tableId) {
        this.tableId = tableId;
    }
    
    public boolean getAllowDisplay() {
        return allowDisplay;
    }

    public void setAllowDisplay(boolean allowDisplay) {
        this.allowDisplay = allowDisplay;
    }
    
    public boolean getAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }
    
    public boolean getAllowInsert() {
        return allowInsert;
    }

    public void setAllowInsert(boolean allowInsert) {
        this.allowInsert = allowInsert;
    }
    
    public boolean getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }   
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    } 
    
}
