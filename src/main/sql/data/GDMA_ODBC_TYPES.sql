delete from GDMA2_ODBC_TYPES;
commit;
insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
values (1, 'MS SQL Server 2000', 'SELECT NAME as TABLE_NAME FROM SYSOBJECTS WHERE XTYPE=''U'' ORDER BY NAME', 'sun.jdbc.odbc.JdbcOdbcDriver');
insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
values (2, 'ODBC Oracle 8/9', 'select table_name as TABLE_NAME from user_tables', 'sun.jdbc.odbc.JdbcOdbcDriver');
insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
values (3, 'JDBC Oracle 8/9', 'select table_name as TABLE_NAME from user_tables', 'oracle.jdbc.OracleDriver');
insert into GDMA2_ODBC_TYPES (ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS)
values (4, 'ODBC Teradata 8/9', 'SELECT TABLENAME AS TABLE_NAME  FROM DBC.TABLES ORDER BY TABLENAME', 'sun.jdbc.odbc.JdbcOdbcDriver');
commit;

