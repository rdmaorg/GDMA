-- Create table
create table DELETE_ME
(
  ID       NUMBER(6) not null,
  NAME     VARCHAR2(255) not null,
  NUM      NUMBER(10),
  DEC      NUMBER(10,2),
  DATEFROM DATE
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
