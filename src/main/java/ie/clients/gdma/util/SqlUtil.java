package ie.clients.gdma.util;

import ie.clients.gdma.domain.Column;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.web.command.Filter;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.util.StringUtils;

/**
 * @author RGILL
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class SqlUtil {

	private static Logger LOG = Logger.getLogger(SqlUtil.class);

	public static String createSelect(Server server, Table table, Column sortColumn, String dir, List<Filter> filters) {
		StringBuilder stringBuilder = new StringBuilder(table.getColumns().size() * 20);
		String tableName = table.getName();
		boolean addComma = false;

		stringBuilder.append("SELECT ");

		for (Column column : table.getColumns()) {
			if (column.isDisplayed()) {
				if (addComma) {
					stringBuilder.append(", ");
				} else {
					addComma = true;
				}
				stringBuilder.append(tableName);
				stringBuilder.append('.');
				stringBuilder.append(column.getName());
			}
		}

		stringBuilder.append(" FROM ");
		if (StringUtils.hasText(server.getPrefix())) {
			stringBuilder.append(server.getPrefix());
			stringBuilder.append('.');
		}

		stringBuilder.append(tableName);

		String whereClause = createFilterWhereClause(table, filters);
		if (whereClause.length() > 0) {
			stringBuilder.append(" WHERE ");
			stringBuilder.append(whereClause);
		}

		stringBuilder.append(createOrderbyClause(table, sortColumn, dir));

		return stringBuilder.toString();
	}

	public static String createDropDownSelect(Server server, Table table, Column display, Column store) {

		StringBuilder stringBuilder = new StringBuilder(60);
		String tableName = table.getName();

		stringBuilder.append("SELECT ");

		stringBuilder.append(tableName);
		stringBuilder.append('.');
		stringBuilder.append(store.getName());
		stringBuilder.append(", ");
		stringBuilder.append(tableName);
		stringBuilder.append('.');
		stringBuilder.append(display.getName());

		stringBuilder.append(" FROM ");
		if (StringUtils.hasText(server.getPrefix())) {
			stringBuilder.append(server.getPrefix());
			stringBuilder.append('.');
		}

		stringBuilder.append(tableName);

		stringBuilder.append(createOrderbyClause(table, display, "asc"));

		return stringBuilder.toString();
	}

	public static String createOrderbyClause(Table table, Column sortColumn, String dir) {
		if (sortColumn == null) {
			return "";
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(" ORDER BY ");
		stringBuilder.append(table.getName());
		stringBuilder.append('.');
		stringBuilder.append(sortColumn.getName());
		stringBuilder.append(' ');
		stringBuilder.append(dir);

		return stringBuilder.toString();
	}

	public static String createFilterWhereClause(Table table, List<Filter> filters) {
		StringBuilder stringBuilder = new StringBuilder();

		boolean addAnd = false;
		
		boolean addOrColumn = false;
		
		if (filters != null) {
			for (Filter filter : filters) {
				boolean addOr = false;
				if (filter.isValid()) {
					LOG.error("filter = "  + filter.toString() );
					if(addOrColumn)
					{						
						stringBuilder.append(" OR ");
						addOrColumn = false;
					}
					else
					{
						if (addAnd) {
							stringBuilder.append(" AND ");
						} else {
							addAnd = true;
						}
					}
					stringBuilder.append(" (");

					//if (filter.isNullValue()) {
					if (filter.getFilterOperator() == 8) {
						stringBuilder.append(table.getName());
						stringBuilder.append('.');
						stringBuilder.append(filter.getColumnName());
						stringBuilder.append(" IS NULL ");
						addOr = true;
					}

					//if (filter.isBlank()) {
					if (filter.getFilterOperator() == 9) {
						stringBuilder.append(table.getName());
						stringBuilder.append('.');
						stringBuilder.append(filter.getColumnName());
						stringBuilder.append(" = '' ");
					}

					if (StringUtils.hasText(filter.getFilterValue())) {
						if (addOr) {
							stringBuilder.append(" OR ");
						} else {
							addOr = true;
						}
						//deal with NOT queries, i.e. if the Not checkbox is ticked
						if (filter.isNotValue())
						{
							stringBuilder.append(" NOT ");
						}
						stringBuilder.append(table.getName());
						stringBuilder.append('.');
						stringBuilder.append(filter.getColumnName());
																	
						switch (filter.getFilterOperator()) {
			            	case 0:  stringBuilder.append(" = ?"); break; 
			            	case 1:  stringBuilder.append(" < ?"); break; 
			            	case 2:  stringBuilder.append(" <= ?"); break; 
			            	case 3:  stringBuilder.append(" > ?"); break; 
			            	case 4:  stringBuilder.append(" >= ?"); break; 
			            	case 5:  stringBuilder.append(" LIKE ?"); break; 
			            	case 6:  stringBuilder.append(" LIKE ?"); break; 
			            	case 7:  stringBuilder.append(" LIKE ?"); break;			            	
						}
						
					}
					if(filter.isOrValue())
					{
						addOrColumn = true;
					}

					stringBuilder.append(" )");

				}
			}
		}
		return stringBuilder.toString();
	}

	public static String createCount(Server server, Table table, List<Filter> filters) {
		StringBuilder stringBuilder = new StringBuilder(100);
		String tableName = table.getName();
		boolean addComma = false;

		stringBuilder.append("SELECT count(1) ");

		stringBuilder.append(" FROM ");
		if (StringUtils.hasText(server.getPrefix())) {
			stringBuilder.append(server.getPrefix());
			stringBuilder.append('.');
		}

		stringBuilder.append(tableName);

		String whereClause = createFilterWhereClause(table, filters);
		if (whereClause.length() > 0) {
			stringBuilder.append(" WHERE ");
			stringBuilder.append(whereClause);
		}

		return stringBuilder.toString();
	}

	public static String createInsertStatement(Server server, Table table, List<Column> columns) {

		StringBuilder sbInsert = new StringBuilder(columns.size() * 30);
		sbInsert.append("INSERT INTO ");

		if (StringUtils.hasText(server.getPrefix())) {
			sbInsert.append(server.getPrefix());
			sbInsert.append('.');
		}

		sbInsert.append(table.getName());
		sbInsert.append(" ( ");
		boolean needComma = false;
		for (Column column : columns) {
			if (needComma) {
				sbInsert.append(", ");
			} else {
				needComma = true;
			}
			sbInsert.append(column.getName());
		}

		sbInsert.append(" ) VALUES ( ");
		needComma = false;
		for (Column column : columns) {
			if (needComma) {
				sbInsert.append(", ");
			} else {
				needComma = true;
			}
			sbInsert.append('?');
		}

		sbInsert.append(" )");

		return sbInsert.toString();
	}

	public static String createUpdateStatement(Server server, Table table, List<Column> columns) {
		StringBuilder sbUpdate = new StringBuilder(columns.size() * 30);
		sbUpdate.append("UPDATE ");

		if (StringUtils.hasText(server.getPrefix())) {
			sbUpdate.append(server.getPrefix());
			sbUpdate.append('.');
		}

		sbUpdate.append(table.getName());
		sbUpdate.append(" SET ");
		boolean needComma = false;
		for (Column column : columns) {
			if (!column.isPrimarykey()) {
				if (needComma) {
					sbUpdate.append(", ");
				} else {
					needComma = true;
				}
				sbUpdate.append(column.getName());
				sbUpdate.append(" = ?");
			}
		}

		sbUpdate.append(createWhereClause(server, table, columns));

		return sbUpdate.toString();
	}

	public static String createDeleteStatement(Server server, Table table, List<Column> columns) {
		StringBuilder sbDelete = new StringBuilder(columns.size() * 15);
		sbDelete.append("DELETE FROM ");

		if (StringUtils.hasText(server.getPrefix())) {
			sbDelete.append(server.getPrefix());
			sbDelete.append('.');
		}

		sbDelete.append(table.getName());

		sbDelete.append(createWhereClause(server, table, columns));

		return sbDelete.toString();
	}

	public static String createWhereClause(Server server, Table table, List<Column> columns) {
		StringBuilder sbWhere = new StringBuilder(columns.size() * 15);
		sbWhere.append(" WHERE ");

		boolean needAnd = false;
		for (Column column : columns) {
			if (column.isPrimarykey()) {
				if (needAnd) {
					sbWhere.append(" AND ");
				} else {
					needAnd = true;
				}
				sbWhere.append(" (");
				sbWhere.append(column.getName());
				sbWhere.append(" = ?");
				sbWhere.append(") ");
			}
		}
		return sbWhere.toString();
	}

	public static Object convertToType(String data, int sqlDataType) {
		Object oReturn = null;

		if (StringUtils.hasText(data)) {

			switch (sqlDataType) {
				case Types.CHAR:
				case Types.VARCHAR:
				case Types.LONGVARCHAR:
					oReturn = data;
					break;
				case Types.TINYINT:
				case Types.INTEGER:
				case Types.BIT:
				case Types.SMALLINT:
					try {
						if (StringUtils.hasText(data)) {
							oReturn = Integer.parseInt(data);
						} else {
							return null;
						}
					} catch (Exception e) {
						throw new TypeMismatchDataAccessException("Vaule [" + data + "] could not be parsed as an integer");
					}
					break;
				case Types.BIGINT:
					try {
						if (StringUtils.hasText(data)) {
							oReturn = new Long(data);
						} else {
							return null;
						}
					} catch (Exception e) {
						throw new TypeMismatchDataAccessException("Vaule [" + data + "] could not be parsed as an integer");
					}
					break;
				case Types.DECIMAL:
				case Types.FLOAT:
				case Types.NUMERIC:
				case Types.REAL:
				case Types.DOUBLE:
					try {
						if (StringUtils.hasText(data)) {
							oReturn = new BigDecimal(data);
						} else {
							return null;
						}
					} catch (Exception e) {
						throw new TypeMismatchDataAccessException("Vaule [" + data + "] could not be parsed as a number");
					}
					break;
				case Types.DATE:
				case Types.TIMESTAMP:
					try {
						if (StringUtils.hasText(data)) {
							oReturn = Formatter.parseDate(data);
						} else {
							return null;
						}
					} catch (Exception e) {
						throw new TypeMismatchDataAccessException("Vaule [" + data + "] could not be parsed as a date. ");
					}
					break;
                case Types.TIME:
                    try {
                        if (StringUtils.hasText(data)) {
                            Formatter.parseTime(data);
                            oReturn = data;  // If no exception thrown from above then we have a valid time format.
                        } else {
                            return null;
                        }
                    } catch (Exception e) {
                        throw new TypeMismatchDataAccessException("Vaule [" + data + "] could not be parsed as a time. ");
                    }
                    break;
				default:
					LOG.error("Unknow datatype[" + sqlDataType + "] - using String");
					oReturn = data;
					break;
			}

		}
		return oReturn;
	}

	public static boolean isNumeric(int sqlDataType) {
		boolean blnReturn = false;

		switch (sqlDataType) {
			case Types.NUMERIC:
			case Types.TINYINT:
			case Types.INTEGER:
			case Types.BIT:
			case Types.SMALLINT:
			case Types.BIGINT:
			case Types.DECIMAL:
			case Types.FLOAT:
			case Types.REAL:
			case Types.DOUBLE:
				blnReturn = true;
				break;
			default:
				blnReturn = false;
				break;
		}
		return blnReturn;
	}

	public static boolean isText(int sqlDataType) {
		boolean blnReturn = false;

		switch (sqlDataType) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				blnReturn = true;
				break;
			default:
				blnReturn = false;
				break;
		}

		return blnReturn;

	}

	public static boolean isDate(int sqlDataType) {
		boolean blnReturn = false;

		switch (sqlDataType) {
			case Types.DATE:
			case Types.TIMESTAMP:
				blnReturn = true;
				break;
			default:
				blnReturn = false;
				break;
		}

		return blnReturn;

	}

    public static boolean isTime(int sqlDataType) {
        boolean blnReturn = false;

        switch (sqlDataType) {
            case Types.TIME:
                blnReturn = true;
                break;
            default:
                blnReturn = false;
                break;
        }

        return blnReturn;

    }	
	public static boolean isNumeric(String sqlDataType) {
		boolean blnReturn = false;

		if (StringUtils.hasText(sqlDataType)) {
			blnReturn = "NUMERIC".equals(sqlDataType) || "TINYINT".equals(sqlDataType) || "INTEGER".equals(sqlDataType) || "BIT".equals(sqlDataType)
			        || "SMALLINT".equals(sqlDataType) || "BIGINT".equals(sqlDataType) || "DECIMAL".equals(sqlDataType) || "FLOAT".equals(sqlDataType)
			        || "REAL".equals(sqlDataType) || "DOUBLE".equals(sqlDataType);
		}
		return blnReturn;
	}

	public static boolean isText(String sqlDataType) {
		boolean blnReturn = false;
		if (StringUtils.hasText(sqlDataType)) {
			blnReturn = sqlDataType.startsWith("CHAR") || sqlDataType.startsWith("VARCHAR") || sqlDataType.startsWith("LONGVARCHAR");
		}
		return blnReturn;
	}

	public static boolean isDate(String sqlDataType) {
		sqlDataType = sqlDataType.toUpperCase();
		boolean blnReturn = false;

		if (StringUtils.hasText(sqlDataType)) {
			blnReturn = "DATE".equals(sqlDataType) || "DATETIME".equals(sqlDataType) || "TIME".equals(sqlDataType) || "TIMESTAMP".equals(sqlDataType);
		}
		return blnReturn;
	}
}
