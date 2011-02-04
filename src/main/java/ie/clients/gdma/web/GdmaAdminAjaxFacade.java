package ie.clients.gdma.web;

import ie.clients.gdma.GdmaFacade;
import ie.clients.gdma.domain.Column;
import ie.clients.gdma.domain.ConnectionType;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.domain.User;
import ie.clients.gdma.domain.UserAccess;
import ie.clients.gdma.util.ServerUtil;
import ie.clients.gdma.web.command.AccessLists;
import ie.clients.gdma.web.command.PaginatedSqlRequest;
import ie.clients.gdma.web.command.PaginatedSqlResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;


/**
 * This is a utility class which will supply the resources to DWR. It will also
 * trap Exceptions and create a user friendly message
 * 
 * @author ronan
 * 
 */
public class GdmaAdminAjaxFacade {

    private static Logger LOG = Logger.getLogger(GdmaAdminAjaxFacade.class);

    private GdmaFacade gdmaFacade;

    private ServerUtil serverUtil;

    public void setGdmaFacade(GdmaFacade gdmaFacade) {
        Assert.notNull(gdmaFacade, "a null gdmaFacade was passed to GdmaAdminAjaxFacade");
        this.gdmaFacade = gdmaFacade;
    }

    public void setServerUtil(ServerUtil serverUtil) {
        Assert.notNull(serverUtil, "a null serverUtil was passed to GdmaAdminAjaxFacade");
        this.serverUtil = serverUtil;
    }

    public List<Server> getServers() {
        return gdmaFacade.getServerDao().get();
    }

    public void deleteServer(Server server) {
        gdmaFacade.getServerDao().delete(server);
    }

    public void saveServers(List<Server> servers) {
        gdmaFacade.getServerDao().save(servers);
    }

    /**
     * Only called from admin and it's a special case. We need to re-sync the
     * tables before calling it, just to ensure that the list is current
     * 
     * @return
     */
    public Set<Table> getTablesForServer(Long serverId) {
        // TODO use an AOP trigger for this
        Server server = gdmaFacade.getServerDao().get(serverId);
        serverUtil.resyncTableList(server);
        return server.getTables();
    }

    public void saveTables(List<Table> tables) {
        // FIXME hack to get around cascading saves
        List<Table> realTables = new ArrayList<Table>();
        for (Table table : tables) {
            Table realTable = gdmaFacade.getTableDao().get(table.getId());
            //realTable.setAllowDelete(table.isAllowDelete());
            //realTable.setDisplayed(table.isDisplayed());
            realTables.add(realTable);
        }
        gdmaFacade.getTableDao().save(realTables);

    }

    /**
     * Only called from admin and it's a special case. We need to re-sync the
     * columns before calling it, just to ensure that the list is current
     * 
     * @return
     */
    public Set<Column> getColumnsForTable(Long serverId, Long tableId) {
        // TODO use an AOP trigger for this
        Server server = gdmaFacade.getServerDao().get(serverId);
        Table table = gdmaFacade.getTableDao().get(tableId);
        serverUtil.resyncColumnList(server, table);
        Set<Column> columns = table.getColumns();
        return columns;
    }

    public void saveColumns(List<Column> columns) {
        for (Column column : columns) {
            LOG.debug(" ====> " + column.getName() + " " + column.getOrderby());
        }
        gdmaFacade.getColumnDao().save(columns);
    }

    public List<ConnectionType> getConnectionTypes() {
        return gdmaFacade.getConnectionTypeDao().get();
    }

    public List<Server> getServerTableColumnList() {
        return gdmaFacade.getServerDao().getServerTableColumnList();
    }

    @SuppressWarnings("unchecked")
    public List<UserAccess> getAccessListForTable(Long tableId) {
        
    	List<User> userList = gdmaFacade.getUserDao().get();
    	List<UserAccess> userAccessList = new ArrayList<UserAccess>();;
    	for(User user :userList)
    	{
    		UserAccess userAccess = gdmaFacade.getUserAccessDao().get(tableId, user.getId());
    		if(userAccess == null)
    		{
    			UserAccess emptyUserAccess = new UserAccess();
    			emptyUserAccess.setUser(user);
    			emptyUserAccess.setUserId(user.getId());
    			emptyUserAccess.setTableId(tableId);
    			emptyUserAccess.setAllowDisplay(false);
    			emptyUserAccess.setAllowUpdate(false);
    			emptyUserAccess.setAllowInsert(false);
    			emptyUserAccess.setAllowDelete(false);
    			gdmaFacade.getUserAccessDao().save(emptyUserAccess);
    			//emptyUserAccess.getUser().getUserName();
    			userAccessList.add(emptyUserAccess);
    		}
    		else
    		{
    			userAccessList.add(userAccess);
    		}
    		
    	}
    	return userAccessList;
    	
    }
    
    @SuppressWarnings("unchecked")
    public List<User> getUserList() {
        
    	List<User> userList = gdmaFacade.getUserDao().get();
    	return userList;    	
    }
    
    @SuppressWarnings("unchecked")
    public UserAccess getUserAccessForTable(Long tableId, Long userId) {
        
    	UserAccess userAccess = gdmaFacade.getUserAccessDao().get(tableId, userId);
    	if(userAccess == null)
    	{
    		return null;
    	}
    	else
    	{
    		return userAccess;    	
    	}
    	
    }
    

    public void saveAccessList(UserAccess userAccess) {
    	UserAccess realUserAccess = gdmaFacade.getUserAccessDao().get(userAccess.getTableId(), userAccess.getUserId());
    	realUserAccess.setAllowDisplay(userAccess.getAllowDisplay());
    	realUserAccess.setAllowUpdate(userAccess.getAllowUpdate());
    	realUserAccess.setAllowInsert(userAccess.getAllowInsert());
    	realUserAccess.setAllowDelete(userAccess.getAllowDelete());
    	gdmaFacade.getUserAccessDao().save(realUserAccess);
    }

    public List<User> getUsers() {
        return gdmaFacade.getUserDao().get();
    }

    public void deleteUser(User user) {
        gdmaFacade.getUserDao().delete(user);
    }

    public void saveUsers(List<User> users) {
        gdmaFacade.getUserDao().save(users);
    }

    public List<Column> executeSelectGetColumns(Long serverId, String sql) {
        return gdmaFacade.getDynamicDao().executeSelectGetColumns(serverId, sql);
    }

    public PaginatedSqlResponse executeSelect(PaginatedSqlRequest paginatedSqlRequest) {
        return gdmaFacade.getDynamicDao().executeSelect(paginatedSqlRequest);
    }

}
