INSERT INTO GDMA.GDMA_ODBC_TYPES (ID,NAME,SELECT_GET_TABLES, CONNECTIONCLASS) VALUES
(
	1,
	'MS SQL Server 2000', 
	'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE=\'U\' ORDER BY NAME',
	'sun.jdbc.odbc.JdbcOdbcDriver'
);

INSERT INTO GDMA.GDMA_ODBC_TYPES (ID,NAME,SELECT_GET_TABLES, CONNECTIONCLASS) VALUES
(
	2,
	'ODBC Oracle 8/9',
	'select table_name as TABLE_NAME from user_tables',
	'sun.jdbc.odbc.JdbcOdbcDriver'
);

INSERT INTO GDMA.GDMA_ODBC_TYPES (ID,NAME,SELECT_GET_TABLES, CONNECTIONCLASS) VALUES
(
	3,
	'JDBC Oracle 8/9',
	'select table_name as TABLE_NAME from user_tables',
	'oracle.jdbc.OracleDriver'
);
