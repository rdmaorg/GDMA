/*
 * Created on Mar 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Table {

    private long id;

    private long serverID;

    private String name;

    private boolean editable;

    /**
     * @return Returns the editable.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable
     *            The editable to set.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
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
     * @return Returns the server_id.
     */
    public long getServerID() {
        return serverID;
    }

    /**
     * @param server_id
     *            The server_id to set.
     */
    public void setServerID(long server_id) {
        this.serverID = server_id;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id             : ");
        sb.append(id);
        sb.append("\nserver_id             : ");
        sb.append(serverID);
        sb.append("\nname             : ");
        sb.append(name);
        sb.append("\neditable         : ");
        sb.append(editable);

        return sb.toString();
    }

    /**
     * @return Returns the full list of columns for this table.
     */
    public ArrayList getColumns() throws ClassNotFoundException, SQLException,
            IOException, Exception {
        ArrayList columns =  ColumnFactory.getInstance().getColumns();
        ArrayList temp = new ArrayList();

        for (int i = 0; i < columns.size(); i++) {
            Column column = (Column) columns.get(i);
            if (column.getTableID().longValue() == id   )
                    temp.add(columns.get(i));
        }
        return temp;
    }
    
    
    /**
     * @return Returns the columns that should be displayed for this table.
     */
    public ArrayList getIncludedColumns() throws ClassNotFoundException, SQLException,
            IOException, Exception {
        ArrayList columns =  ColumnFactory.getInstance().getColumns();
        ArrayList temp = new ArrayList();

        for (int i = 0; i < columns.size(); i++) {
            Column column = (Column) columns.get(i);
            if (column.getTableID().longValue() == id && column.isIncluded()  )
                    temp.add(columns.get(i));
        }
        return temp;
    } 
    
    /**
     * @return Returns the columns that are editable for this table.
     */
    public ArrayList getEditableColumns() throws ClassNotFoundException, SQLException,
            IOException, Exception {
        ArrayList columns =  ColumnFactory.getInstance().getColumns();
        ArrayList temp = new ArrayList();

        for (int i = 0; i < columns.size(); i++) {
            Column column = (Column) columns.get(i);
            if (column.getTableID().longValue() == id && column.isIncluded() && column.isEditable() )
                    temp.add(columns.get(i));
        }
        return temp;
    }      
}
