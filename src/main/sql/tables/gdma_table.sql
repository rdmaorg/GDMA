-- Create table
create table GDMA2_TABLE
(
  ID          NUMBER(9) not null,
  SERVER_ID   NUMBER(9) not null,
  NAME        VARCHAR2(255) not null,
  DISPLAYED   CHAR(1) default 'N' not null,
  ALLOWDELETE CHAR(1) default 'N' not null
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
