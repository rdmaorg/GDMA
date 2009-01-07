-- Create table
create table GDMA2_ODBC_TYPES
(
  ID                NUMBER(9) not null,
  NAME              VARCHAR2(255) not null,
  SELECT_GET_TABLES VARCHAR2(1000) not null,
  CONNECTIONCLASS   VARCHAR2(500) not null
)
tablespace META_DATA_TS_01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
