-- Create table
create table GDMA2_COLUMN
(
  ID                       NUMBER(9) not null,
  TABLE_ID                 NUMBER(9) not null,
  NAME                     VARCHAR2(255) not null,
  TYPE                     NUMBER(2) not null,
  TYPESTRING               VARCHAR2(255),
  DD_LOOKUP_COLUMN_DISPLAY NUMBER(9),
  DD_LOOKUP_COLUMN_STORE   NUMBER(9),
  DISPLAYED                CHAR(1) default 'N' not null,
  ALLOWINSERT              CHAR(1) default 'N' not null,
  ALLOWUPDATE              CHAR(1) default 'N' not null,
  NULLABLE                 CHAR(1) default 'N' not null,
  PRIMARYKEY               CHAR(1) default 'N' NOT NULL,
  SPECIAL                  CHAR(1) DEFAULT 'N' NOT NULL,
  MINWIDTH				   NUMBER(3),
  MAXWIDTH				   NUMBER(3),
  ORDERBY                  NUMBER(3)
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
