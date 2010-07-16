package ie.clients.gdma.dao.jdbc;

import ie.clients.gdma.GdmaFacade;
import ie.clients.gdma.audit.Auditable;
import ie.clients.gdma.dao.DynamicDao;
import ie.clients.gdma.domain.Column;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.domain.User;
import ie.clients.gdma.util.DataSourcePool;
import ie.clients.gdma.util.Formatter;
import ie.clients.gdma.util.PagedResultSetExtractor;
import ie.clients.gdma.util.RowMapper;
import ie.clients.gdma.util.SqlUtil;
import ie.clients.gdma.web.command.ColumnUpdate;
import ie.clients.gdma.web.command.Filter;
import ie.clients.gdma.web.command.PaginatedRequest;
import ie.clients.gdma.web.command.PaginatedResponse;
import ie.clients.gdma.web.command.PaginatedSqlRequest;
import ie.clients.gdma.web.command.PaginatedSqlResponse;
import ie.clients.gdma.web.command.UpdateRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

public class DynamicDaoImpl implements DynamicDao {

	private GdmaFacade gdmaFacade;

	private DataSourcePool dataSourcePool;

	protected static Logger LOG = Logger.getLogger(DynamicDaoImpl.class);

	/**
	 * Property setter method for spring
	 * 
	 * @param gdmaFacade
	 */
	public void setGdmaFacade(GdmaFacade gdmaFacade) {
		Assert.notNull(gdmaFacade, "a null gdmaFacade was passed to GdmaAdminAjaxFacade");
		this.gdmaFacade = gdmaFacade;
	}

	/**
	 * Property setter method for spring
	 * 
	 * @param dataSourcePool
	 */
	public void setDataSourcePool(DataSourcePool dataSourcePool) {
		Assert.notNull(dataSourcePool, "a null dataSourcePool was passed to GdmaAdminAjaxFacade");
		this.dataSourcePool = dataSourcePool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ie.clients.gdma.dao.DynamicDao#get(ie.clients.gdma.web.command.
	 * PaginatedRequest)
	 */
	public PaginatedResponse get(PaginatedRequest paginatedRequest) {
		Server server = gdmaFacade.getServerDao().get(paginatedRequest.getServerId());
		Table table = gdmaFacade.getTableDao().get(paginatedRequest.getTableId());
		Column sortedByColumnId = paginatedRequest.getSortedByColumnId() == null ? null : gdmaFacade.getColumnDao().get(paginatedRequest.getSortedByColumnId());

		String dir = PaginatedRequest.YUI_ASC.equals(paginatedRequest.getDir()) ? "asc" : "desc";

		String sql = SqlUtil.createSelect(server, table, sortedByColumnId, dir, paginatedRequest.getFilters());

		if (LOG.isDebugEnabled()) {
			LOG.debug("SQL USED: " + sql);
		}

		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(sql);

		declareSqlParameters(psc, paginatedRequest.getFilters());

		psc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		psc.setUpdatableResults(false);

		// don't need transacton manager for lookup
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getTransactionManager(server).getDataSource());

		PaginatedResponse paginatedResponse = new PaginatedResponse();

		final List<Object> params = convertFiltersToSqlParameterValues(paginatedRequest.getFilters());

		paginatedResponse.setRecords((List) jdbcTemplate.query(psc.newPreparedStatementCreator(params), new PagedResultSetExtractor(new RowMapper(),
		        paginatedRequest.getRecordOffset(), paginatedRequest.getRowsPerPage())));
		paginatedResponse.setTotalRecords(getCount(server, table, paginatedRequest.getFilters()));
		paginatedResponse.setStartIndex(paginatedRequest.getRecordOffset());
		paginatedResponse.setKey("" + paginatedRequest.getSortedByColumnId());
		paginatedResponse.setSortDir(paginatedRequest.getDir());

		return paginatedResponse;
	}

	private void declareSqlParameters(PreparedStatementCreatorFactory psc, List<Filter> filters) {
		for (Filter filter : filters) {
			if (filter.isNullValue() || filter.isBlank())
				continue;
			if (StringUtils.hasText(filter.getFilterValue())) {
				psc.addParameter(new SqlParameter(filter.getColumnType()));
			}
		}
	}

	private List<Object> convertFiltersToSqlParameterValues(List<Filter> filters) {
		final List<Object> params = new ArrayList<Object>();
		for (Filter filter : filters) {
			if (filter.isNullValue() || filter.isBlank())
				continue;

			if (StringUtils.hasText(filter.getFilterValue())) {
				Object param = filter.getFilterValue();
				if (SqlUtil.isNumeric(filter.getColumnType())) {
					LOG.debug("Number as string as parameter: " + param);
				} else if (SqlUtil.isDate(filter.getColumnType())) {
					try {
						param = new java.sql.Date(Formatter.parseDate(filter.getFilterValue()).getTime());
						LOG.debug("Date as parameter: " + param);
					} catch (Exception ex) {
						LOG.error("Could not parse the date: " + filter.getFilterValue(), ex);
					}
				} else {
					// For LIKE stmt
					param = "%" + param + "%";
					LOG.debug("Generic parameter: " + param);
				}
				params.add(param);
			}
		}
		return params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ie.clients.gdma.dao.DynamicDao#getCount(ie.clients.gdma.domain.Server ,
	 * ie.clients.gdma.domain.Table, java.util.List)
	 */
	public Long getCount(Server server, Table table, List<Filter> filters) {
		// TODO optimise!!

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getTransactionManager(server).getDataSource());

		final String sql = SqlUtil.createCount(server, table, filters);

		if (LOG.isDebugEnabled()) {
			LOG.debug("SQL USED for count: " + sql);
		}

		return jdbcTemplate.queryForLong(sql, convertFiltersToSqlParameterValues(filters).toArray());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ie.clients.gdma.dao.DynamicDao#addRecord(ie.clients.gdma.web.command
	 * .UpdateRequest)
	 */
	@Auditable
	public void addRecord(UpdateRequest updateRequest) {
		final Server server = gdmaFacade.getServerDao().get(updateRequest.getServerId());
		final Table table = gdmaFacade.getTableDao().get(updateRequest.getTableId());
		final List<List<ColumnUpdate>> columnUpdates = updateRequest.getUpdates();

		final DataSourceTransactionManager transactionManager = dataSourcePool.getTransactionManager(server);
		TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);

		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@SuppressWarnings("unchecked")
			public void doInTransactionWithoutResult(TransactionStatus status) {
				for (List<ColumnUpdate> list : columnUpdates) {

					List<Column> columns = new ArrayList<Column>();
					final List parameters = new ArrayList();

					for (ColumnUpdate columnUpdate : list) {
						Column column = gdmaFacade.getColumnDao().get(columnUpdate.getColumnId());
						columns.add(column);

						parameters.add(SqlUtil.convertToType(columnUpdate.getNewColumnValue(), column.getColumnType()));
					}
					handleSpecialColumns(table.getColumns(), columns, parameters);
					final String sql = SqlUtil.createInsertStatement(server, table, columns);

					if (LOG.isDebugEnabled()) {
						LOG.debug("SQL USED: " + sql);
                        LOG.debug("parameters: " + parameters);
					}

					JdbcTemplate jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
					jdbcTemplate.update(sql, parameters.toArray());

				}

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ie.clients.gdma.dao.DynamicDao#updateRecords(ie.clients.gdma.web.
	 * command.UpdateRequest)
	 */
	@Auditable
	public int updateRecords(UpdateRequest updateRequest) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateRecords(updateRequest=" + updateRequest
					+ ") - start");
		}

		final Server server = gdmaFacade.getServerDao().get(updateRequest.getServerId());
		final Table table = gdmaFacade.getTableDao().get(updateRequest.getTableId());
		final List<List<ColumnUpdate>> columnUpdates = updateRequest.getUpdates();

		final DataSourceTransactionManager transactionManager = dataSourcePool.getTransactionManager(server);
		TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);

		int countDeleted = (Integer) txTemplate.execute(new TransactionCallback() {
			@SuppressWarnings("unchecked")
			public Object doInTransaction(TransactionStatus status) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("doInTransaction(status=" + status
									+ ") - start");
						}

				int countDeleted = 0;
				for (List<ColumnUpdate> list : columnUpdates) {

					List<Column> columns = new ArrayList<Column>();
					final List parameters = new ArrayList();
					List keys = new ArrayList();

					if (CollectionUtils.isEmpty(list)) {
						throw new InvalidDataAccessResourceUsageException("Update list is empty");
					}

					for (ColumnUpdate columnUpdate : list) {
						Column column = gdmaFacade.getColumnDao().get(columnUpdate.getColumnId());
						if (column.isPrimarykey()) {
							columns.add(column);
							keys.add(SqlUtil.convertToType(columnUpdate.getOldColumnValue(), column.getColumnType()));
						} else {
							if (columnUpdate.getNewColumnValue() != null) {
								columns.add(column);
								Object o = SqlUtil.convertToType(columnUpdate.getNewColumnValue(), column.getColumnType());
								if (o == null && !column.isNullable()) {
									throw new InvalidDataAccessResourceUsageException("Column " + column.getName()
									        + " can not be set to null and must have a value");
								}
								parameters.add(o);
								if (o == null){
									LOG.debug("o is null");
								} else {
								LOG.debug("Added parameter " + o.toString());
								}
							}
						}
					}
					if (CollectionUtils.isEmpty(parameters)) {
						throw new InvalidDataAccessResourceUsageException("No update values found");
					}

                    if (null == keys || keys.isEmpty()) {
                        throw new InvalidDataAccessResourceUsageException("Update Not possible as no Table Primary Key Set.  Please contact your administrator");
                    }
					
					handleSpecialColumns(table.getColumns(), columns, parameters);
					parameters.addAll(keys);

					String sql = SqlUtil.createUpdateStatement(server, table, columns);

					if (LOG.isDebugEnabled()) {
                        LOG.debug("SQL USED: " + sql);
                        LOG.debug("parameters: " + parameters);
					}

					JdbcTemplate jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
					countDeleted += jdbcTemplate.update(sql, parameters.toArray());

				}

						if (LOG.isDebugEnabled()) {
							LOG.debug("doInTransaction() - end - return value="
									+ countDeleted);
						}
				return countDeleted;
			}
		});

		if (LOG.isDebugEnabled()) {
			LOG.debug("updateRecords() - end - return value=" + countDeleted);
		}
		return countDeleted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ie.clients.gdma.dao.DynamicDao#deleteRecords(ie.clients.gdma.web.
	 * command.UpdateRequest)
	 */
	@Auditable
	public int deleteRecords(UpdateRequest updateRequest) {
		final Server server = gdmaFacade.getServerDao().get(updateRequest.getServerId());
		final Table table = gdmaFacade.getTableDao().get(updateRequest.getTableId());
		final List<List<ColumnUpdate>> columnUpdates = updateRequest.getUpdates();

		final DataSourceTransactionManager transactionManager = dataSourcePool.getTransactionManager(server);
		TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);

		int countDeleted = (Integer) txTemplate.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				int countDeleted = 0;
				for (List<ColumnUpdate> list : columnUpdates) {

					List<Column> columns = new ArrayList<Column>();
					final List keys = new ArrayList();
					if (CollectionUtils.isEmpty(list)) {
						throw new InvalidDataAccessResourceUsageException("Cannot delete records as this table does not have a primary key set");
					}

					for (ColumnUpdate columnUpdate : list) {
						Column column = gdmaFacade.getColumnDao().get(columnUpdate.getColumnId());
						columns.add(column);
						if (column.isPrimarykey()) {
							keys.add(SqlUtil.convertToType(columnUpdate.getOldColumnValue(), column.getColumnType()));
						}
					}

					final String sql = SqlUtil.createDeleteStatement(server, table, columns);

					if (LOG.isDebugEnabled()) {
						LOG.debug("SQL USED    : " + sql);
						LOG.debug("VALUES USED : " + keys);
					}

					JdbcTemplate jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
					countDeleted += jdbcTemplate.update(sql, keys.toArray());

				}
				return countDeleted;
			}
		});

		return countDeleted;
	}

	@SuppressWarnings("unchecked")
	private void handleSpecialColumns(Set<Column> set, List<Column> columns, List parameters) {
		for (Column column : set) {
			if (StringUtils.hasText(column.getSpecial())) {
				if ("U".equals(column.getSpecial())) {
					String user = "";
					SecurityContext securityContext = SecurityContextHolder.getContext();
					Authentication authentication = securityContext.getAuthentication();
					if (authentication != null && authentication.getPrincipal() instanceof User) {
						user = ((User) authentication.getPrincipal()).getUserName();
					} else {
						user = "UNKNOWN";
					}
					// first see if by error the column is already included
					if (columns.contains(column)) {
						int index = columns.indexOf(column);
						parameters.set(index, SqlUtil.convertToType(user, column.getColumnType()));
					} else {
						columns.add(column);
						parameters.add(SqlUtil.convertToType(user, column.getColumnType()));
					}
				} else if ("D".equals(column.getSpecial())) {
					// first see if by error the column is already included
					if (columns.contains(column)) {
						int index = columns.indexOf(column);
						parameters.set(index, SqlUtil.convertToType(Formatter.formatDate(new Date()), column.getColumnType()));
					} else {
						columns.add(column);
						// can't be guaranteed that the column is a dated column
						// so convert to string
						parameters.add(SqlUtil.convertToType(Formatter.formatDate(new Date()), column.getColumnType()));
					}
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ie.clients.gdma.dao.DynamicDao#getDropDownData(ie.clients.gdma.domain
	 * .Column, ie.clients.gdma.domain.Column)
	 */
	public List getDropDownData(Column display, Column store) {

		Server server = gdmaFacade.getServerDao().getByColumn(store.getId());

		String sql = SqlUtil.createDropDownSelect(server, server.getTables().iterator().next(), display, store);

		if (LOG.isDebugEnabled()) {
			LOG.debug("SQL USED: " + sql);
		}

		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(sql);
		psc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		psc.setUpdatableResults(false);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getTransactionManager(server).getDataSource());

		return (List) jdbcTemplate.query(psc.newPreparedStatementCreator(Collections.EMPTY_LIST), new RowMapper());
	}

	public PaginatedSqlResponse executeSelect(PaginatedSqlRequest paginatedSqlRequest) {
		Server server = gdmaFacade.getServerDao().get(paginatedSqlRequest.getServerId());
		String sql = paginatedSqlRequest.getSql().trim();
		if (LOG.isDebugEnabled()) {
			LOG.debug("SQL USED: " + sql);
		}

		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(sql);
		psc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		psc.setUpdatableResults(false);

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getTransactionManager(server).getDataSource());

		PaginatedSqlResponse paginatedSQlResponse = new PaginatedSqlResponse();
		paginatedSQlResponse.setRecords((List) jdbcTemplate.query(psc.newPreparedStatementCreator(Collections.EMPTY_LIST), new PagedResultSetExtractor(
		        new RowMapper(), paginatedSqlRequest.getRecordOffset(), paginatedSqlRequest.getRowsPerPage())));

		// paginatedResponse.setTotalRecords(getCount(server, table,
		// paginatedSqlRequest.getFilters()));
		paginatedSQlResponse.setStartIndex(0);

		return paginatedSQlResponse;
	}

	public List<Column> executeSelectGetColumns(Long serverId, String sql) {
		Server server = gdmaFacade.getServerDao().get(serverId);
		sql = sql.trim();
		// find the where, ditch it and add 'where 1 = 0
		Pattern p = Pattern.compile("[\\s]where[\\s]", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher(sql);

		if (m.find()) {
			int start = m.start();
			sql = sql.substring(0, start) + " where 1 = 0";
		} else {
			sql += " where 1 = 0";
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("SQL USED: " + sql);
		}
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Column> columns = new ArrayList<Column>();

		try {
			DataSource dataSource = dataSourcePool.getTransactionManager(server).getDataSource();
			connection = dataSource.getConnection();

			statement = connection.createStatement();

			resultSet = statement.executeQuery(sql);

			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
				Column column = new Column();
				column.setId(i - 1L);
				column.setName(resultSetMetaData.getColumnLabel(i));
				columns.add(column);
			}

		} catch (Throwable e) {
			LOG.error(e, e);
			e.printStackTrace();
		} finally {
			JdbcUtils.closeResultSet(resultSet);
			JdbcUtils.closeStatement(statement);
			JdbcUtils.closeConnection(connection);
		}
		return columns;
	}

	public int bulkImport(Server server, Table table, InputStream data) throws IOException {
		final CSVReader rdr = new CSVReader(new InputStreamReader(data));
		final String[] headers = rdr.readNext();
		LOG.debug("Headers starting with: " + headers[0]);
		int counter = 0;
		if (headers != null) {

			final Set<Column> columns = table.getColumns();
			final List<SqlParameter> params = new ArrayList<SqlParameter>();
			String tableList = null;
			String patternList = null;
			for (String h : headers) {
				h = h.trim();
				Column theColumn = null;
				for (Column c : columns) {
					if (c.getName().equals(h)) {
						theColumn = c;
						break;
					}
				}
				if (theColumn == null)
					throw new IOException("The column \"" + h + "\" does not exist in " + table.getName());
				
	            LOG.debug("Column is of type " + theColumn.getColumnTypeString());

				if (tableList == null) {
					tableList = "\"" + h + "\"";
					patternList = "?";
				} else {
					tableList += ",\"" + h + "\"";
					patternList += ",?";
				}
				// using varchar because all values from CSV are strings
			//	params.add(new SqlParameter(Types.VARCHAR));
			//bh changing this as it was breaking date types
				params.add(new SqlParameter(theColumn.getColumnType()));
			}

			final String sql = "INSERT INTO " + table.getName() + " (" + tableList + ") VALUES (" + patternList + ")";
			LOG.debug("Preparing sql: [" + sql + "]");
			final PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(sql, params);

			String[] row = rdr.readNext();
			final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getTransactionManager(server).getDataSource());

			try {
				while (row != null) {
					LOG.debug("Row starting with: " + row[0] + "," + row[1] + "," + row[2]+ "," + row[3]+ "," + row[4]+ "," + row[5]);
					jdbcTemplate.update(sql, psc.newPreparedStatementSetter(row));
					counter++;
					row = rdr.readNext();
				}
			} catch (DataAccessException ex) {
				throw new IOException("Could not import data:" + ex.getMessage(), ex);
			}
		}
		return counter;
	}
}
