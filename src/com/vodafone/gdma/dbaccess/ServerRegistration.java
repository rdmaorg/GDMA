/*
 * Created on 13-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Ronan Gill
 */
public class ServerRegistration {

    private long id;

    private String name;

    private String username;

    private String password;

    private String connectionURL;

    private long odbcTypeID;

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
    public ArrayList getTables() throws ClassNotFoundException, SQLException,
            IOException, Exception {

        TableFactory tableFac = TableFactory.getInstance();
        return tableFac.getTablesForServer(id);
    }
    
    /**
     * @return Returns the tables.
     */
    public ArrayList getEditableTables() throws ClassNotFoundException, SQLException,
            IOException, Exception {

        TableFactory tableFac = TableFactory.getInstance();
        return tableFac.getEditableTablesForServer(id); 
    }

      /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(long id) {
        this.id = id;
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

}
