/*
 * Created on 14-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author Ronan Gill
 * 
 * 14-Mar-2004
 */
public class ODBCProviderFactory {

    // Log4j logger
    private static Logger logger = Logger.getLogger(ODBCProviderFactory.class);

    private static ODBCProviderFactory instance = null;

    private ArrayList odbcProviders;

    public static void main(String args[]) {
        ODBCProviderFactory o;
        try {
            o = ODBCProviderFactory.getInstance();
            ArrayList a = o.getODBCProviderList();
            for (int i = 0; i < a.size(); i++)
                logger.debug((String) a.get(i));
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static ODBCProviderFactory getInstance() throws Exception {

        if (instance == null) {
            synchronized (ODBCProviderFactory.class) {
                if (instance == null) {
                    try {
                        instance = new ODBCProviderFactory();
                    } catch (Exception e) {
                        logger.error(e);
                        throw e;
                    }
                }
            }
        }
        return instance;
    }

    private ODBCProviderFactory() throws Exception {
        buildODBCProvidersList();
    }

    public ArrayList getODBCProviderList() {
        return odbcProviders;
    }

    public ODBCProvider getODBCProvider(long id) {
        for (int i = 0; i <= odbcProviders.size(); i++) {
            if (((ODBCProvider) odbcProviders.get(i)).getId() == id)
                    return (ODBCProvider) odbcProviders.get(i);
        }
        return null;
    }

    public synchronized void buildODBCProvidersList() throws Exception {
        // Create the TreeMap whcih will hold the Providers
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        // Create the TreeMap whcih will hold the ServerRegistrations
        odbcProviders = new ArrayList();

        try {
            con = Connection.getConnection();
            stmt = con.createStatement();
            rs = stmt
                    .executeQuery("SELECT id, name, select_get_tables, connectionClass  FROM dbo.gdma_odbc_types ORDER BY name");

            while (rs != null && rs.next()) {
                ODBCProvider odbc = new ODBCProvider();

                odbc.setId(rs.getLong("id"));
                odbc.setName(rs.getString("name"));
                odbc.setSQLGetTables(rs.getString("select_get_tables"));
                odbc.setConnectionClass(rs.getString("connectionClass"));
                odbcProviders.add(odbc);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }

    }
}
