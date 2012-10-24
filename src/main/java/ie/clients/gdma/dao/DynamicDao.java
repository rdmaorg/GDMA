package ie.clients.gdma.dao;

import ie.clients.gdma.domain.Column;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.web.command.Filter;
import ie.clients.gdma.web.command.PaginatedRequest;
import ie.clients.gdma.web.command.PaginatedResponse;
import ie.clients.gdma.web.command.PaginatedSqlRequest;
import ie.clients.gdma.web.command.PaginatedSqlResponse;
import ie.clients.gdma.web.command.UpdateRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DynamicDao {

	/**
	 * @param paginatedRequest
	 * @return
	 */
	public PaginatedResponse get(PaginatedRequest paginatedRequest);

	/**
	 * @param server
	 * @param table
	 * @return
	 */
	public Long getCount(Server server, Table table, List<Filter> filters);

	/**
	 * @param updateRequest
	 */
	public void addRecord(UpdateRequest updateRequest);

	/**
	 * Update one or more records
	 * 
	 * @param updateRequest
	 * @return the numebr of records updated
	 */
	public int updateRecords(UpdateRequest updateRequest);

	/**
	 * Delete one or more records
	 * 
	 * @param updateRequest
	 * @return the number of records delete
	 */
	public int deleteRecords(UpdateRequest updateRequest);

	/**
	 * Get a list of drop down values and text for a column
	 * 
	 * @param display
	 *            the display column
	 * @param store
	 *            the column to store
	 * @return
	 */
	public List getDropDownData(Column display, Column store);

	/**
	 * Get the list of columns for a select through the SQL Console
	 * 
	 * @param serverId
	 * @param sql
	 * @return
	 */
	public List<Column> executeSelectGetColumns(Long serverId, String sql);

	/**
	 * Execute a select from the SQL console
	 * 
	 * @param paginatedRequest
	 * @return
	 */
	public PaginatedSqlResponse executeSelect(PaginatedSqlRequest paginatedSqlRequest);

	/**
	 * Perform bulk data import
	 * 
	 * @param server
	 * @param table
	 * @param data
	 * @return number of rows imported
	 * @throws IOException
	 */
	public int bulkImport(Server server, Table table, InputStream data) throws IOException;

}
