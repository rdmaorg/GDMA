-- Create table
create table GDMA_SERVER_REGISTRATION
(
  ID           NUMBER(9) not null,
  ODBC_TYPE_ID NUMBER(9) not null,
  NAME         VARCHAR2(255) not null,
  USERNAME     VARCHAR2(255) not null,
  PASSWORD     VARCHAR2(255),
  URL          VARCHAR2(500)
)
tablespace GDMA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
