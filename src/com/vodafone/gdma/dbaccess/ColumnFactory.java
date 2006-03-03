package com.vodafone.gdma.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author RGILL
 * This class is used to create and retrieve Column records in the database.
 * The list of columns is held im memory to speed up the application.
 * Uses a singleton pattern
 * 
 */
public class ColumnFactory extends DBFactory {

    // Log4j logger
    private static Logger logger = Logger.getLogger(ColumnFactory.class);

    private static ColumnFactory instance = null;

    
    public static ColumnFactory getInstance() throws Exception {
        if (instance == null) {
            //need to make sure we only ever get back the one object
            synchronized (ODBCProviderFactory.class) {
                if (instance == null) {
                    try {
                        instance = new ColumnFactory();
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                        throw e;
                    }
                }
            }
        }
        return instance;
    }

    private ColumnFactory() throws Exception {
        buildList();
    }

    public Column getColumn(long id) {
        Column column = null;
        for (int i = 0; i < list.size(); i++) {
            column = (Column) list.get(i);
            if (column.getId().longValue() == id) return column;
        }
        return null;
    }

    public synchronized void buildList() throws Exception {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        Column column = null;
        String query = "SELECT * FROM gmda_column ORDER BY id";
        // Create the ArrayList which will hold the Columns
        list = new ArrayList();
        long temp;

        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs != null && rs.next()) {
                column = new Column();

                column.setId(new Long(rs.getLong("id")));
                column.setTableID(new Long(rs.getLong("table_id")));
                column.setName(rs.getString("name"));
                column.setColumnType(rs.getInt("type"));
                column.setColumnTypeString(rs.getString("typeString"));
                
                temp = rs.getLong("dd_lookup_column_display");               
                if(rs.wasNull())
                    column.setDropDownColumnDisplay((Long)null);
                else
                    column.setDropDownColumnDisplay(new Long(temp));
                
                temp = rs.getLong("dd_lookup_column_store");                
                if(rs.wasNull())
                    column.setDropDownColumnStore((Long)null);
                else
                    column.setDropDownColumnStore(new Long(temp));                 
                
                column.setDisplayed("Y".equals(rs.getString("displayed")));
                column.setAllowInsert("Y".equals(rs.getString("allowinsert")));
                column.setAllowUpdate("Y".equals(rs.getString("allowupdate")));
                column.setNullable("Y".equals(rs.getString("nullable")));
                column.setSpecial(rs.getString("special"));
                list.add(column);
            }
        }catch (Exception e) {
            logger.error(e.getMessage(),e);  
            throw e;
        }  finally {
            closeAll(con, stmt, rs);
        }

    }
    public void save(Column col) throws Exception {
        if(col.getId() == null)
            addColumn(col);    //new column
        else
            updateColumn(col); //old column 
    }
    
    private void addColumn(Column col) throws Exception {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DBUtil.getConnection();
            stmt = con
                    .prepareStatement("INSERT INTO gmda_column (table_id,name,type,dd_lookup_column_display,dd_lookup_column_store,displayed,allowinsert,allowupdate,nullable,special) VALUES  (?,?,?,?,?,?,?,?,?,?)");
            stmt.setLong(1, col.getTableID().longValue());
            stmt.setString(2, col.getName());
            stmt.setInt(3, col.getColumnType());
            if (col.getDropDownColumnStore() != null)
                stmt.setLong(4, col.getDropDownColumnStore().longValue());
            else
                stmt.setNull(4, Types.NUMERIC);
            if (col.getDropDownColumnDisplay() != null)
                stmt.setLong(5, col.getDropDownColumnDisplay().longValue());
            else
                stmt.setNull(5, Types.NUMERIC);
            stmt.setString(6, col.isDisplayed() ? "Y" : "N");
            stmt.setString(7, col.isAllowInsert() ? "Y" : "N");
            stmt.setString(8, col.isAllowUpdate() ? "Y" : "N");
            stmt.setString(9, col.isNullable() ? "Y" : "N");
            stmt.setString(10, col.getSpecial());
            stmt.executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);  
            throw e;
        } finally {
            closeAll(con, stmt, null);
        }
    }

    private void updateColumn(Column col) throws Exception {

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = DBUtil.getConnection();
            stmt = con
                    .prepareStatement("UPDATE gmda_column SET table_id=?,name=?,type=?,dd_lookup_column_display=?,dd_lookup_column_store=?"
                            + ",displayed=?,allowinsert=?,allowupdate=?,nullable=?,special=? WHERE id =?");
            stmt.setLong(1, col.getTableID().longValue());
            stmt.setString(2, col.getName());
            stmt.setInt(3, col.getColumnType());
            if (col.getDropDownColumnDisplay() != null)
                stmt.setLong(4, col.getDropDownColumnDisplay().longValue());
            else
                stmt.setNull(4, Types.NUMERIC);
            
            if (col.getDropDownColumnStore() != null)
                stmt.setLong(5, col.getDropDownColumnStore().longValue());
            else
                stmt.setNull(5, Types.NUMERIC);
           
            stmt.setString(6, col.isDisplayed() ? "Y" : "N");
            stmt.setString(7, col.isAllowInsert() ? "Y" : "N");
            stmt.setString(8, col.isAllowUpdate() ? "Y" : "N");
            stmt.setString(9, col.isNullable() ? "Y" : "N");           
            stmt.setString(10, col.getSpecial());
            stmt.setLong(11, col.getId().longValue());
            stmt.executeUpdate();

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        } finally {
            closeAll(con, stmt, null);
        }
    }
    /*
     * This method is used to 'remove' the current records for columns.
     * The record are actually reset rather than actually deleting them
     * 
     */
    public void hideAll(Long lngTableID) throws Exception {
        Connection con = null;
        PreparedStatement stmt = null;

        try {

            con = DBUtil.getConnection();
            stmt = con
                    .prepareStatement("UPDATE gmda_column SET dd_lookup_column_display=?,dd_lookup_column_store=?,displayed=?,allowinsert=?,allowupdate=?,nullable=?,special=? WHERE table_id =?");
            stmt.setNull(1, Types.NUMERIC); //dd_lookup_column_display
            stmt.setNull(2, Types.NUMERIC); //dd_lookup_column_store=
            stmt.setString(3, "N"); //displayed
            stmt.setString(4, "N"); //allowinsert
            stmt.setString(5, "N"); //allowupdate
            stmt.setString(6, "N"); //nullable
            stmt.setString(7, "N"); //special
            stmt.setLong(8, lngTableID.longValue()); //table_id

           // System.out.println("STMT1 : UPDATE gmda_column SET dd_lookup_column_display=?," +
           // 		"dd_lookup_column_store=?," +
           // 		"displayed=?," +
           // 		"allowinsert=?," +
           // 		"allowupdate=?," +
           // 		"nullable=?," +
           // 		"special=? " +
           //		"WHERE table_id =?");                  
            
            stmt.executeUpdate();
            
        }
        catch (Exception e) 
        {
            logger.error(e.getMessage(),e);
            throw e;
        } finally {
            closeAll(con, stmt, null);
        }
    }
}
