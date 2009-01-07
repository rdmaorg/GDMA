/*
 * Created on 14-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.domain;

/**
 * @author Ronan Gill
 * 
 * 14-Mar-2004
 */
public class ConnectionType {
    private long id;

    private String name;

    private String SQLGetTables;

    private String connectionClass;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSQLGetTables() {
        return SQLGetTables;
    }

    public void setSQLGetTables(String getTables) {
        SQLGetTables = getTables;
    }

    public String getConnectionClass() {
        return connectionClass;
    }

    public void setConnectionClass(String connectionClass) {
        this.connectionClass = connectionClass;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof ConnectionType))
            return false;
        final ConnectionType that = (ConnectionType) other;
        return this.name.equals(that.getName());
    }

    public int hashCode() {
        return name.hashCode();
    }

}
