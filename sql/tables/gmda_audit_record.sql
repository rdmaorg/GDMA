-- Create table
create table GMDA_AUDIT_RECORD
(
  ID              NUMBER(12) not null,
  AUDIT_HEADER_ID NUMBER(12) not null,
  COLUMN_ID       NUMBER(9) not null,
  OLD_VALUE       VARCHAR2(1000),
  NEW_VALUE       VARCHAR2(1000)
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
