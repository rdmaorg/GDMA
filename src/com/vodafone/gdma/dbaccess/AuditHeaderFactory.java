package com.vodafone.gdma.dbaccess;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author RGILL
 * 
 * This class is used to create and retrieve Audit Header records in the database.
 * For now, the code only works with an oracle database.
 *  
 */
public class AuditHeaderFactory extends DBFactory {

    private static AuditHeaderFactory instance = null;

    // Log4j logger
    private static Logger logger = Logger.getLogger(AuditHeaderFactory.class);

    /*
     * GEt a particular Audit Header Record
     */
    public AuditHeader getAuditHeader(Long id) {
        AuditHeader AuditHeader = null;
        if (id == null) return null;
        for (int i = 0; i <= list.size(); i++) {
            AuditHeader = (AuditHeader) list.get(i);
            if (id.equals(AuditHeader.getID())) return AuditHeader;
        }
        return null;
    }

    /*
     * Build the list of Audit Header Records
     * @see com.vodafone.gdma.dbaccess.DBFactory#buildList()
     */
    public synchronized void buildList() throws ClassNotFoundException,
            SQLException, IOException, Exception {
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM GDMA_AUDIT_HEADER order by modifed_date";
        // Create the ArrayList which will hold records
        list = new ArrayList();
        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs != null && rs.next()) {
                AuditHeader AuditHeader = new AuditHeader();
                AuditHeader.setID(new Long(rs.getLong("id")));
                AuditHeader.setTableID(new Long(rs.getLong("table_id")));
                AuditHeader.setType(rs.getString("type"));
                AuditHeader.setModifiedBy(rs.getString("modified_by"));
                AuditHeader.setModifiedOn(rs.getTimestamp("modified_on"));
                list.add(AuditHeader);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw e;
        } finally {
            closeAll(con, stmt, rs);
        }

    }

    /*
     * Save an Audit Header record in teh database.
     * The newly created id will be placed back into the Audit Header record
     * 
     */
    public void save(AuditHeader auditHeader) throws Exception {

        Connection con = null;
        CallableStatement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        try {
            con = DBUtil.getConnection();

            sbInsert.append("BEGIN\n INSERT INTO \"GMDA_AUDIT_HEADER\" ");
            sbInsert.append(" (\"ID\", \"TABLE_ID\", \"TYPE\", ");
            sbInsert.append(" \"MODIFIED_BY\", \"MODIFIED_ON\") ");
            sbInsert
                    .append(" VALUES (\"SEQ_GDMA_AUDIT_HEADER\".NEXTVAL, ?, ?, ?, ? )");
            sbInsert.append(" RETURNING \"ID\" INTO ? ;\n");
            sbInsert.append("END;");

            stmt = con.prepareCall(sbInsert.toString());
            stmt.setLong(1, auditHeader.getTableID().longValue());
            stmt.setString(2, auditHeader.getType());
            stmt.setString(3, auditHeader.getModifiedBy());
            stmt.setTimestamp(4, auditHeader.getModifiedOn());
            stmt.registerOutParameter(5, Types.NUMERIC);
            stmt.execute();
            
            long lng = stmt.getBigDecimal(5).longValue();            
            auditHeader.setID(new Long(lng));
            logger.debug("Audit Header ID = " + auditHeader.getID());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw e;
        } finally {
            closeAll(con, stmt, null);
        }
    }
}
