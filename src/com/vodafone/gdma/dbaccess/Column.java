/*
 * Created on Mar 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.sql.Types;

import org.apache.log4j.Logger;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Column implements Comparable {

    private static Logger logger = Logger.getLogger(Column.class);

    private Long id;

    private Long tableID;

    private String name;

    private int columnType;
    
    private String columnTypeString;

    private Long dropDownColumnDisplay;

    private Long dropDownColumnStore;

    private boolean displayed;

    private boolean allowInsert;
    
    private boolean allowUpdate;

    private boolean nullable;

    /**
     * @return Returns the dropDownColumnDisplay.
     */
    public Long getDropDownColumnDisplay() {
        return dropDownColumnDisplay;
    }

    /**
     * @param dropDownColumnDisplay
     *            The dropDownColumnDisplay to set.
     */
    public void setDropDownColumnDisplay(Long dropDownColumnDisplay) {
        this.dropDownColumnDisplay = dropDownColumnDisplay;
    }

    /**
     * @param dropDownColumnDisplay
     *            The dropDownColumnDisplay to set.
     */
    public void setDropDownColumnDisplay(String dropDownColumnDisplay) {
        try {
            if(dropDownColumnDisplay == null || "".equals(dropDownColumnDisplay.trim()))
                this.dropDownColumnDisplay = null;
            else
                this.dropDownColumnDisplay = new Long(dropDownColumnDisplay);
        } catch (NumberFormatException e) {
            this.dropDownColumnDisplay = null;
            logger.error(e);
        }
    }

    /**
     * @return Returns the dropDownColumnStore.
     */
    public Long getDropDownColumnStore() {
        return dropDownColumnStore;
    }

    /**
     * @param dropDownColumnStore
     *            The dropDownColumnStore to set.
     */
    public void setDropDownColumnStore(Long dropDownColumnStore) {
        this.dropDownColumnStore = dropDownColumnStore;
    }

    /**
     * @param dropDownColumnStore
     *            The dropDownColumnStore to set.
     */
    public void setDropDownColumnStore(String dropDownColumnStore) {
        try {
            if(dropDownColumnStore == null || "".equals(dropDownColumnStore.trim()))
                this.dropDownColumnStore = null;
            else
                this.dropDownColumnStore = new Long(dropDownColumnStore);            
        } catch (NumberFormatException e) {
            this.dropDownColumnStore = null;
            logger.error(e);
            throw e;
        }
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
     * @param dropDownColumnStore
     *            The dropDownColumnStore to set.
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
     * @return Returns the table_id.
     */
    public Long getTableID() {
        return tableID;
    }

    /**
     * @param table_id
     *            The table_id to set.
     */
    public void setTableID(Long tableID) {
        this.tableID = tableID;
    }

    /**
     * @return Returns the columnType.
     */
    public int getColumnType() {
        return columnType;
    }

    /**
     * @param columnType
     *            The columnType to set.
     */
    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }
    
    /**
     * @param dropDownColumnStore
     *            The dropDownColumnStore to set.
     */
    public void setColumnType(String columnType) {
        try {
            if(columnType == null || "".equals(columnType.trim()))
                this.columnType = Types.VARCHAR;
            else
                this.columnType = Integer.parseInt(columnType);             
        } catch (NumberFormatException e) {
            this.columnType = Types.VARCHAR;
            logger.error(e);
            throw e;
        }
    }       

    /**
     * @return Returns the displayed.
     */
    public boolean isDisplayed() {
        return displayed;
    }
    /**
     * @param displayed The displayed to set.
     */
    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    /**
     * @return Returns the allowInsert.
     */
    public boolean isAllowInsert() {
        return allowInsert;
    }
    /**
     * @param allowInsert The allowInsert to set.
     */
    public void setAllowInsert(boolean allowInsert) {
        this.allowInsert = allowInsert;
    }
    /**
     * @return Returns the allowUpdate.
     */
    public boolean isAllowUpdate() {
        return allowUpdate;
    }
    /**
     * @param allowUpdate The allowUpdate to set.
     */
    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    /**
     * @return Returns the nullable.
     */
    public boolean isNullable() {
        return nullable;
    }

    
    /**
     * @param nullable
     *            The nullable to set.
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    /**
     * @return Returns the columnTypeString.
     */
    public String getColumnTypeString() {
        return columnTypeString;
    }
    /**
     * @param columnTypeString The columnTypeString to set.
     */
    public void setColumnTypeString(String columnTypeString) {
        this.columnTypeString = columnTypeString;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id                    : ");
        sb.append(id);
        sb.append("\ntable_id              : ");
        sb.append(tableID);
        sb.append("\nname                  : ");
        sb.append(name);
        sb.append("\ntype                  : ");
        sb.append(columnType);
        sb.append("\ntypeString            : ");
        sb.append(columnTypeString);
        sb.append("\ndropDownColumnDisplay : ");
        sb.append(dropDownColumnDisplay);
        sb.append("\ndropDownColumnStore   : ");
        sb.append(dropDownColumnStore);
        sb.append("\nallowInsert           : ");
        sb.append(allowInsert);
        sb.append("\nallowUpdate           : ");
        sb.append(allowUpdate);        
        sb.append("\ndisplayed             : ");
        sb.append(displayed );
        sb.append("\nnullable              : ");
        sb.append(nullable);        
        return sb.toString();
    }    
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) throws ClassCastException {
        if (o == null || !(o instanceof Column)) { throw new ClassCastException(
                "Cannot compare Column with "
                        + o.getClass().getName()); }
        return name.compareTo(((Column) o).getName());
    }
}
