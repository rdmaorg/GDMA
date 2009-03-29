delete from GDMA2_ODBC_TYPES;
commit;
-- insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
-- values (1, 'MS SQL Server (ODBC)', 'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE=''U'' ORDER BY NAME', 'sun.jdbc.odbc.JdbcOdbcDriver');
insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
values (1, 'MS SQL Server (JDBC)', 'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE=''U'' ORDER BY NAME', 'com.microsoft.sqlserver.jdbc.SQLServerDriver');
-- insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
-- values (2, 'Oracle 8/9 (ODBC)', 'select table_name as TABLE_NAME from user_tables', 'sun.jdbc.odbc.JdbcOdbcDriver');
insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
values (3, 'Oracle 8/9 (JDBC)', 'select table_name as TABLE_NAME from user_tables', 'oracle.jdbc.OracleDriver');
-- insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
-- values (4, 'Teradata 8/9 (ODBC)', 'SELECT TABLENAME AS TABLE_NAME  FROM DBC.TABLES ORDER BY TABLENAME', 'sun.jdbc.odbc.JdbcOdbcDriver');
insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
values (5, 'Teradata 8/9 (JDBC)', 'SELECT TABLENAME AS TABLE_NAME  FROM DBC.TABLES ORDER BY TABLENAME', 'com.ncr.teradata.TeraDriver');
insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
values (6, 'MySQL (JDBC)', 'SHOW TABLES', 'com.mysql.jdbc.Driver');
commit;

