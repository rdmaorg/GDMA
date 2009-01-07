package com.vodafone.gdma.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.vodafone.gdma.GdmaFacade;
import com.vodafone.gdma.domain.Column;
import com.vodafone.gdma.domain.ConnectionType;
import com.vodafone.gdma.domain.Server;
import com.vodafone.gdma.domain.Table;
import com.vodafone.gdma.domain.User;
import com.vodafone.gdma.util.ServerUtil;
import com.vodafone.gdma.web.command.AccessLists;
import com.vodafone.gdma.web.command.PaginatedSqlResponse;
import com.vodafone.gdma.web.command.PaginatedSqlRequest;

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
            realTable.setAllowDelete(table.isAllowDelete());
            realTable.setDisplayed(table.isDisplayed());
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
    public AccessLists getAccessListForTable(Long tableId) {
        // TODO extract this no a new home!
        AccessLists accessLists = new AccessLists();

        // get access list for able
        List<User> users = gdmaFacade.getUserDao().getAccess(tableId);
        if (users != null && users.size() != 0) {
            accessLists.setCanAccess(users);
        }

        // get access denied list for table
        users = gdmaFacade.getUserDao().getCannotAccess(tableId);
        if (users != null && users.size() != 0) {
            accessLists.setCanNotAccess(users);
        }

        return accessLists;
    }

    public void saveAccessList(Table table) {
        // TODO extract this no a new home!
        // the table isn't a gully populated one - so retrieve it first
        Table realTable = gdmaFacade.getTableDao().get(table.getId());
        realTable.getUsers().clear();
        for (User user : table.getUsers()) {
            User realUser = gdmaFacade.getUserDao().get(user.getId());
            realTable.getUsers().add(realUser);
        }
        gdmaFacade.getTableDao().save(realTable);
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
