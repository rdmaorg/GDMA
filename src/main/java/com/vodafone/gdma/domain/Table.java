package com.vodafone.gdma.domain;

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

    private String name;

    private boolean displayed;

    private boolean allowDelete;

    private Server server;

    private Set<User> users = new LinkedHashSet<User>();

    private Set<Column> columns = new LinkedHashSet<Column>();

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

    public boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    public boolean isAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
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

}
