package com.vodafone.gdma.dbaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author Ronan Gill
 * 
 * This class is used to create and retrieve ODBC records in the database.
 * The list is held im memory to speed up the application.
 * Uses a singleton pattern
 * 
 */
public class ODBCProviderFactory extends DBFactory {

    // Log4j logger
    private static Logger logger = Logger.getLogger(ODBCProviderFactory.class);

    private static ODBCProviderFactory instance = null;

    public static ODBCProviderFactory getInstance() throws Exception {
        if (instance == null) {
            synchronized (ODBCProviderFactory.class) {
                if (instance == null) {
                    try {
                        instance = new ODBCProviderFactory();
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                        throw e;
                    }
                }
            }
        }
        return instance;
    }

    private ODBCProviderFactory() throws Exception {
        buildList();
    }

    public ODBCProvider getODBCProvider(long id) {
        ODBCProvider odbc;
        for (int i = 0; i <= list.size(); i++) {
            odbc = (ODBCProvider) list.get(i);
            if (odbc.getId() == id) return odbc;
        }
        return null;
    }

    public synchronized void buildList() throws Exception {
        // Create the TreeMap whcih will hold the Providers
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        ODBCProvider odbc = null;
        String query = "SELECT * FROM gdma_odbc_types ORDER BY name";

        // Create the ArrayList whcih will hold the ServerRegistrations
        list = new ArrayList();

        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs != null && rs.next()) {
                odbc = new ODBCProvider();

                odbc.setId(rs.getLong("id"));
                odbc.setName(rs.getString("name"));
                odbc.setSQLGetTables(rs.getString("select_get_tables"));
                odbc.setConnectionClass(rs.getString("connectionClass"));
                list.add(odbc);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw e;
        } finally {
            closeAll(con, stmt, rs);
        }
    }
}
