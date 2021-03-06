-- Create table
create table GDMA2_AUDIT_HEADER
(
  ID          NUMBER(12) not null,
  TABLE_ID    NUMBER(9) not null,
  AUDIT_TYPE        CHAR(1) not null,
  MODIFIED_BY VARCHAR2(255) not null,
  MODIFIED_ON DATE not null
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
