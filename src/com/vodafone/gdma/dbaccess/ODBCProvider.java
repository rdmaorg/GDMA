/*
 * Created on 14-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

/**
 * @author Ronan Gill
 * 
 * 14-Mar-2004
 */
public class ODBCProvider {
    private long id;
	private String name;
	private String SQLGetTables;
	private String SQLGetColumns;
	private String connectionClass;
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the sQLGetFields.
	 */
	public String getSQLGetColumns() {
		return SQLGetColumns;
	}
	/**
	 * @param getFields The sQLGetFields to set.
	 */
	public void setSQLGetColumns(String SQLGetColumns) {
		this.SQLGetColumns = SQLGetColumns;
	}
	/**
	 * @return Returns the SQLGetTables.
	 */
	public String getSQLGetTables() {
		return SQLGetTables;
	}
	/**
	 * @param getTables The sQLGetFields to set.
	 */
	public void setSQLGetTables(String SQLGetTables) {
		this.SQLGetTables = SQLGetTables;
	}
	
	public String toString(){
		StringBuffer sb =  new StringBuffer();
		sb.append("name             : ");
		sb.append(name);
		sb.append("\nSQLGetTables     : ");
		sb.append(SQLGetTables);
		sb.append("\nSQLGetColumns    : ");
		sb.append(SQLGetColumns);
		sb.append("\nConnectionClass  : ");
		sb.append(connectionClass);
		
		return sb.toString();
	}
    /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * @return Returns the connectionClass.
     */
    public String getConnectionClass() {
        return connectionClass;
    }
    /**
     * @param connectionClass The connectionClass to set.
     */
    public void setConnectionClass(String connectionClass) {
        this.connectionClass = connectionClass;
    }
}
