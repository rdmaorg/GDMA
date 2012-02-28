package ie.clients.gdma.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author RGILL
 * 
 */
public class Table {

    private static Logger LOG = Logger.getLogger(Table.class);

    private Long id;

    private Long serverId;

    private String name;

    private Server server;

    private Set<User> users = new LinkedHashSet<User>();
    
    private Set<Column> columns = new LinkedHashSet<Column>();
    
    private Set<UserAccess> userAccess = new LinkedHashSet<UserAccess>();

    private boolean active;
    

    public Set<UserAccess> getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(Set<UserAccess> userAccess) {
        this.userAccess = userAccess;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Column> getColumns() {
        return columns;
    }

    public void setColumns(Set<Column> columns) {
        this.columns = columns;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Table))
            return false;
        final Table that = (Table) other;
        return this.name.equals(that.getName());
    }

    public int hashCode() {
        return name.hashCode();
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
