/*
 * Created on Mar 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ColumnFactory {

    // Log4j logger
    private static Logger logger = Logger.getLogger(ColumnFactory.class);

    private static ColumnFactory instance = null;

    private ArrayList columns;

    public static void main(String args[]) {
        ColumnFactory o;
        try {
            o = ColumnFactory.getInstance();
            ArrayList a = o.getColumns(); 
            for (int i = 0; i < a.size(); i++)
                logger.debug(((Column) a.get(i)).getName());
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static ColumnFactory getInstance() throws ClassNotFoundException,
            SQLException, IOException, Exception {
        if (instance == null) {
            synchronized (ODBCProviderFactory.class) {
                if (instance == null) {
                    try {
                        instance = new ColumnFactory();
                    } catch (Exception e) {
                        logger.error(e);
                        throw e;
                    }
                }
            }
        }
        return instance;
    }

    private ColumnFactory() throws ClassNotFoundException, SQLException,
            IOException, Exception {
        buildColumnsList();
    }

    public ArrayList getColumns() {
        return columns;
    }

    public Column getColumn(long id) {
        for (int i = 0; i < columns.size(); i++) {
            if (((Column) columns.get(i)).getId().longValue() == id)
                    return (Column) columns.get(i);
        }
        return null;
    }

    public synchronized void buildColumnsList() throws ClassNotFoundException,
            SQLException, IOException, Exception {
        // Create the TreeMap whcih will hold the Providers
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        // Create the TreeMap whcih will hold the ServerRegistrations
        columns = new ArrayList();

        try {
            con = Connection.getConnection();
            stmt = con.createStatement();
            rs = stmt
                    .executeQuery("SELECT id, table_id, name, type, typeString, dd_lookup_column_display, dd_lookup_column_store, editable, included, nullable FROM gmda_column ORDER BY id");

            while (rs != null && rs.next()) {
                Column column = new Column();

                column.setId(new Long(rs.getLong("id")));
                column.setTableID(new Long(rs.getLong("table_id")));
                column.setName(rs.getString("name"));
                column.setColumnType(rs.getInt("type"));
                column.setColumnTypeString(rs.getString("typeString"));
                column.setDropDownColumnDisplay(new Long(rs.getLong("dd_lookup_column_display")));
                column.setDropDownColumnStore(new Long(rs.getLong("dd_lookup_column_store")));
                column.setEditable("Y".equals(rs.getString("editable")));
                column.setIncluded("Y".equals(rs.getString("included")));
                column.setNullable("Y".equals(rs.getString("nullable")));
                columns.add(column);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }

    }

    public void addColumn(Column col) throws Exception {

        java.sql.Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = Connection.getConnection();
            stmt = con.prepareStatement("INSERT INTO gmda_column (table_id,name,type,dd_lookup_column_display," +
                    "dd_lookup_column_store,editable,included,nullable) VALUES  (?,?,?,?,?,?,?,?)");
            stmt.setLong(1,col.getTableID().longValue());
            stmt.setString(2,col.getName());
            stmt.setInt(3,col.getColumnType());
            if(col.getDropDownColumnStore() != null)
                stmt.setLong(4,col.getDropDownColumnStore().longValue());
            else
                stmt.setNull(4,Types.NUMERIC);
            if(col.getDropDownColumnDisplay() != null)
                stmt.setLong(5,col.getDropDownColumnDisplay().longValue());
            else
                stmt.setNull(5,Types.NUMERIC);            
            stmt.setString(6,col.isEditable() ? "Y":"N");
            stmt.setString(7,col.isIncluded() ? "Y":"N");
            stmt.setString(8,col.isNullable() ? "Y":"N");
           
            stmt.executeUpdate();
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) con.close();
        }

        //now refresh our local list
        buildColumnsList();

    }

    public void updateColumn(Column col) throws Exception {

        java.sql.Connection con = null;
        PreparedStatement stmt = null;
        
        try {
            con = Connection.getConnection();
            stmt = con.prepareStatement("UPDATE gmda_column SET table_id=?,name=?,type=?,dd_lookup_column_display=?,dd_lookup_column_store=?" +
            		",editable=?, included =?, nullable=? WHERE id =?");
            stmt.setLong(1,col.getTableID().longValue());
            stmt.setString(2,col.getName());
            stmt.setInt(3,col.getColumnType());
            if(col.getDropDownColumnStore() != null)
                stmt.setLong(4,col.getDropDownColumnStore().longValue());
            else
                stmt.setNull(4,Types.NUMERIC);
            if(col.getDropDownColumnStore() != null)
                stmt.setLong(5,col.getDropDownColumnDisplay().longValue());
            else
                stmt.setNull(5,Types.NUMERIC);            
            stmt.setString(6,col.isEditable() ? "Y":"N");
            stmt.setString(7,col.isIncluded() ? "Y":"N");
            stmt.setString(8,col.isNullable() ? "Y":"N");
            stmt.setLong(9,col.getId().longValue());
            stmt.executeUpdate();
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) con.close();
        }

        buildColumnsList();
    }
}
