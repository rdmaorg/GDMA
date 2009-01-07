-- Create table
create table GDMA2_USER_TABLE_ACCESS
(
  TABLE_ID          NUMBER(9) not null,
  USER_ID           NUMBER(9) not null
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
