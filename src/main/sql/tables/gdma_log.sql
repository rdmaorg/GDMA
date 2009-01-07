-- Create table
create table GDMA2_LOG
(
  LOGGED_AT   DATE not null,
  LOGLEVEL    VARCHAR2(50) not null,
  LOGGERNAME  VARCHAR2(100) not null,
  MESSAGE     VARCHAR2(1000) not null
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
