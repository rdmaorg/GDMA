/*
 * Created on Mar 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author RGILL
 * 
 * This class is used to create and retrieve Table records in the database.
 * The list is held im memory to speed up the application.
 * Uses a singleton pattern
 * 
 */
public class TableFactory extends DBFactory {

    private static TableFactory instance = null;

    // Log4j logger
    private static Logger logger = Logger.getLogger(TableFactory.class);

    public static TableFactory getInstance() throws ClassNotFoundException,
            SQLException, IOException, Exception {
        if (instance == null) {
            synchronized (ODBCProviderFactory.class) {
                if (instance == null) {
                    try {
                        instance = new TableFactory();
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                        throw e;
                    }
                }
            }
        }
        return instance;
    }

    private TableFactory() throws Exception {
        buildList();
    }

    public Table getTable(Long id) {
        Table table = null;
        
        if(id == null)
            return null;
        
        for (int i = 0; i <= list.size(); i++) {
            table = (Table) list.get(i);
            if (id.equals(table.getId())) return table;
        }
        return null;
    }

    protected synchronized void buildList() throws ClassNotFoundException,
            SQLException, IOException, Exception {
        // Create the TreeMap whcih will hold the Providers
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "select * from gdma_table order by name";
        // Create the TreeMap whcih will hold the ServerRegistrations
        list = new ArrayList();

        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs != null && rs.next()) {
                Table table = new Table();

                table.setId(new Long(rs.getLong("id")));
                table.setServerID(new Long(rs.getLong("server_id")));
                table.setName(rs.getString("name"));
                table.setDisplayed("Y".equals(rs.getString("displayed")));
                table.setAllowDelete("Y".equals(rs.getString("allowdelete")));
                list.add(table);
            }
        } catch (Exception e) {
            logger.error(e);
            throw e;
        } finally {
            closeAll(con, stmt, rs);
        }

    }

    public void save(Table table) throws Exception {
        if(table.getId() == null)
            addTable(table);    //new table
        else
            updateTable(table); //old table 
    }
    
    private void addTable(Table table) throws Exception {

        Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("INSERT INTO gdma_table  ");
        sbInsert.append("(server_id,name,displayed,allowdelete) VALUES (");
        sbInsert.append(table.getServerID());
        sbInsert.append(",'");
        sbInsert.append(table.getName());
        sbInsert.append("','");
        sbInsert.append(table.isDisplayed() ? 'Y' : 'N');
        sbInsert.append("','");
        sbInsert.append(table.isAllowDelete() ? 'Y' : 'N');
        sbInsert.append("')");

        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw e;
        } finally {
            closeAll(con, stmt, null);
        }
    }

    private void updateTable(Table table) throws Exception {

        Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("UPDATE gdma_table SET server_id=");
        sbInsert.append(table.getServerID());
        sbInsert.append(",name='");
        sbInsert.append(table.getName());
        sbInsert.append("',displayed='");
        sbInsert.append(table.isDisplayed() ? 'Y' : 'N');
        sbInsert.append("',allowdelete='");
        sbInsert.append(table.isAllowDelete() ? 'Y' : 'N');
        sbInsert.append("' WHERE id =");
        sbInsert.append(table.getId());
        logger.debug(sbInsert); 
        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
        } catch (Exception e) {
            logger.error("Exception while calling [updateTable]", e);
            throw e;
        } finally {
            closeAll(con, stmt, null);
        }
    }
    
    public void hideAll(Long lngServerID) throws Exception {
        Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("UPDATE gdma_table SET ");
        sbInsert.append(" displayed='N',");
        sbInsert.append(" allowdelete='N'");
        sbInsert.append(" WHERE server_id =");
        sbInsert.append(lngServerID);

        logger.debug(sbInsert);     

        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
        } catch (Exception e) {
            logger.error("Exception while calling [hideAll]", e);
            throw e;
        } finally {
            closeAll(con, stmt, null);
        }
    }
}
