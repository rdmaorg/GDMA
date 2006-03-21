/*
 * Created on Mar 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Table implements Comparable {

    private static Logger logger = Logger.getLogger(Table.class);

    private Long id;

    private Long serverID;

    private String name;

    private boolean displayed;

    private boolean allowDelete;
    
    // DOES THE USER LOGGED IN HAVE ACCESS TO THIS TABLE?
    private boolean userAllowedAccess;

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

    
    
    public boolean isUserAllowedAccess()
	{
		return userAllowedAccess;
	}

	public void setUserAllowedAccess(boolean userAccess)
	{
		this.userAllowedAccess = userAccess;
	}

	/**
     * @param dropDownColumnStore
     *            The dropDownColumnStore to set.
     */
    public void setId(String id) {
        try {
            if (id == null || "".equals(id.trim()))
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
        if (name != null)
            this.name = name.trim();
        else
            this.name = null;
        ;
    }

    /**
     * @return Returns the server_id.
     */
    public Long getServerID() {
        return serverID;
    }

    /**
     * @param server_id
     *            The server_id to set.
     */
    public void setServerID(Long serverID) {
        this.serverID = serverID;
    }

    /**
     * @param dropDownColumnStore
     *            The dropDownColumnStore to set.
     */
    public void setServerID(String serverID) {
        try {
            if (serverID == null || "".equals(serverID.trim()))
                this.serverID = null;
            else
                this.serverID = new Long(serverID);
        } catch (NumberFormatException e) {
            this.serverID = null;
            logger.error(e);
            throw e;
        }
    }

    /**
     * @return Returns the allowDelete.
     */
    public boolean isAllowDelete() {
        return allowDelete;
    }

    /**
     * @return Returns the allowDelete.
     */
    public boolean isAllowInsert() {
        ArrayList columns;
        try {
            columns = getColumns();
            for (int i = 0; i < columns.size(); i++) {
                if (((Column) columns.get(i)).isAllowInsert()) return true;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    /**
     * @return Returns the allowDelete.
     */
    public boolean isAllowUpdate() {
        ArrayList columns;
        try {
            columns = getColumns();
            for (int i = 0; i < columns.size(); i++) {
                if (((Column) columns.get(i)).isAllowUpdate()) return true;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return false;
    }

    /**
     * @param allowDelete
     *            The allowDelete to set.
     */
    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    /**
     * @return Returns the displayed.
     */
    public boolean isDisplayed() {
        return displayed;
    }

    /**
     * @param displayed
     *            The displayed to set.
     */
    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id               : ");
        sb.append(id);
        sb.append("\nserver_id        : ");
        sb.append(serverID);
        sb.append("\nname             : ");
        sb.append(name);
        sb.append("\ndisplayed        : ");
        sb.append(displayed);
        sb.append("\nallowdelete      : ");
        sb.append(allowDelete);
        sb.append("\naccess      : ");
        sb.append(userAllowedAccess);

        return sb.toString();
    }

    /**
     * @return Returns the full list of columns for this table.
     */
    public ArrayList getColumns() throws Exception {
        ArrayList columns = ColumnFactory.getInstance().getList();
        ArrayList temp = new ArrayList();

        if (id == null) return temp;

        for (int i = 0; i < columns.size(); i++) {
            Column column = (Column) columns.get(i);
            if (id.equals(column.getTableID())) temp.add(columns.get(i));
        }
        return temp;
    }

    /**
     * @return Returns the columns that should be displayed for this table.
     */
    public ArrayList getDisplayedColumns() throws Exception {
        ArrayList columns = ColumnFactory.getInstance().getList();
        ArrayList temp = new ArrayList();

        if (id == null) return temp;

        for (int i = 0; i < columns.size(); i++) {
            Column column = (Column) columns.get(i);
            if (id.equals(column.getTableID()) && column.isDisplayed())
                    temp.add(columns.get(i));
        }
        return temp;
    }

    /**
     * @return Returns the columns that can be have values inserted
     */
    public ArrayList getInsertColumns() throws Exception {
        ArrayList columns = ColumnFactory.getInstance().getList();
        ArrayList temp = new ArrayList();

        if (id == null) return temp;

        for (int i = 0; i < columns.size(); i++) {
            Column column = (Column) columns.get(i);
            if (id.equals(column.getTableID()) && column.isAllowInsert()
                    && column.isDisplayed()) temp.add(columns.get(i));
        }
        return temp;
    }

    /**
     * @return Returns the columns that can be have values updated
     */
    public ArrayList getUpdateColumns() throws Exception {
        ArrayList columns = ColumnFactory.getInstance().getList();
        ArrayList temp = new ArrayList();

        if (id == null) return temp;

        for (int i = 0; i < columns.size(); i++) {
            Column column = (Column) columns.get(i);
            if (id.equals(column.getTableID()) && column.isAllowUpdate()
                    && column.isDisplayed()) temp.add(columns.get(i));
        }
        return temp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) throws ClassCastException {
        if (o == null || !(o instanceof Table)) { throw new ClassCastException(
                "Cannot compare Table with " + o.getClass().getName()); }
        return name.compareTo(((Table) o).getName());
    }

}
