/*
 * Created on Mar 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.vodafone.gdma.security.User;
import com.vodafone.gdma.util.Formatter;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class EditData {

    // Log4j logger
    private static Logger logger = Logger
            .getLogger("ServerRegistrationFactory");

    private ODBCProvider odbc = null;

    private Connection con = null;

    private String quotedIdentifer = "";

    private ServerRegistration reg = null;

    private Table table = null;

    private HttpServletRequest request = null;

    private HashMap valuesNew = null;

    private HashMap valuesWhere = null;

    private ArrayList columnsWhere = null;

    private ArrayList columnsNew = null;
    
    private User user = null;

    public int save(ServerRegistration reg, Table table,
            HttpServletRequest request) throws Exception {

        int ret = 0;
        this.reg = reg;
        this.table = table;
        this.request = request;
        
        user = (User)request.getSession().getAttribute("USER");

        odbc = ODBCProviderFactory.getInstance().getODBCProvider(
                reg.getOdbcTypeID());
        con = DBUtil.getConnection(odbc.getConnectionClass(),
                reg.getUsername(), reg.getPassword(), reg.getConnectionURL());
        quotedIdentifer = con.getMetaData().getIdentifierQuoteString();

        String strMode = request.getParameter("mode");
        if ("INSERT".equals(strMode)) {
            ret = insertData();
        } else if ("UPDATE".equals(strMode)) {
            ret = updateData();
        } else if ("DELETE".equals(strMode)) {
            ret = deleteData();
        }
        return ret;
    }

    private int updateData() throws Exception {
        PreparedStatement stmt = null;
        Column column = null;

        try {
            columnsWhere = table.getDisplayedColumns();
            columnsNew = table.getUpdateColumns();
            getOldValues();
            getNewValues();
            stmt = con.prepareStatement(createUpdateStatement());
            setNewValues(stmt);
            setWhereValues(stmt, columnsNew.size());

            int ret = stmt.executeUpdate();

            logger.debug("[" + ret + "] rows updated");
            generateUpdateAuditRecord();
            return ret;
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            throw new Exception("NumberFormatException - " + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            closeAll(con, stmt);
        }
    }

    private int insertData() throws Exception {
        PreparedStatement stmt = null;
        ArrayList columnsInsert = null;

        try {
            columnsNew = table.getInsertColumns();
            getNewValues();
            stmt = con.prepareStatement(createInsertStatement());
            setNewValues(stmt);

            int ret = stmt.executeUpdate();

            logger.debug("[" + ret + "] rows inserted");
            generateInsertAuditRecord();
            return ret;
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            throw new Exception("NumberFormatException - " + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            closeAll(con, stmt);
        }
    }

    private int deleteData() throws Exception {
        PreparedStatement stmt = null;
        ArrayList columnsDisplayed = null;

        try {
            columnsWhere = table.getDisplayedColumns();

            getOldValues();
            stmt = con.prepareStatement(createDeleteStatement());
            setWhereValues(stmt, 0);
            int ret = stmt.executeUpdate();

            logger.debug("[" + ret + "] rows deleted");
            generateDeleteAuditRecord();
            return ret;
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            throw new Exception("NumberFormatException - " + e.getMessage());
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            throw new Exception(
                    "ParseException - please ensure your dates are in the form ["
                            + Formatter.dateFormat + "].\n" + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            closeAll(con, stmt);
        }
    }

    private void getOldValues() {
        valuesWhere = new HashMap();
        Column column = null;
        for (int i = 0; i < columnsWhere.size(); i++) {
            column = (Column) columnsWhere.get(i);
            valuesWhere.put(column.getName(), request.getParameter("old_"
                    + column.getName()));
        }
    }

    private void getNewValues() throws ParseException {
        Column column = null;
        String value;

        valuesNew = new HashMap();

        for (int i = 0; i < columnsNew.size(); i++) {
            column = (Column) columnsNew.get(i);
            value = request.getParameter("new_" + column.getName());

            if (value != null) value = value.trim();
            if ("".equals(value.trim())) value = null;

            if (value != null && DBUtil.isDate(column.getColumnType())) {
                value = "" + Formatter.parseDate(value).getTime();
            }

            valuesNew.put(column.getName(), value);
        }
    }

    private void setNewValues(PreparedStatement stmt) throws Exception {
        Column column;
        String value;
        for (int i = 0; i < columnsNew.size(); i++) {
            column = (Column) columnsNew.get(i);
            value = (String) valuesNew.get(column.getName());
            setValue(stmt, i + 1, column.getColumnType(), value);
        }
    }

    private void setWhereValues(PreparedStatement stmt, int offset)
            throws Exception {
        Column column;
        String value;
        for (int i = 0; i < columnsWhere.size(); i++) {
            column = (Column) columnsWhere.get(i);
            value = (String) valuesWhere.get(column.getName());
            setValue(stmt, i + 1 + offset, column.getColumnType(), value);
        }
    }

    private String createWhereClause() {

        Column column;
        int size = columnsWhere.size();
        String value = null;

        StringBuffer sbStatement = new StringBuffer(" WHERE ");

        for (int i = 0; i < size; i++) {
            column = (Column) columnsWhere.get(i);
            value = (String) valuesWhere.get(column.getName());
            sbStatement.append(" (");
            sbStatement.append(quotedIdentifer);
            sbStatement.append(column.getName());
            sbStatement.append(quotedIdentifer);
            if (value == null || "".equals(value.trim())) {
                sbStatement.append(" IS NULL OR ");
                sbStatement.append(quotedIdentifer);
                sbStatement.append(column.getName());
                sbStatement.append(quotedIdentifer);
            }
            sbStatement.append(" = ?");
            sbStatement.append(") ");
            sbStatement.append(i == (size - 1) ? "" : " AND ");
        }
        return sbStatement.toString();
    }

    private String createUpdateStatement() {

        Column column;
        int size = columnsNew.size();

        StringBuffer sbUpdate = new StringBuffer("UPDATE ");

        sbUpdate.append(quotedIdentifer);
        sbUpdate.append(table.getName());
        sbUpdate.append(quotedIdentifer);
        sbUpdate.append(" SET ");

        for (int i = 0; i < size; i++) {

            sbUpdate.append(quotedIdentifer);
            sbUpdate.append(((Column) columnsNew.get(i)).getName());
            sbUpdate.append(quotedIdentifer);
            sbUpdate.append(" = ?");
            sbUpdate.append(i == (size - 1) ? "" : ",");
        }

        sbUpdate.append(createWhereClause());

        logger.debug(sbUpdate);
        return sbUpdate.toString();
    }

    private String createInsertStatement() {

        Column column;
        int size = columnsNew.size();

        StringBuffer sbInsert = new StringBuffer("INSERT INTO ");
        StringBuffer sbValues = new StringBuffer("VALUES ( ");

        sbInsert.append(quotedIdentifer);
        sbInsert.append(table.getName());
        sbInsert.append(quotedIdentifer);
        sbInsert.append(" ( ");
        for (int i = 0; i < size; i++) {

            sbInsert.append(quotedIdentifer);
            sbInsert.append(((Column) columnsNew.get(i)).getName());
            sbInsert.append(quotedIdentifer);
            sbValues.append(" ?");
            if (i != (size - 1)) {//Don't add commas for last column
                sbInsert.append(", ");
                sbValues.append(", ");
            }
        }
        sbInsert.append(" ) ");
        sbValues.append(" ) ");

        sbInsert.append(sbValues.toString());

        logger.debug(sbInsert);

        return sbInsert.toString();
    }

    private String createDeleteStatement() {

        StringBuffer sbDelete = new StringBuffer("DELETE FROM ");
        sbDelete.append(quotedIdentifer);
        sbDelete.append(table.getName());
        sbDelete.append(quotedIdentifer);
        sbDelete.append(createWhereClause());

        logger.debug(sbDelete);

        return sbDelete.toString();
    }

    private void setValue(PreparedStatement stmt, int position,
            int sqlDataType, String data) throws Exception {
        if (data == null || "".equals(data.trim())) {
            stmt.setNull(position, sqlDataType);
            logger.debug(position + " : [null]");
        } else {

            switch (sqlDataType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                stmt.setString(position, data);
                logger.debug(position + " : " + data);
                break;
            case Types.TINYINT:
            case Types.INTEGER:
            case Types.BIGINT:
                stmt.setInt(position, Integer.parseInt(data));
                logger.debug(position + " : " + Integer.parseInt(data));
                break;
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.NUMERIC:
            case Types.REAL:
                stmt.setBigDecimal(position, new BigDecimal(data));
                logger.debug(position + " : " + new BigDecimal(data));
                break;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                stmt
                        .setTimestamp(position, new Timestamp(Long
                                .parseLong(data)));
                logger.debug(position + " : " +  new Timestamp(Long
                        .parseLong(data)));
                break;
            default:
                logger.debug("Unknow datatype[" + sqlDataType
                        + "] - using String");
                stmt.setString(position, data);
                break;
            }
        }
    }

    private void closeAll(Connection con, Statement stmt) {
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

    private void generateInsertAuditRecord() throws Exception{
        AuditHeaderFactory headerFac = new AuditHeaderFactory();
        AuditRecordFactory recordFac = new AuditRecordFactory();
        Timestamp date = new Timestamp((new Date()).getTime());
        AuditHeader header = new AuditHeader();
        header.setTableID(table.getId());
        header.setType("I");
        header.setModifiedBy(user.getDomain() + "\\" + user.getUserName());
        header.setModifiedOn(date);
        
        headerFac.addAuditHeader(header);
        logger.debug("Audit Header ID = " + header.getID());
        for(int i = 0; i < columnsWhere.size();i++){
            Column column = (Column)columnsWhere.get(i);
            AuditRecord record = new AuditRecord();
            record.setAuditHeaderID(header.getID());
            record.setColumnID(column.getId());
            record.setNewValue(valuesNew == null? null: (String)valuesNew.get(column.getName()));
            record.setOldValue(null);
            recordFac.addAuditRecord(record);
        }
    }
    
    private void generateUpdateAuditRecord() throws Exception{
        AuditHeaderFactory headerFac = new AuditHeaderFactory();
        AuditRecordFactory recordFac = new AuditRecordFactory();
        Timestamp date = new Timestamp((new Date()).getTime());
        AuditHeader header = new AuditHeader();
        header.setTableID(table.getId());
        header.setType("I");
        header.setModifiedBy(user.getDomain() + "\\" + user.getUserName());
        header.setModifiedOn(date);
        
        headerFac.addAuditHeader(header);
        logger.debug("Audit Header ID = " + header.getID());
        for(int i = 0; i < columnsWhere.size();i++){
            Column column = (Column)columnsWhere.get(i);
            AuditRecord record = new AuditRecord();
            record.setAuditHeaderID(header.getID());
            record.setColumnID(column.getId());
            if(column.isAllowUpdate()){
                record.setNewValue((String)valuesNew.get(column.getName()));
            }else{
                record.setNewValue((String)valuesWhere.get(column.getName()));
            }
            record.setOldValue(valuesWhere == null? null: (String)valuesWhere.get(column.getName()));
            recordFac.addAuditRecord(record);
        }
    }
    
    private void generateDeleteAuditRecord() throws Exception{
        AuditHeaderFactory headerFac = new AuditHeaderFactory();
        AuditRecordFactory recordFac = new AuditRecordFactory();
        Timestamp date = new Timestamp((new Date()).getTime());
        AuditHeader header = new AuditHeader();
        header.setTableID(table.getId());
        header.setType("I");
        header.setModifiedBy(user.getDomain() + "\\" + user.getUserName());
        header.setModifiedOn(date);
        
        headerFac.addAuditHeader(header);
        logger.debug("Audit Header ID = " + header.getID());
        for(int i = 0; i < columnsWhere.size();i++){
            Column column = (Column)columnsWhere.get(i);
            AuditRecord record = new AuditRecord();
            record.setAuditHeaderID(header.getID());
            record.setColumnID(column.getId());
            record.setNewValue(null);
            record.setOldValue(valuesWhere == null? null: (String)valuesWhere.get(column.getName()));
            recordFac.addAuditRecord(record);
        }
    }
}
