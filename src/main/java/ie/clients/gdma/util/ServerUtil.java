package ie.clients.gdma.util;

import ie.clients.gdma.GdmaFacade;
import ie.clients.gdma.dao.ServerDao;
import ie.clients.gdma.dao.TableDao;
import ie.clients.gdma.dao.UserAccessDao;
import ie.clients.gdma.domain.AuditRecord;
import ie.clients.gdma.domain.Column;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.domain.UserAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    private UserAccessDao userAccessDao;
    

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
    
    public void setUserAccessDao(UserAccessDao userAccessDao) {
        Assert.notNull(userAccessDao, "userAccessDao is null");
        this.userAccessDao = userAccessDao;
    }
  
    /**
     * This method will get the list of tables from the database and then
     * iterate over the list of tables form the server object. It will see if
     * any are missing,. If there is, then it will create a new table object and
     * save it.
     * 
     * @param server
     */
    @SuppressWarnings("null")
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

            Set<Table> tables = server.getTables(); //Get list(set) of tables from the GDMA2_TABLE table
            Table[] tableArray = new Table[tables.size()];
            tableArray = tables.toArray(tableArray); //Copy tables into an array 
            tables.clear(); //Clear the list(set) of tables
            List<String> tableNamesInDB = new ArrayList<String>(); //create a list to hold the names of the tables in the 'maintained' database
            resultSet = statement.executeQuery(server.getConnectionType().getSQLGetTables());//get the list of tables in the 'maintained' DB
            
            int numTableswInDB = 0;
            outerloop: while (resultSet != null && resultSet.next()) { //Loop through the list of tables in the 'maintained' DB
                String tableName = resultSet.getString(1);                
                tableNamesInDB.add(tableName);
                if (tableName != null) {
                    // see if it's already in the list
                    for (int i = 0; i < tableArray.length; i++) {
                        if (tableName.equals(tableArray[i].getName()) && tableArray[i].isActive()) {
                            // yes, so add it back
                            tables.add(tableArray[i]);
                            numTableswInDB++;
                            continue outerloop;
                        } 
                        if (tableName.equals(tableArray[i].getName()) && !tableArray[i].isActive()) {
                        	LOG.debug("Server:[" + server.getName() + "], Table:[" + tableName + "] (inactive) found ");
                        	Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                        	String timestamp = currentTimestamp.toString();

                        	tableArray[i].setName(tableArray[i].getName() + "_" + timestamp);
                        	tables.add(tableArray[i]);
                        	Table table = new Table();
                            table.setName(tableName);
                            table.setServerId(server.getId());
                            table.setActive(true);
                            tables.add(table);
                            numTableswInDB++;
                            continue outerloop;
                        }
                    }                                   
                    
                    // no so create a new table record
                    LOG.debug("Server:[" + server.getName() + "], Table:[" + tableName + "] Not found ");
                    Table table = new Table();
                    table.setName(tableName);
                    table.setServerId(server.getId());
                    table.setActive(true);
                    tables.add(table);
                }
                numTableswInDB++;
            } 
            
            //outerloop2: for (int i = 0; i < tableArray.length; i++) {
            
            int i = 0;
            outerloop2: while (i < tableArray.length) { //loop through the tables in the tableArray (GDMA2_TABLE table)
                int foundTable = 0;
            	for(int j = 0; j < numTableswInDB; j++){
                	if (tableNamesInDB.get(j).equals(tableArray[i].getName())) {
                        // yes, so add it back
                        foundTable = 1;
                        i++;
                        continue outerloop2;
                    }                 	
                }
            	
            	if(foundTable == 0){
            		//If the table has been deleted from the 'maintained' database then make it inactive in GDMA
            		//so that it is unavailable to users but remains in the database for auditing purposes
            		tableArray[i].setActive(false);
            		tables.add(tableArray[i]);
            		Long tid = tableArray[i].getId();
            		List<UserAccess> userAccesses = userAccessDao.get(tid);
            		for(UserAccess ua: userAccesses){
            			userAccessDao.delete(ua);
            		} 
            		
            		List<Long> columnNamesInTable = new ArrayList<Long>();
            		Set<Column> columnsToCheckFor = tableArray[i].getColumns();
            		for(Column c: columnsToCheckFor){
            			columnNamesInTable.add(c.getId());
        			}              		
            		
            		Set<Table> allTables = server.getTables();    		
            		for(Table t: allTables){
            			Set<Column> columns = t.getColumns();
            			
            			for(Column c: columns){
            				for(int j = 0; j < columnNamesInTable.size(); j ++){
            					if(c.getDropDownColumnDisplay() != null && (c.getDropDownColumnDisplay().getId() == columnNamesInTable.get(j))){
                						c.setDropDownColumnDisplay(null);
                						c.setDropDownColumnStore(null);
                				}  
                				if(c.getDropDownColumnStore() != null && (c.getDropDownColumnStore().getId() == columnNamesInTable.get(j))){
                					c.setDropDownColumnDisplay(null);
            						c.setDropDownColumnStore(null);
                				} 
                				LOG.debug("Removed foreign key reference from column " + c.getName() + " in table " + t.getName());
            				}            				
            			}
            		}
            	}
            	i++;
            }            
            
            
            LOG.debug("*************Server details: " + server.toString());
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
                String tableNameString = resultSetMetaData.getTableName(i);
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
                        
                        column.setActive(true);
                        
                        column.setColumnSize(resultSetMetaData.getColumnDisplaySize(i));                        
                        columns.add(column);
                    } else {                    	
                    	if (columnName.equals(column.getName()) && tableNameString.equals(table.getName().trim()) && column.isActive()) {
                    		// update type - just in case
                            column.setColumnType(resultSetMetaData.getColumnType(i));
                            column.setColumnTypeString(resultSetMetaData.getColumnTypeName(i));
                            column.setNullable(resultSetMetaData.isNullable(i) == ResultSetMetaData.columnNullable);
                            column.setColumnSize(resultSetMetaData.getColumnDisplaySize(i));
                            column.setActive(true);
                        }else if(columnName.equals(column.getName()) && tableNameString.equals(table.getName().trim()) && !column.isActive()){
                        	LOG.debug("Server:[" + server.getName() + "], Table:[" + table.getName() + "], Column:[ " + column.getName() + " (inactive) found ");
                        	Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                        	String timestamp = currentTimestamp.toString();

                        	String inactiveColumnName = column.getName() + "_" + timestamp;
                        	String activeColumnName = column.getName();
                        	column.setName(inactiveColumnName);
                        	column.setActive(false);
                        	columns.add(column);
                        	Column c = new Column();
                            c.setName(activeColumnName);
                            c.setTable(table);
                            c.setActive(true);
                            c.setAllowInsert(true);
                            c.setAllowUpdate(true);
                            c.setDisplayed(true);
                            c.setColumnType(resultSetMetaData.getColumnType(i));
                            c.setColumnTypeString(resultSetMetaData.getColumnTypeName(i));
                            c.setNullable(resultSetMetaData.isNullable(i) == ResultSetMetaData.columnNullable);
                            c.setColumnSize(resultSetMetaData.getColumnDisplaySize(i));
                            c.setSpecial("N");
                            
                            columns.add(c);                            
                    	}
                    }
                }
            }
            //tableDao.save(table);
            // now sync with column names to make sure none were deleted & reset
            // the orderby
            int idx = 0;
            for (Column column : columns) {
                if (!columnNames.contains(column.getName())) {
                    //columns.remove(column);
                	column.setDisplayed(false);
                	column.setActive(false);
                	Set<Table> allTables = server.getTables();    		
            		for(Table t: allTables){
            			Set<Column> tableColumns = t.getColumns();
            			for(Column c: tableColumns){
            				if(c.getDropDownColumnDisplay() != null && (c.getDropDownColumnDisplay().getId() == column.getId())){
            					c.setDropDownColumnStore(null);
                				c.setDropDownColumnDisplay(null);
                				tableDao.save(t);
                				LOG.debug("Removed foreign key reference from column " + c.getName() + " in table " + t.getName());
                			}  
                			if(c.getDropDownColumnStore() != null && (c.getDropDownColumnStore().getId() == column.getId())){
                				c.setDropDownColumnStore(null);
                				c.setDropDownColumnDisplay(null);
                				tableDao.save(t);
                				LOG.debug("Removed foreign key reference from column " + c.getName() + " in table " + t.getName());
                			}                 			            				
            			}
            		}
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
