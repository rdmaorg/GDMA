package com.vodafone.gdma.dbaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author Ronan Gill
 * 
 * This class is used to create and retrieve Server Registration records in the database.
 * The list is held im memory to speed up the application.
 * Uses a singleton pattern
 * 
 */
public class ServerRegistrationFactory extends DBFactory{

    // Log4j logger
    protected static Logger logger = Logger
            .getLogger(ServerRegistrationFactory.class);

    private static ServerRegistrationFactory instance = null;

    public static ServerRegistrationFactory getInstance() throws Exception {
        if (instance == null) {
            synchronized (ServerRegistrationFactory.class) {
                if (instance == null) {
                    try {
                        instance = new ServerRegistrationFactory();
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                        throw e;
                    }
                }
            }
        }
        return instance;
    }

    private ServerRegistrationFactory() throws Exception {
        buildList();
    }

    public ServerRegistration getServerRegistration(Long id) {
        ServerRegistration reg = null;
        if(id == null)
            return null;
        for (int i = 0; i < list.size(); i++) {
            reg = (ServerRegistration) list.get(i);
            if (id.equals(reg.getId())) 
                return reg;
        }
        return null;
    }

    /**
     *
     * Implemention of the buildList method. Generate an ArrayList
     * of objects from the database. Must be synchronized
     * if the object is to be a singleton.
     * 
     * @see com. com.vodafone.gdma.dbaccess.DBFactory#buildList()
     */
    protected synchronized void buildList() throws Exception {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ServerRegistration reg = null;
        String query = "SELECT * FROM gdma_server_registration ORDER BY name";

        // Create the ArrayList which will hold the ServerRegistrations
        list = new ArrayList();

        try {
            con = DBUtil.getConnection();

            stmt = con.createStatement();
            
            rs = stmt.executeQuery(query);
           
            while (rs != null && rs.next()) 
            {
                reg = new ServerRegistration();

                reg.setId(new Long(rs.getLong("id")));
                reg.setOdbcTypeID(rs.getLong("odbc_type_id"));
                reg.setName(rs.getString("name"));
                reg.setUsername(rs.getString("username"));
                reg.setPassword(rs.getString("password"));
                reg.setConnectionURL(rs.getString("url"));
                reg.setPrefix(rs.getString("prefix"));
                if(rs.wasNull())
                    reg.setPrefix(null);

                list.add(reg);
                
                logger.debug(" ServerRegistration :: \n"+reg);
                
            }
        }
        catch (SQLException e) 
        {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
            throw e;
        }
        catch (Exception e) 
        {
            logger.error(e.getMessage(),e);
            throw e;
        }
        finally 
        {
            closeAll(con, stmt, null);
        }
    }

    public void addServerRegistration(ServerRegistration reg) throws Exception {
        Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("INSERT INTO gdma_server_registration (odbc_type_id,");
        sbInsert.append("name,username,password, url, prefix) VALUES (");
        sbInsert.append(reg.getOdbcTypeID());
        sbInsert.append(",'");
        sbInsert.append(reg.getName());
        sbInsert.append("','");
        sbInsert.append(reg.getUsername());
        sbInsert.append("','");
        sbInsert.append(reg.getPassword() == null ? "" : reg.getPassword());
        sbInsert.append("','");
        sbInsert.append(reg.getConnectionURL());
        sbInsert.append("','");
        sbInsert.append(reg.getPrefix());
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
        
        buildList();
    }

    public void updateServerRegistration(ServerRegistration reg)
            throws Exception {

        Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("UPDATE gdma_server_registration SET odbc_type_id=");
        sbInsert.append(reg.getOdbcTypeID());
        sbInsert.append(",name='");
        sbInsert.append(reg.getName());
        sbInsert.append("',username='");
        sbInsert.append(reg.getUsername());
        sbInsert.append("',password='");
        sbInsert.append(reg.getPassword() == null ? "" : reg.getPassword());
        sbInsert.append("', url ='");
        sbInsert.append(reg.getConnectionURL());
        sbInsert.append("', prefix ='");
        sbInsert.append(reg.getPrefix());
        sbInsert.append("' WHERE id =");
        sbInsert.append(reg.getId());

        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeAll(con, stmt, null);
        }

        // now change our refernce to the object
        buildList();
    }

    public void deleteServerRegistration(int id) throws Exception {
        //TODO: put checks here for tables hanging off DB
        Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("DELETE FROM gdma_server_registration WHERE id =");
        sbInsert.append(id);

        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeAll(con, stmt, null);
        }

        //      now refresh our local list
        buildList();
    }

    public ArrayList getTablesFromDB(ServerRegistration reg) throws Exception 
    {
        ODBCProvider odbc = ODBCProviderFactory.getInstance().getODBCProvider(reg.getOdbcTypeID());
    
        ArrayList tables = new ArrayList();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String table;

        try 
        {        	
        	
            con = DBUtil.getConnection(odbc.getConnectionClass(), reg.getUsername(), reg.getPassword(), reg.getConnectionURL());
            
            stmt = con.createStatement();
             
            rs = stmt.executeQuery(odbc.getSQLGetTables());

            while (rs != null && rs.next()) {
                table = rs.getString("TABLE_NAME");
                if(table != null)
                    tables.add(table.trim());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeAll(con, stmt, rs);
        }

        return tables;
    }

    public ArrayList getColumnsFromDB(ServerRegistration reg, String tableName)
            throws Exception {
        ODBCProvider odbc = ODBCProviderFactory.getInstance().getODBCProvider(reg.getOdbcTypeID());
        ArrayList columns = new ArrayList();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsMeta = null;
        Column column = null;
        String columnName = null;
        String quotedIdentifer = "";
        StringBuffer sbQuery = new StringBuffer();

        try {
            con = DBUtil.getConnection(odbc.getConnectionClass(), reg
                    .getUsername(), reg.getPassword(), reg.getConnectionURL());
            stmt = con.createStatement();
            quotedIdentifer = con.getMetaData().getIdentifierQuoteString();

            sbQuery.append("select * from ");
            
           // SOCO commented out:
           // sbQuery.append(quotedIdentifer);// original
            
            sbQuery.append(reg.getPrefix());
            
           // SOCO commented out:            
           // sbQuery.append(quotedIdentifer);// original
            
            sbQuery.append(".");
            sbQuery.append(quotedIdentifer);
            sbQuery.append(tableName);
            sbQuery.append(quotedIdentifer);
            
           // SOCO commented out: THIS OK ???? only looking for COLUMNS NAMES ?
            sbQuery.append(" where 1 = 0");// original
            
            logger.debug(sbQuery.toString());

            rs = stmt.executeQuery(sbQuery.toString());

            rsMeta = rs.getMetaData();
            for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
                columnName = rsMeta.getColumnName(i);
                if(columnName != null) {
                    column = new Column();
                    column.setName(columnName.trim());
                    column.setColumnType(rsMeta.getColumnType(i));
                    column.setColumnTypeString(rsMeta.getColumnTypeName(i));
                    column.setAllowInsert(rsMeta.isWritable(i));
                    column.setAllowUpdate(rsMeta.isWritable(i));
                    column
                        .setNullable(rsMeta.isNullable(i) == ResultSetMetaData.columnNullable
                                || rsMeta.isNullable(i) == ResultSetMetaData.columnNullableUnknown);
                    columns.add(column);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw e;
        } finally {
            closeAll(con, stmt, rs);
        }

        return columns;
    }
    
    
    
}
