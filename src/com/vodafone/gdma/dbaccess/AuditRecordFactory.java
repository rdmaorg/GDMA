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
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class AuditRecordFactory extends DBFactory {

    private static AuditRecordFactory instance = null;

    // Log4j logger
    private static Logger logger = Logger.getLogger(AuditRecordFactory.class);

    public AuditRecord getAuditRecord(Long id) {
        AuditRecord AuditRecord = null;
        
        if(id == null)
            return null;
        
        for (int i = 0; i <= list.size(); i++) {
            AuditRecord = (AuditRecord) list.get(i);
            if (id.equals(AuditRecord.getId())) return AuditRecord;
        }
        return null;
    }

    public synchronized void buildList() throws ClassNotFoundException,
            SQLException, IOException, Exception {
        // Create the TreeMap whcih will hold the Providers
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "select * from gdma_audit_header order by column_id";
        // Create the ArrayList which will hold the ServerRegistrations
        list = new ArrayList();

        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs != null && rs.next()) {
                AuditRecord AuditRecord = new AuditRecord();

                AuditRecord.setId(new Long(rs.getLong("id")));
                AuditRecord.setAuditHeaderID(new Long(rs.getLong("audit_header_id")));
                AuditRecord.setColumnID(new Long(rs.getLong("column_id")));
                AuditRecord.setOldValue(rs.getString("old_value"));
                AuditRecord.setNewValue(rs.getString("new_value"));
                list.add(AuditRecord);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw e;
        } finally {
            closeAll(con, stmt, rs);
        }

    }

    public void addAuditRecord(AuditRecord auditHeader) throws Exception {

        Connection con = null;
        Statement stmt = null;
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("INSERT INTO gmda_audit_record  ");
        sbInsert.append("(audit_header_id,column_id,old_value,new_value) VALUES (");
        sbInsert.append(auditHeader.getAuditHeaderID());
        sbInsert.append(",'");
        sbInsert.append(auditHeader.getColumnID());
        sbInsert.append("','");
        sbInsert.append(auditHeader.getOldValue());
        sbInsert.append("','");
        sbInsert.append(auditHeader.getNewValue());
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

    public void updateAuditRecord(AuditRecord AuditRecord) throws Exception {
        throw new Exception ("Update of Audit Not Allowed"); 
    }
}
