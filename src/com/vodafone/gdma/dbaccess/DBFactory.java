/*
 * Created on Mar 23, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;


/**
 * @author RGILL
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class DBFactory {
    
//  Log4j logger
    protected static Logger logger = Logger
            .getLogger(DBFactory.class);

    protected ArrayList list;
    
    protected abstract void buildList() throws Exception;
    
    public ArrayList getList() {
        return list;
    }

    protected void closeAll(Connection con, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (Exception e) {
            logger.error("Exception while closing ResultSet", e);
        }
        try {
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            logger.error("Exception while closing Statement", e);
        }
        try {
            if (con != null) con.close();
        } catch (Exception e) {
            logger.error("Exception while closing Connection", e);
        }
    }
}
