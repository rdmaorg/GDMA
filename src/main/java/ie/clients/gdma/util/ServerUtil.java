package ie.clients.gdma.util;

import ie.clients.gdma.dao.ServerDao;
import ie.clients.gdma.dao.TableDao;
import ie.clients.gdma.domain.Column;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


public class ServerUtil {
    private static Logger LOG = Logger.getLogger(ServerUtil.class);

    private DataSourcePool dataSourcePool;

    private ServerDao serverDao;

    private TableDao tableDao;

    public void setDataSourcePool(DataSourcePool dataSourcePool) {
        Assert.notNull(dataSourcePool, "dataSourcePool is null");
        this.dataSourcePool = dataSourcePool;
    }

    public void setServerDao(ServerDao serverDao) {
        Assert.notNull(serverDao, "serverDao is null");
        this.serverDao = serverDao;
    }

    public void setTableDao(TableDao tableDao) {
        Assert.notNull(tableDao, "tableDao is null");
        this.tableDao = tableDao;
    }

    /**
     * This method will get the list of tables from the database and then
     * iterate over the list of tables form the server object. It will see if
     * any are missing,. If there is, then it will create a new table object and
     * save it.
     * 
     * @param server
     */
    public void resyncTableList(Server server) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            DataSource dataSource = dataSourcePool.getTransactionManager(server).getDataSource();
            // TODO make this better
            if (dataSource == null)
                return;
            connection = dataSource.getConnection();

            statement = connection.createStatement();

            Set<Table> tables = server.getTables();
            Table[] tableArray = new Table[tables.size()];
            tableArray = tables.toArray(tableArray);
            tables.clear();

            resultSet = statement.executeQuery(server.getConnectionType().getSQLGetTables());

            outerloop: while (resultSet != null && resultSet.next()) {
                String tableName = resultSet.getString(1);
                if (tableName != null) {
                    // see if it's already in the list
                    for (int i = 0; i < tableArray.length; i++) {
                        if (tableName.equals(tableArray[i].getName())) {
                            // yes, so add it back
                            tables.add(tableArray[i]);
                            continue outerloop;
                        }
                    }
                    // no so create a new table record
                    LOG.debug("Server:[" + server.getName() + "], Table:[" + tableName + "] Not found ");
                    Table table = new Table();
                    table.setName(tableName);
                    table.setDisplayed(true);
                    table.setAllowDelete(false);
                    tables.add(table);
                }
            }
            serverDao.save(server);
        } catch (Exception e) {
            LOG.error(e, e);
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }

    }

    /**
     * This method will get the list of columns from the database and then
     * iterate over the list of columns from the table object. It will see if
     * any are missing,. If there is, then it will create a new column object
     * and save it.
     * 
     * @param table
     */
    public void resyncColumnList(Server server, Table table) {
        LOG.info("Calling resyncColumnList!!");
        // TODO tidy all this up !!
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            DataSource dataSource = dataSourcePool.getTransactionManager(server).getDataSource();
            // TODO make this better
            if (dataSource == null)
                return;
            connection = dataSource.getConnection();

            statement = connection.createStatement();

            Set<Column> columns = table.getColumns();
            // this is used to keep track of the names - so we can check at the
            // end if we have a column that doesn't exist anymore
            ArrayList<String> columnNames = new ArrayList<String>();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select * from ");
            if (StringUtils.hasText(server.getPrefix())) {
                stringBuilder.append(server.getPrefix());
                stringBuilder.append('.');
            }
            stringBuilder.append(table.getName());
            stringBuilder.append("  where 1 = 0");
            resultSet = statement.executeQuery(stringBuilder.toString());

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                columnNames.add(columnName);
                if (columnName != null) {
                    // see if it's already in the list
                    Column column = getColumn(columnName, columns);
                    if (column == null) {
                        // no so create a new column record
                        column = new Column();
                        column.setName(columnName);
                        column.setColumnType(resultSetMetaData.getColumnType(i));
                        column.setColumnTypeString(resultSetMetaData.getColumnTypeName(i));
                        column.setAllowInsert(true);
                        column.setAllowUpdate(true);
                        column.setDisplayed(true);
                        column.setNullable(resultSetMetaData.isNullable(i) == ResultSetMetaData.columnNullable);
                        column.setSpecial("N");
                        
                        
                        column.setColumnSize(resultSetMetaData.getColumnDisplaySize(i));
                        
                        columns.add(column);
                    } else {
                        // update type - just in case
                        column.setColumnType(resultSetMetaData.getColumnType(i));
                        column.setColumnTypeString(resultSetMetaData.getColumnTypeName(i));
                        column.setNullable(resultSetMetaData.isNullable(i) == ResultSetMetaData.columnNullable);
                        column.setColumnSize(resultSetMetaData.getColumnDisplaySize(i));
                    }
                }
            }

            // now sync with column names to make sure none were deleted & reset
            // the orderby
            int idx = 0;
            for (Column column : columns) {
                if (!columnNames.contains(column.getName())) {
                    columns.remove(column);
                } else {
                    column.setOrderby(idx);
                    idx++;
                }
            }

            tableDao.save(table);
        } catch (Throwable e) {
            LOG.error(e, e);
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }

    }

    private Column getColumn(String columnName, Set<Column> columns) {
        for (Column column : columns) {
            if (columnName.equals(column.getName())) {
                return column;
            }
        }
        return null;

    }
}
