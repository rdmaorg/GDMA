/*
 * Created on Mar 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class TableFactory {

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
                        logger.error(e);
                        throw e;
                    }
                }
            }
        }
        return instance;
    }

    public static void main(String args[]) {
        TableFactory o;
        try {
            o = TableFactory.getInstance();
            ArrayList a = o.getTables();
            for (int i = 0; i < a.size(); i++)
                logger.debug(((Table) a.get(i)).getName());
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private ArrayList tables;

    private TableFactory() throws ClassNotFoundException, SQLException,
            IOException, Exception {
        buildTablesList();
    }

    public synchronized void buildTablesList() throws ClassNotFoundException,
            SQLException, IOException, Exception {
        // Create the TreeMap whcih will hold the Providers
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        // Create the TreeMap whcih will hold the ServerRegistrations
        tables = new ArrayList();

        try {
            con = Connection.getConnection();
            stmt = con.createStatement();
            rs = stmt
                    .executeQuery("SELECT id, server_id, name, editable FROM dbo.gdma_table order by name");

            while (rs != null && rs.next()) {
                Table table = new Table();

                table.setId(rs.getLong("id"));
                table.setServerID(rs.getLong("server_id"));
                table.setName(rs.getString("name"));
                table.setEditable("Y".equals(rs.getString("editable")));

                //                logger.debug(odbc.toString());
                tables.add(table);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }

    }

    public Table getTable(long id) {
        for (int i = 0; i <= tables.size(); i++) {
            if (((Table) tables.get(i)).getId() == id)
                    return (Table) tables.get(i);
        }
        return null;
    }

    public ArrayList getTables() {
        return tables;
    }

    public ArrayList getTablesForServer(long serverId) {
        ArrayList temp = new ArrayList();

        for (int i = 0; i < tables.size(); i++) {
            if (((Table) tables.get(i)).getServerID() == serverId)
                    temp.add(tables.get(i));
        }
        return temp;
    }

    public ArrayList getEditableTablesForServer(long serverId) {
        ArrayList temp = new ArrayList();

        for (int i = 0; i < tables.size(); i++) {
            if (((Table) tables.get(i)).getServerID() == serverId
                    && ((Table) tables.get(i)).isEditable())
                    temp.add(tables.get(i));
        }
        return temp;
    }

    public void addTable(long serverID, String name, boolean editable)
            throws Exception {

        java.sql.Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("INSERT INTO gdma_table  ");
        sbInsert.append("(server_id,name,editable) VALUES (");
        sbInsert.append(serverID);
        sbInsert.append(",'");
        sbInsert.append(name);
        sbInsert.append("','");
        sbInsert.append(editable ? 'Y' : 'N');
        sbInsert.append("')");

        try {
            con = Connection.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) con.close();
        }

        //now refresh our local list
        buildTablesList();

    }

    public void updateTable(long id, long serverID, String name,
            boolean editable) throws Exception {

        java.sql.Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("UPDATE gdma_table SET server_id=");
        sbInsert.append(serverID);
        sbInsert.append(",name='");
        sbInsert.append(name);
        sbInsert.append("',editable='");
        sbInsert.append(editable ? 'Y' : 'N');
        sbInsert.append("' WHERE id =");
        sbInsert.append(id);

        try {
            con = Connection.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (con != null) con.close();
        }

        buildTablesList();
    }
}
