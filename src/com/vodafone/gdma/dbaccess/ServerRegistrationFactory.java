/*
 * Created on 14-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author Ronan Gill
 * 
 * 14-Mar-2004
 */
public class ServerRegistrationFactory {

    // Log4j logger
    private static Logger logger = Logger
            .getLogger("ServerRegistrationFactory");

    private static ServerRegistrationFactory instance = null;

    private ArrayList serverRegistrations;

    //for testing only
    public static void main(String args[]) {
        ServerRegistrationFactory o;
        try {
            o = ServerRegistrationFactory.getInstance();
            ArrayList a = o.getServerRegistrations();
            for (int i = 0; i < a.size(); i++)
                logger.debug(((ServerRegistration) a.get(i)).getName());
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static ServerRegistrationFactory getInstance() throws Exception {

        if (instance == null) {
            synchronized (ServerRegistrationFactory.class) {
                if (instance == null) {
                    try {
                        instance = new ServerRegistrationFactory();
                    } catch (Exception e) {
                        logger.error(e);
                        throw e;
                    }
                }
            }
        }
        return instance;
    }

    private ServerRegistrationFactory() throws Exception {
        buildServerRegistrationsList();
    }

    public ArrayList getServerRegistrations() {
        return serverRegistrations;
    }

    public ServerRegistration getServerRegistration(long id) {

        for (int i = 0; i < serverRegistrations.size(); i++) {
            if (id == ((ServerRegistration) serverRegistrations.get(i)).getId()) { return (ServerRegistration) serverRegistrations
                    .get(i); }
        }
        return null;
    }

    private synchronized void buildServerRegistrationsList() throws Exception {
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        // Create the TreeMap whcih will hold the ServerRegistrations
        serverRegistrations = new ArrayList();

        try {
            con = Connection.getConnection();
            stmt = con.createStatement();
            rs = stmt
                    .executeQuery("SELECT id, odbc_type_id, name, username, password, url FROM dbo.gdma_server_registration ORDER BY name");

            while (rs != null && rs.next()) {
                ServerRegistration reg = new ServerRegistration();

                reg.setId(rs.getLong("id"));
                reg.setOdbcTypeID(rs.getLong("odbc_type_id"));
                reg.setName(rs.getString("name"));
                reg.setUsername(rs.getString("username"));
                reg.setPassword(rs.getString("password"));
                reg.setConnectionURL(rs.getString("url"));

                serverRegistrations.add(reg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }
    }

    public void addServerRegistration(String name, String ODBCTypeID,
            String username, String password, String url) throws Exception {

        java.sql.Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("INSERT INTO gdma_server_registration (odbc_type_id,");
        sbInsert.append("name,username,password, url) VALUES (");
        sbInsert.append(ODBCTypeID);
        sbInsert.append(",'");
        sbInsert.append(name);
        sbInsert.append("','");
        sbInsert.append(username);
        sbInsert.append("','");
        sbInsert.append(password == null ? "" : password);
        sbInsert.append("','");
        sbInsert.append(url);
        sbInsert.append("')");

        try {
            con = Connection.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }

        //now refresh our local list
        buildServerRegistrationsList();

    }

    public void updateServerRegistration(long id, String name,
            String ODBCTypeID, String username, String password, String url)
            throws Exception {

        java.sql.Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("UPDATE gdma_server_registration SET odbc_type_id=");
        sbInsert.append(ODBCTypeID);
        sbInsert.append(",name='");
        sbInsert.append(name);
        sbInsert.append("',username='");
        sbInsert.append(username);
        sbInsert.append("',password='");
        sbInsert.append(password == null ? "" : password);
        sbInsert.append("', url ='");
        sbInsert.append(url);
        sbInsert.append("' WHERE id =");
        sbInsert.append(id);

        try {
            con = Connection.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }

        // now change our refernce to the object
        ServerRegistration reg = getServerRegistration(id);
        reg.setName(name);
        buildServerRegistrationsList();
    }

    public void deleteServerRegistration(int id) throws Exception {

        java.sql.Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("DELETE FROM gdma_server_registration WHERE id =");
        sbInsert.append(id);

        try {
            con = Connection.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }

        //      now refresh our local list
        buildServerRegistrationsList();
    }

    public ArrayList getTablesFromDB(ServerRegistration reg) throws Exception {
        ODBCProvider odbc = ODBCProviderFactory.getInstance().getODBCProvider(
                reg.getOdbcTypeID());
        ArrayList tables = new ArrayList();
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = Connection.getConnection(odbc.getConnectionClass(), reg
                    .getUsername(), reg.getPassword(), reg.getConnectionURL());
            stmt = con.createStatement();

            rs = stmt.executeQuery(odbc.getSQLGetTables());

            while (rs != null && rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }

        return tables;
    }

    public ArrayList getColumnsFromDB(ServerRegistration reg, String tableName)
            throws Exception {
        ODBCProvider odbc = ODBCProviderFactory.getInstance().getODBCProvider(
                reg.getOdbcTypeID());
        ArrayList columns = new ArrayList();
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsMeta = null;
        Column column = null;

        try {
            con = Connection.getConnection(odbc.getConnectionClass(), reg
                    .getUsername(), reg.getPassword(), reg.getConnectionURL());
            stmt = con.createStatement();
            logger.debug("select * from " + tableName + " where 1 = 0");
            rs = stmt.executeQuery("select * from " + tableName
                    + " where 1 = 0");

            rsMeta = rs.getMetaData();
            for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
                column = new Column();
                column.setName(rsMeta.getColumnName(i));
                column.setColumnType(rsMeta.getColumnType(i));
                column.setColumnTypeString(rsMeta.getColumnTypeName(i));
                column.setEditable(rsMeta.isWritable(i));
                column.setNullable(rsMeta.isNullable(i) == ResultSetMetaData.columnNullable || rsMeta.isNullable(i) == ResultSetMetaData.columnNullableUnknown );
                columns.add(column);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }

        return columns;
    }
}

