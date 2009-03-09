/*
 * Created on 13-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ie.clients.gdma.domain;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ronan Gill
 */
public class Server {

    private Long id;

    private String name;

    private String username;

    private String password;

    private String connectionUrl;

    private ConnectionType connectionType;

    private String prefix;

    // the following 2 values are runtime values and are not persisted
    private boolean connected;

    private String lastError;

    private Set<Table> tables = new LinkedHashSet<Table>();

    public Server() {
        connected = false;
        lastError = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getPrefix() {
        return prefix;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Set<Table> getTables() {
        return tables;
    }

    public void setTables(Set<Table> tables) {
        this.tables = tables;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Server))
            return false;
        final Server that = (Server) other;
        return this.name.equals(that.getName());
    }

    public int hashCode() {
        return name.hashCode();
    }
}
