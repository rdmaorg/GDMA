/*
 * Created on Mar 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.dbaccess;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

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

    public void save(ServerRegistration reg, Table table,
            HttpServletRequest request) throws Exception {
        String strMode = request.getParameter("mode");
        if ("INSERT".equals(strMode)) {
            insertData(reg, table, request);
        } else if ("UPDATE".equals(strMode)) {
            updateData(reg, table, request);
        } else {
            deleteData(reg, table, request);
        }
    }

    private void updateData(ServerRegistration reg, Table table,
            HttpServletRequest request) throws Exception {
        ODBCProvider odbc = null;
        java.sql.Connection con = null;
        PreparedStatement stmt = null;
        ArrayList columnsInclude = null;
        ArrayList columnsEdit = null;
        int ret;
        try {
            columnsInclude = table.getDisplayedColumns();
            columnsEdit = table.getUpdateColumns();
            odbc = ODBCProviderFactory.getInstance().getODBCProvider(
                    reg.getOdbcTypeID());
            con = DBUtil.getConnection(odbc.getConnectionClass(), reg
                    .getUsername(), reg.getPassword(), reg.getConnectionURL());
            //1 - build preparedstatement
            stmt = con.prepareStatement(createUpdateStatement(table,
                    columnsInclude, columnsEdit));
            //2 - retieve each value and validate
            for (int i = 0; i < columnsEdit.size(); i++) {
                Column column = (Column) columnsEdit.get(i);
                //preparedstatement is 1 based
                setValue(stmt, i + 1, column.getColumnType(), request
                        .getParameter("new_" + column.getName()));
            }
            for (int i = 0; i < columnsInclude.size(); i++) {
                Column column = (Column) columnsInclude.get(i);
                //preparedstatement is 1 based
                setValue(stmt, i + 1 + columnsEdit.size(), column
                        .getColumnType(), request.getParameter("old_"
                        + column.getName()));
            }
            ret = stmt.executeUpdate();
            logger.debug("[" + ret + "] rows update");
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            throw new Exception("NumberFormatException - " + e.getMessage());
        } catch (SQLException e) {
            SQLException temp = e;
            while (temp != null) {
                // do handling
                logger.error(e.getMessage());
                temp = temp.getNextException();
            }
            SQLWarning sqlw = con.getWarnings();
            while (sqlw != null) {
                logger.error(sqlw.getMessage());
                sqlw = sqlw.getNextWarning();
            }
            con.clearWarnings();

            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (con != null) con.close();
        }
        //3 - set each value in the prepared statement
        //4 - run the query
    }

    private void insertData(ServerRegistration reg, Table table,
            HttpServletRequest request) throws Exception {

    }

    private void deleteData(ServerRegistration reg, Table table,
            HttpServletRequest request) throws Exception {

    }

    public String createUpdateStatement(Table table, ArrayList columnsInclude,
            ArrayList columnsEdit) {
        StringBuffer sbStatement = new StringBuffer();
        sbStatement.append("UPDATE ");
        sbStatement.append(table.getName());
        sbStatement.append(" SET ");
        for (int i = 0; i < columnsEdit.size(); i++) {

            sbStatement.append(((Column) columnsEdit.get(i)).getName());
            sbStatement.append("=?");
            sbStatement.append(i == columnsEdit.size() - 1 ? "" : ",");
        }
        sbStatement.append(" WHERE ");

        for (int i = 0; i < columnsInclude.size(); i++) {
            sbStatement.append(((Column) columnsInclude.get(i)).getName());
            sbStatement.append("=?");
            sbStatement.append(i == columnsInclude.size() - 1 ? "" : " AND ");
        }
        logger.debug(sbStatement);
        return sbStatement.toString();
    }

    public void setValue(PreparedStatement stmt, int position, int sqlDataType,
            String data) throws Exception {
        if (data == null) {
            stmt.setNull(position, sqlDataType);
            logger.debug(position + " : [null]");
        } else {

            switch (sqlDataType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                stmt.setString(position, data);
                break;
            case Types.TINYINT:
            case Types.INTEGER:
            case Types.BIGINT:
                stmt.setInt(position, Integer.parseInt(data));
                break;
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.NUMERIC:
            case Types.REAL:
                stmt.setBigDecimal(position, new BigDecimal(data));
                break;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                stmt.setTimestamp(position, new Timestamp((new Date(data))
                        .getTime()));
                break;
            default:
                logger.debug("Unknow datatype[" + sqlDataType
                        + "] - using String");
                stmt.setString(position, data);
                break;

            }
            logger.debug(position + " : " + data);
        }
    }
}
