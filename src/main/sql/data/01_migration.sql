-- TUNCATE TABLE GDMA2_ODBC_TYPES
INSERT INTO GDMA2_ODBC_TYPES (  ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS,  DIALECTCLASS)
  SELECT  ID, NAME, SELECT_GET_TABLES, CONNECTIONCLASS, 'org.hibernate.dialect.Oracle10gDialect' FROM GDMA_ODBC_TYPES;

-- TUNCATE TABLE GDMA2_SERVER_REGISTRATION
INSERT INTO GDMA2_SERVER_REGISTRATION ( ID, ODBC_TYPE_ID, NAME, USERNAME, PASSWORD, URL, PREFIX)
  SELECT ID, ODBC_TYPE_ID, NAME, USERNAME, PASSWORD, URL, PREFIX FROM GDMA_SERVER_REGISTRATION;

-- TUNCATE TABLE GDMA2_USERS
INSERT INTO GDMA2_USERS ( USER_ID, FIRST_NAME, LAST_NAME, USERNAME)
  SELECT USER_ID, FIRST_NAME, LAST_NAME, USERNAME FROM GDMA_USERS;

-- TUNCATE TABLE GDMA2_TABLE
INSERT INTO GDMA2_TABLE (  ID, SERVER_ID, NAME, DISPLAYED, ALLOWDELETE )
  SELECT  ID, SERVER_ID, NAME, DISPLAYED, ALLOWDELETE FROM GDMA_TABLE;

-- TUNCATE TABLE GDMA2_COLUMN
INSERT INTO GDMA2_COLUMN (  ID, TABLE_ID, NAME, TYPE, TYPESTRING, DD_LOOKUP_COLUMN_DISPLAY, DD_LOOKUP_COLUMN_STORE, DISPLAYED, ALLOWINSERT, ALLOWUPDATE, NULLABLE, PRIMARYKEY, SPECIAL)
  SELECT  ID, TABLE_ID, NAME, TYPE, TYPESTRING, DD_LOOKUP_COLUMN_DISPLAY, DD_LOOKUP_COLUMN_STORE, DISPLAYED, ALLOWINSERT, ALLOWUPDATE, NULLABLE, PRIMARYKEY, SPECIAL FROM GMDA_COLUMN;

-- TUNCATE TABLE GDMA2_USER_TABLE_ACCESS
INSERT INTO GDMA2_USER_TABLE_ACCESS ( USER_ID, TABLE_ID)
  SELECT USER_ID, TABLE_ID  FROM GDMA_USER_TABLE_ACCESS;

-- clean out the data
delete from GDMA2_user_table_access where user_id not in (select user_id from GDMA_users)
delete from GDMA2_user_table_access where table_id not in (select id  from GDMA_table)

-- call the sequence code now - rember to get the "Start With" value formthe select max