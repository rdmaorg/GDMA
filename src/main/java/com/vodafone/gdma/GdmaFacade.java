package com.vodafone.gdma;

import org.springframework.util.Assert;

import com.vodafone.gdma.dao.ColumnDao;
import com.vodafone.gdma.dao.ConnectionTypeDao;
import com.vodafone.gdma.dao.DynamicDao;
import com.vodafone.gdma.dao.ServerDao;
import com.vodafone.gdma.dao.TableDao;
import com.vodafone.gdma.dao.UserDao;

public class GdmaFacade {
    private ServerDao serverDao;

    private TableDao tableDao;

    private ColumnDao columnDao;

    private ConnectionTypeDao connectionTypeDao;

    private UserDao userDao;

    private DynamicDao dynamicDao;

    public ServerDao getServerDao() {
        return serverDao;
    }

    public TableDao getTableDao() {
        return tableDao;
    }

    public ColumnDao getColumnDao() {
        return columnDao;
    }

    public ConnectionTypeDao getConnectionTypeDao() {
        return connectionTypeDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public DynamicDao getDynamicDao() {
        return dynamicDao;
    }

    public void setServerDao(ServerDao serverDao) {
        Assert.notNull(serverDao, "a null serverDao was passed to GdmaFacade");
        this.serverDao = serverDao;
    }

    public void setTableDao(TableDao tableDao) {
        Assert.notNull(tableDao, "a null tableDao was passed to GdmaFacade");
        this.tableDao = tableDao;
    }

    public void setColumnDao(ColumnDao columnDao) {
        Assert.notNull(columnDao, "a null columnDao was passed to GdmaFacade");
        this.columnDao = columnDao;
    }

    public void setConnectionTypeDao(ConnectionTypeDao connectionTypeDao) {
        Assert.notNull(connectionTypeDao, "a null connectionTypeDao was passed to GdmaFacade");
        this.connectionTypeDao = connectionTypeDao;
    }

    public void setUserDao(UserDao userDao) {
        Assert.notNull(userDao, "a null userDao was passed to GdmaFacade");
        this.userDao = userDao;
    }

    public void setDynamicDao(DynamicDao dynamicDao) {
        Assert.notNull(dynamicDao, "a null dynamicDao was passed to GdmaFacade");
        this.dynamicDao = dynamicDao;
    }

}
