/*
 * Created on 13-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author Ronan Gill
 */
public class ServerRegistration implements Comparable {

    private static Logger logger = Logger.getLogger(ServerRegistration.class);
    
    private Long id;

    private String name;

    private String username;

    private String password;

    private String connectionURL;

    private long odbcTypeID;
    
    private String prefix;

    public ServerRegistration() {

    }

    /**
     * @return Returns the connectionString.
     */
    public String getConnectionURL() {
        return connectionURL;
    }

    /**
     * @param connectionString
     *            The connectionString to set.
     */
    public void setConnectionURL(String connectionString) {
        this.connectionURL = connectionString;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return Returns the tables.
     */
    public ArrayList getTables() throws Exception {
        ArrayList list = TableFactory.getInstance().getList();
        ArrayList temp = new ArrayList();
        Table table = null;
        
        if(id == null)
            return temp;
        
        for (int i = 0; i < list.size(); i++) {
            table = (Table) list.get(i);
            if (id.equals(table.getServerID()))
                temp.add(table);
        }
        return temp;
    }

    /**
     * @return Returns the tables.
     */
    public ArrayList getDisplayedTables() throws Exception {
        ArrayList list = TableFactory.getInstance().getList();
        ArrayList temp = new ArrayList();
        Table table = null;
        for (int i = 0; i < list.size(); i++) {
            table = (Table) list.get(i);
            if (id.equals(table.getServerID()) && table.isDisplayed()) temp.add(table);
        }
        return temp;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @param id
     *            The id to set.
     */
    public void setId(String id) {
        try {
            if(id == null || "".equals(id.trim()))
                this.id = null;
            else
                this.id = new Long(id);             
        } catch (NumberFormatException e) {
            this.id = null;
            logger.error(e);
            throw e;
        }
    }     

    /**
     * @return Returns the odbcTypeID.
     */
    public long getOdbcTypeID() {
        return odbcTypeID;
    }

    /**
     * @param odbcTypeID
     *            The odbcTypeID to set.
     */
    public void setOdbcTypeID(long odbcTypeID) {
        this.odbcTypeID = odbcTypeID;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name             : ");
        sb.append(name);
        sb.append("\nusername         : ");
        sb.append(username);
        sb.append("\npassword         : ");
        sb.append(password);
        sb.append("\nconnectionString : ");
        sb.append(connectionURL);
        sb.append("\nconnectionTypeID : ");
        sb.append(odbcTypeID);

        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) throws ClassCastException {
        if (o == null || !(o instanceof ServerRegistration)) { throw new ClassCastException(
                "Cannot compare ServerRegistration with "
                        + o.getClass().getName()); }
        return name.compareTo(((ServerRegistration) o).getName());
    }
    /**
     * @return Returns the prefix.
     */
    public String getPrefix() {
        return prefix;
    }
    /**
     * @param prefix The prefix to set.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
