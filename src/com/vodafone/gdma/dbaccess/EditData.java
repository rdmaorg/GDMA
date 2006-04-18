/*
 * Created on Mar 22, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.vodafone.gdma.security.User;
import com.vodafone.gdma.util.Config;
import com.vodafone.gdma.util.Formatter;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class EditData {

    // Log4j logger
    private static Logger logger = Logger.getLogger("EditData");

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
    
    // SOCO change request 25/01/06 (add ability to change sort order of results) 
    private static String orderby = null;
    private static boolean descending = false;
    // END SOCO
    
    public int save(ServerRegistration reg, Table table,
            HttpServletRequest request) throws Exception {

        int ret = 0;
        this.reg = reg;
        this.table = table;
        this.request = request;

        user = (User) request.getSession().getAttribute("USER");

        odbc = ODBCProviderFactory.getInstance().getODBCProvider(
                reg.getOdbcTypeID());
        con = DBUtil.getConnection(odbc.getConnectionClass(),
                reg.getUsername(), reg.getPassword(), reg.getConnectionURL());
       
        // SOCO COMMENTED OUT
        //quotedIdentifer = con.getMetaData().getIdentifierQuoteString(); // original
        quotedIdentifer = ""; // soco

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

    public String select(ServerRegistration reg, Table table,
            HttpServletRequest request, String mode) throws Exception {

        int ret = 0;
        this.reg = reg;
        this.table = table;
        this.request = request;

        odbc = ODBCProviderFactory.getInstance().getODBCProvider(
                reg.getOdbcTypeID());
        con = DBUtil.getConnection(odbc.getConnectionClass(),
                reg.getUsername(), reg.getPassword(), reg.getConnectionURL());
        // SOCO COMMENTED OUT        
        //quotedIdentifer = con.getMetaData().getIdentifierQuoteString(); // original
        quotedIdentifer = ""; // soco
        
        return selectData(mode);
    }

    public String generateSelect(Column display, Column store, String name,
            String value) throws Exception {
        PreparedStatement stmt = null;
        Column column = null;
        ResultSet rs = null;
        StringBuffer sbTemp = new StringBuffer();
        String strDisplay, strStore;

        this.table = TableFactory.getInstance().getTable(display.getTableID());
        this.reg = ServerRegistrationFactory.getInstance()
                .getServerRegistration(table.getServerID());

        odbc = ODBCProviderFactory.getInstance().getODBCProvider(
                reg.getOdbcTypeID());
        con = DBUtil.getConnection(odbc.getConnectionClass(),
                reg.getUsername(), reg.getPassword(), reg.getConnectionURL());

        // SOCO COMMENTED OUT        
        //quotedIdentifer = con.getMetaData().getIdentifierQuoteString(); // original
        quotedIdentifer = ""; // soco

        columnsNew = new ArrayList();
        columnsWhere = new ArrayList();
        columnsNew.add(display);
        columnsNew.add(store);

        // SOCO 13/04/06 CHANGED THIS
        stmt = con.prepareStatement(createSelectStatement());

        rs = stmt.executeQuery();
        sbTemp.append("<select value=\"");
        sbTemp.append(value);
        sbTemp.append("\" name=\"");
        sbTemp.append(name);
        sbTemp.append("\" id=\"");
        sbTemp.append(name);
        sbTemp.append("\">\n");
        //if its for a search the insert an ALL
        if (name.startsWith("search"))
                sbTemp.append("<option value=\"%\">ALL</option>\n");

        if (rs != null) {
            while (rs.next()) {
                strDisplay = rs.getString(1);
                strStore = rs.getString(2);
                sbTemp.append("<option value=\"");
                sbTemp.append(strStore);
                sbTemp.append("\">");
                sbTemp.append(strDisplay);
                sbTemp.append("</option>\n");
            }
            sbTemp.append("</select>\n");
            if (value != null && !"".equals(value.trim())) {
                sbTemp.append("<script>\n");
                sbTemp.append("  document.getElementById('");
                sbTemp.append(name);
                sbTemp.append("').value = '");
                sbTemp.append(value);
                sbTemp.append("';\n</script>\n");
            }
        } else {
            sbTemp.append("<option value=\"\">");
            sbTemp.append("No data found");
            sbTemp.append("</option>\n");
            sbTemp.append("</select>\n");
        }

        return sbTemp.toString();
    }

    private String selectData(String mode) throws Exception {
        PreparedStatement stmt = null;
        Column column = null;
        ResultSet rs = null;
       
        // SOCO change request 25/01/06 (add ability to change sort order of results) 
        if(null!=orderby && orderby.equals((String)request.getParameter("orderby")))
        	toggleDescending();
        else
        {
        	// reset descending
        	descending = false;
        }
        
        orderby = (String)request.getParameter("orderby");
        // END SOCO
                
        try 
        {
            columnsNew = table.getDisplayedColumns();
            columnsWhere = table.getDisplayedColumns();
            
          
            if (table.getDisplayedColumns().size() > 0) 
            {
                if (request.getParameter("BACK") != null && request.getSession().getAttribute("OLD_WHERE") != null) 
                {
                    columnsWhere = (ArrayList) request.getSession().getAttribute("OLD_WHERE");
                    valuesWhere = (HashMap) request.getSession().getAttribute("OLD_WHERE_VALUES"); 
                } 
                else 
                {
                    getSearchValues();
                }

                request.setAttribute("orderby", orderby);
                request.setAttribute("descending", descending+"");
                request.getSession().setAttribute("OLD_WHERE", columnsWhere);
                request.getSession().setAttribute("OLD_WHERE_VALUES", valuesWhere);

                stmt = con.prepareStatement(createSelectStatement());
                rs = stmt.executeQuery();
            }
            if ("XML".equals(mode))
                return getAsXML(rs);
            else if ("CSV".equals(mode))
                return getAsCSV(rs);
            else
                return getAsHTML(rs);

        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            throw new Exception("NumberFormatException - " + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            closeAll(con, stmt, rs);
        }
    }

    /* SOCO 
     * NEW method to toggle the sort order from asceding to descending
     */
    private void toggleDescending()
    {
    	
    	if(new Boolean((String)request.getParameter("descending")).booleanValue())
    	  descending = false;
    	if(!new Boolean((String)request.getParameter("descending")).booleanValue())
      	  descending = true;
    	
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
        } catch (SQLException e) {
            logger.error("START SQL ERROR");
            while (e != null) {
                logger.error("SQLState: " + e.getSQLState());
                logger.error("Message:  " + e.getMessage());
                logger.error("Vendor:   " + e.getErrorCode());
                e = e.getNextException();
            }
            logger.error("END SQL ERROR");
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            closeAll(con, stmt, null);
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
        } catch (SQLException e) {
            logger.error("START SQL ERROR");
            while (e != null) {
                logger.error("SQLState: " + e.getSQLState());
                logger.error("Message:  " + e.getMessage());
                logger.error("Vendor:   " + e.getErrorCode());
                e = e.getNextException();
            }
            logger.error("END SQL ERROR");
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            throw e;
        } finally {
            closeAll(con, stmt, null);
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
        } catch (SQLException e) {
            logger.error("START SQL ERROR");
            while (e != null) {
                logger.error("SQLState: " + e.getSQLState());
                logger.error("Message:  " + e.getMessage());
                logger.error("Vendor:   " + e.getErrorCode());
                e = e.getNextException();
            }
            logger.error("END SQL ERROR");
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            closeAll(con, stmt, null);
        }
    }

    private void getSearchValues() throws ParseException {
        valuesWhere = new HashMap();
        Column column = null;
        String value;
        
        
        for (int i = 0; i < columnsWhere.size(); i++) {
            column = (Column) columnsWhere.get(i);
            value = request.getParameter("search_" + column.getName());
            
            if (value != null) {
                if ("".equals(value)) value = null;

                if (value != null && DBUtil.isDate(column.getColumnType())) {
                    value = "" + Formatter.parseDate(value).getTime();
                }
            }
            valuesWhere.put(column.getName(), value);
        }
    }

    private void getOldValues() throws ParseException {
        valuesWhere = new HashMap();
        Column column = null;
        String value;
        
        for (int i = 0; i < columnsWhere.size(); i++) {
            column = (Column) columnsWhere.get(i);
            value = request.getParameter("old_" + column.getName());
            if (value != null) {
                if ("".equals(value)) value = null;

                if (value != null && DBUtil.isDate(column.getColumnType())) {
                    value = "" + Formatter.parseDate(value).getTime();
                }
            }
            valuesWhere.put(column.getName(), value);
        }
    }

    private void getNewValues() throws ParseException {
        Column column = null;
        String value;
        valuesNew = new HashMap();

        for (int i = 0; i < columnsNew.size(); i++) {
            column = (Column) columnsNew.get(i);
            value = request.getParameter("new_" + column.getName());
            if (value != null) {
                if ("".equals(value)) value = null;

                if (value != null && DBUtil.isDate(column.getColumnType())) {
                    value = "" + Formatter.parseDate(value).getTime();
                }
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

    
    private String createSelectWhereClause() {

        Column column;
        int size = columnsWhere.size();
        String value = null;
        boolean firstWhereClause = true;

        StringBuffer sbStatement = new StringBuffer();

        for (int i = 0; i < size; i++) {
            column = (Column) columnsWhere.get(i);
            value = (String) valuesWhere.get(column.getName());
            
            if (value != null && !"".equals(value.trim())) {
                sbStatement.append(firstWhereClause ? "" : " AND ");
                if (column != null && 
                    column.getDropDownColumnDisplay() != null &&
                    !"%".equals(value)) {
                    sbStatement.append("upper(cast(");
                    sbStatement.append(quotedIdentifer);
                    sbStatement.append(column.getName());
                    sbStatement.append(quotedIdentifer);
                    sbStatement.append(" as char(100)))");
                    sbStatement.append(" = upper(cast('");
                    sbStatement.append(value.toUpperCase());
                    sbStatement.append("'as char(100)))");
                } else {

                    sbStatement.append("upper(cast(");
                    sbStatement.append(quotedIdentifer);
                    sbStatement.append(column.getName());
                    sbStatement.append(quotedIdentifer);
                    sbStatement.append(" as char(100)))");
                    sbStatement.append(" LIKE '%");
                    sbStatement.append(value.toUpperCase());
                    sbStatement.append("%'");
                }

                firstWhereClause = false;
            }
        }
        if (sbStatement.length() > 1) {
            sbStatement.insert(0, " WHERE ( ");
            sbStatement.append(") ");
        }
        
        return sbStatement.toString();
    }

    private String createUpdateStatement() {

        Column column;
        int size = columnsNew.size();

        StringBuffer sbUpdate = new StringBuffer("UPDATE ");

        sbUpdate.append(quotedIdentifer);
        sbUpdate.append(reg.getPrefix());
        sbUpdate.append(quotedIdentifer);
        sbUpdate.append(".");
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

    private String createSelectStatement() {

        Column column;
        int size = columnsNew.size();
        StringBuffer sbSelect = new StringBuffer("SELECT ");

        for (int i = 0; i < size; i++) {
            column = (Column) columnsNew.get(i);
            sbSelect.append(quotedIdentifer);
            sbSelect.append(table.getName());
            sbSelect.append(quotedIdentifer);
            sbSelect.append(".");
            sbSelect.append(quotedIdentifer);
            sbSelect.append(column.getName());
            sbSelect.append(quotedIdentifer);
            sbSelect.append(i == (size - 1) ? "" : ",");
        }

        sbSelect.append(" FROM ");

        sbSelect.append(quotedIdentifer);
        sbSelect.append(reg.getPrefix());
        sbSelect.append(quotedIdentifer);
        sbSelect.append(".");
        sbSelect.append(quotedIdentifer);
        sbSelect.append(table.getName());
        sbSelect.append(quotedIdentifer);

        sbSelect.append(createSelectWhereClause());
        
        // SOCO ON THURSDAY COMMENTED THIS OUT 13/04/06
        sbSelect.append(createSelectOrderByClause());      
        
        logger.debug(sbSelect);
        return sbSelect.toString();
    }
    
    

    /**
     * SOCO
     * NEW method to add "order by" to select SQL statment
     */
    private String createSelectOrderByClause() 
    {      
        StringBuffer statement = new StringBuffer();
        if(null!=orderby && !orderby.trim().equals(""))
        {   
        	boolean orderByOk = false;
        	for (int i = 0; i < columnsNew.size(); i++) 
        	{
        		Column column = (Column) columnsNew.get(i);
        		
                if(column.getName().equalsIgnoreCase(orderby))
	           	{
	                orderByOk = true;
	           		break;
	           	}

	         }
        	
        	 if(orderByOk)
        	 {
        	    statement.append(" order by "+orderby); 
        	    if(descending)
                  statement.append(" desc ");
        	 }
        }
        
        return statement.toString();
    }


    // METHOD NO LONGER USED ????
    private String createOrderBy() {

       
	         Column column;
	         int size = columnsNew.size();
	
	         StringBuffer sbOrderBy = new StringBuffer(" ORDER BY ");
	
	         for (int i = 0; i < size; i++) {
	            column = (Column) columnsNew.get(i);
	            sbOrderBy.append(quotedIdentifer);
	            sbOrderBy.append(table.getName());
	            sbOrderBy.append(quotedIdentifer);
	            sbOrderBy.append(".");
	            sbOrderBy.append(quotedIdentifer);
	            sbOrderBy.append(column.getName());
	            sbOrderBy.append(quotedIdentifer);
	            sbOrderBy.append(i == (size - 1) ? "" : ",");
	         }
	
	         logger.debug(sbOrderBy);
	         return sbOrderBy.toString();
       
    }
   
    private String createInsertStatement() {

        Column column;
        int size = columnsNew.size();

        StringBuffer sbInsert = new StringBuffer("INSERT INTO ");
        StringBuffer sbValues = new StringBuffer("VALUES ( ");

        sbInsert.append(quotedIdentifer);
        sbInsert.append(reg.getPrefix());
        sbInsert.append(quotedIdentifer);
        sbInsert.append(".");
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
        sbDelete.append(reg.getPrefix());
        sbDelete.append(quotedIdentifer);
        sbDelete.append(".");
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
            case Types.BIT:
            case Types.SMALLINT:
                stmt.setInt(position, Integer.parseInt(data));
                logger.debug(position + " : " + Integer.parseInt(data));
                break;
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.NUMERIC:
            case Types.REAL:
            case Types.DOUBLE:
                stmt.setBigDecimal(position, new BigDecimal(data));
                logger.debug(position + " : " + new BigDecimal(data));
                break;
            case Types.DATE:
                java.util.Date date = Formatter.parseDate(data);
                if (date != null) {
                    stmt.setDate(position, new java.sql.Date(date.getTime()));
                    logger.debug(position + " : " + date);
                } else {
                    stmt.setNull(position, sqlDataType);
                    logger.debug(position + " : null");
                }
                break;
            case Types.TIME:
            case Types.TIMESTAMP:
                java.util.Date time = Formatter.parseDate(data);
                if (time != null) {
                    stmt.setTimestamp(position, new Timestamp(time.getTime()));
                    logger.debug(position + " : " + time);
                } else {
                    stmt.setNull(position, sqlDataType);
                    logger.debug(position + " : null");
                }
                break;
            default:
                logger.debug("Unknow datatype[" + sqlDataType
                        + "] - using String");
                stmt.setString(position, data);
                break;
            }
        }
    }

    private void closeAll(Connection con, Statement stmt, ResultSet rs) {
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

    private void generateInsertAuditRecord() throws Exception {
        AuditHeaderFactory headerFac = new AuditHeaderFactory();
        AuditRecordFactory recordFac = new AuditRecordFactory();
        Timestamp date = new Timestamp((new Date()).getTime());
        AuditHeader header = new AuditHeader();
        header.setTableID(table.getId());
        header.setType("I");
        header.setModifiedBy(user.getDomain() + "\\" + user.getUserName());
        header.setModifiedOn(date);

        headerFac.save(header);
        logger.debug("Audit Header ID = " + header.getID());
        for (int i = 0; i < columnsNew.size(); i++) {
            Column column = (Column) columnsNew.get(i);
            String value;
            AuditRecord record = new AuditRecord();
            record.setAuditHeaderID(header.getID());
            record.setColumnID(column.getId());
            value = (String) valuesNew.get(column.getName());
            if (value != null && DBUtil.isDate(column.getColumnType())) {
                value = Formatter.getDateStringFromTimestamp(value);
            }
            record.setNewValue("" + value);
            record.setOldValue(null);
            recordFac.addAuditRecord(record);
        }
    }

    private void generateUpdateAuditRecord() throws Exception {
        AuditHeaderFactory headerFac = new AuditHeaderFactory();
        AuditRecordFactory recordFac = new AuditRecordFactory();
        Timestamp date = new Timestamp((new Date()).getTime());
        AuditHeader header = new AuditHeader();
        header.setTableID(table.getId());
        header.setType("U");
        header.setModifiedBy(user.getDomain() + "\\" + user.getUserName());
        header.setModifiedOn(date);

        headerFac.save(header);
        logger.debug("Audit Header ID = " + header.getID());
        for (int i = 0; i < columnsWhere.size(); i++) {
            Column column = (Column) columnsWhere.get(i);
            String value;
            AuditRecord record = new AuditRecord();
            record.setAuditHeaderID(header.getID());
            record.setColumnID(column.getId());
            if (column.isAllowUpdate())
                value = (String) valuesNew.get(column.getName());
            else
                value = (String) valuesWhere.get(column.getName());

            if (value != null && DBUtil.isDate(column.getColumnType())) {
                value = Formatter.getDateStringFromTimestamp(value);
            }
            record.setNewValue(value);

            value = (String) valuesWhere.get(column.getName());
            if (DBUtil.isDate(column.getColumnType())) {
                value = Formatter.getDateStringFromTimestamp(value);
            }

            record.setOldValue("" + value);
            recordFac.addAuditRecord(record);
        }
    }

    private void generateDeleteAuditRecord() throws Exception {
        AuditHeaderFactory headerFac = new AuditHeaderFactory();
        AuditRecordFactory recordFac = new AuditRecordFactory();
        Timestamp date = new Timestamp((new Date()).getTime());
        AuditHeader header = new AuditHeader();
        header.setTableID(table.getId());
        header.setType("D");
        header.setModifiedBy(user.getDomain() + "\\" + user.getUserName());
        header.setModifiedOn(date);

        headerFac.save(header);
        logger.debug("Audit Header ID = " + header.getID());
        for (int i = 0; i < columnsWhere.size(); i++) {
            Column column = (Column) columnsWhere.get(i);
            String value;
            AuditRecord record = new AuditRecord();
            record.setAuditHeaderID(header.getID());
            record.setColumnID(column.getId());
            record.setNewValue(null);
            value = (String) valuesWhere.get(column.getName());
            if (DBUtil.isDate(column.getColumnType())) {
                value = Formatter.getDateStringFromTimestamp(value);
            }

            record.setOldValue("" + value);
            recordFac.addAuditRecord(record);
        }
    }

    private String getAsCSV(ResultSet rs) throws Exception {
        StringBuffer sbTemp = new StringBuffer();
        String value;
        int columnCount;
        ResultSetMetaData rsmd;

        if (rs != null) {
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                if ("id".equalsIgnoreCase(rsmd.getColumnName(i)))
                        sbTemp.append("\"");
                sbTemp.append(rsmd.getColumnName(i));
                if ("id".equalsIgnoreCase(rsmd.getColumnName(i)))
                        sbTemp.append("\"");
                sbTemp.append(i == columnCount ? "" : ", ");
            }

            sbTemp.append("\r\n");

            for (int row = 1; rs.next(); row++) {
                for (int i = 1; i <= columnCount; i++) {
                    value = rs.getString(i);
                    sbTemp.append(value == null ? "" : value);
                    sbTemp.append(i == columnCount ? "" : ", ");
                }
                sbTemp.append("\r\n");
            }
        } else {
            sbTemp.append("No columns specified\r\n");
        }
        return sbTemp.toString();
    }

    private String getAsXML(ResultSet rs) throws Exception {

        StringBuffer sbTemp = new StringBuffer();

        ResultSetMetaData rsmd = null;
        int columnCount;
        sbTemp.append("<?xml version=\"1.0\"?>\n");
        sbTemp.append("<Workbook xmlns=\"urn:schemas-microsoft-com:");
        sbTemp.append("office:spreadsheet\"\n");
        sbTemp.append(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"\r\n");
        sbTemp.append(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\r\n");
        sbTemp
                .append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\r\n");
        sbTemp.append(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">\r\n");

        sbTemp.append("<Worksheet ss:Name=\"Sheet1\">\r\n");
        sbTemp.append("  <Table >\r\n");
        if (rs != null) {
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
            sbTemp.append("  <Row>\r\n");
            for (int i = 1; i <= columnCount; i++) {
                sbTemp.append("    <Cell><Data ss:Type=\"String\">");
                sbTemp.append(rsmd.getColumnName(i));
                sbTemp.append("</Data></Cell>\r\n");
            }
            sbTemp.append("  </Row>\r\n");
            for (int row = 1; rs.next(); row++) {
                sbTemp.append("  <Row>\r\n");
                for (int i = 1; i <= columnCount; i++) {
                    sbTemp.append("    <Cell><Data ss:Type=\"");

                    if (DBUtil.isDate(rsmd.getColumnType(i))) {
                        sbTemp.append("String\">");
                        java.sql.Date value = rs.getDate(i);
                        sbTemp.append(value == null ? "" : Formatter
                                .formatDate(value));
                    } else if (DBUtil.isNumber(rsmd.getColumnType(i))) {
                        sbTemp.append("Number\">");
                        String value = rs.getString(i);
                        sbTemp.append(value == null ? "" : value.trim());
                    } else {
                        String value = rs.getString(i);
                        sbTemp.append("String\">");
                        sbTemp.append(value == null ? "" : value.trim());
                    }
                    sbTemp.append("</Data></Cell>\r\n");
                }
                sbTemp.append("  </Row>\r\n");
            }

        } else {
            sbTemp.append("  <Row>\r\n");
            sbTemp.append("    <Cell><Data ss:Type=\"String\">");
            sbTemp.append("No Columns Specified");
            sbTemp.append("  </Row>\r\n");
        }
        sbTemp.append("</Table>\r\n");
        sbTemp.append("</Worksheet>\r\n");
        sbTemp.append("</Workbook>\r\n");

        return sbTemp.toString();
    }

    private String getAsHTML(ResultSet rs) throws Exception {
        StringBuffer sbTemp = new StringBuffer();

        ResultSetMetaData rsmd;
        int columnCount;
        int rowCount = 0;
        int maxRowCount = Integer.parseInt(Config.getProperty("max_row_count"));
        sbTemp.append("      <table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" class=\"dataTable\">\n");
        if (rs != null) {
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
            sbTemp.append("    <tr id=\"trHeader\">\n");
            sbTemp.append("      <td class=\"dataHeader\" width=\"30px\" nowrap>&nbsp;&nbsp;&nbsp;</td>\n");
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                sbTemp.append(" <td class=\"dataHeader\" nowrap style=\"cursor: hand;\" ");
                // SOCO add orderby onclick
                sbTemp.append(" ondblclick=\"refreshResultsOrderby('");
                sbTemp.append(columnName);
                sbTemp.append("', "+descending+");\"");
                sbTemp.append(" onclick=\"refreshResultsOrderby('");
                sbTemp.append(columnName);
                sbTemp.append("', "+descending+");\"");
                sbTemp.append(" >");
                sbTemp.append(columnName);
                //add hidden columns to take values when editing
                sbTemp.append("<input type=\"hidden\" value=\"\" id=\"old_");
                sbTemp.append(columnName);
                sbTemp.append("\" name=\"old_");
                sbTemp.append(columnName);
                sbTemp.append("\">&nbsp;</td>\n");
            }
            sbTemp.append("    </tr>\n");

            for (int row = 1; rs.next(); row++) {
                rowCount++;
                if (rowCount <= maxRowCount) {
                    sbTemp
                            .append("    <tr onmouseover=\"mouseEntered(this)\" onmouseout=\"mouseExited(this)\"");
                    if (table.isAllowUpdate()) {
                        sbTemp.append(" ondblclick=\"mouseDblClicked(this,");
                        sbTemp.append(row);
                        sbTemp.append(");\"");
                        sbTemp.append(" onclick=\"mouseClicked(this,");
                        sbTemp.append(row);
                        sbTemp.append(");\"");
                    }

                    sbTemp.append(" id=\"trRow");
                    sbTemp.append(row);
                    sbTemp.append("\" class=\"dataBody\">\n");
                    sbTemp
                            .append("      <td class=\"dataHeader\" align=\"left\" ");
                    sbTemp.append(" style=\"font-weight: normal\">");
                    sbTemp.append(row);
                    sbTemp.append("</td>\n");
                    String value, formatted;
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        // we need a hidden field for each column
                        if (DBUtil.isDate(rsmd.getColumnType(i))) {
                            java.sql.Timestamp date = rs.getTimestamp(i);
                            value = (date == null ? "0" : "" + date.getTime());
                            formatted = (date == null ? "&nbsp;" : Formatter
                                    .formatDate(date));
                        } else {
                            value = rs.getString(i);
                            formatted = (value == null ? "" : value.replaceAll(
                                    " ", "&nbsp;"));

                        }
                        sbTemp
                                .append("      <td class=\"dataGreyBorder\" nowrap>");
                        sbTemp.append(formatted);
                        sbTemp.append("<input type=\"hidden\" value=\"");
                        sbTemp.append(value);
                        sbTemp.append("\"></td>\n");
                    }
                    sbTemp.append("    </tr>\n");
                } else {
                    sbTemp.append("    <tr id=\"dataBody\">\n");
                    sbTemp
                            .append("      <td class=\"dataHeader\" width=\"30px\" ");
                    sbTemp.append("nowrap>&nbsp;&nbsp;&nbsp;</td>\n");
                    sbTemp
                            .append("      <td class=\"dataGreyBorder\" nowrap colspan=\"");
                    sbTemp.append(columnCount);
                    sbTemp.append("\">NOTE: Only the first ");
                    sbTemp.append(maxRowCount);
                    sbTemp.append(" rows were returned</td>\n");
                    ;
                    sbTemp.append("    </tr>\n");
                    break;
                }
            }

        } else {
            sbTemp.append("    <tr id=\"trHeader\">\n");
            sbTemp
                    .append("      <td class=\"dataHeader\" width=\"30px\" nowrap>&nbsp;&nbsp;&nbsp;</td>\n");
            sbTemp.append("      <td class=\"dataHeader\" nowrap >&nbsp;</td>");
            sbTemp.append("    </tr>\n");
            sbTemp.append("    <tr id=\"dataBody\">\n");
            sbTemp
                    .append("      <td class=\"dataHeader\" width=\"30px\" nowrap>&nbsp;&nbsp;&nbsp;</td>\n");
            sbTemp
                    .append("      <td class=\"dataGreyBorder\" nowrap >No Columns Specified</td>\n");
            sbTemp.append("    </tr>\n");
        }
        sbTemp.append("      </table>\n");
        return sbTemp.toString();
    }

}