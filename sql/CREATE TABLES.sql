/* CREATE TABLES */

drop table GDMA.DELETE_ME cascade constraints;
create table GDMA.DELETE_ME  (
   ID                   NUMBER(6)                        not null,
   NAME                 VARCHAR2(255)                    not null,
   NUM                  NUMBER(10),
   DEC                  NUMBER(10,2),
   DATEFROM             DATE
);

drop table GDMA.GDMA_ODBC_TYPES cascade constraints;
create table GDMA.GDMA_ODBC_TYPES  (
   ID                   NUMBER(9)                        not null,
   NAME                 VARCHAR2(255)                    not null,
   SELECT_GET_TABLES    VARCHAR2(1000)                   not null,
   CONNECTIONCLASS      VARCHAR2(500)                    not null,
   CONSOLE              VARCHAR2(100)                    null
);

drop table GDMA.GDMA_SERVER_REGISTRATION cascade constraints;
create table GDMA.GDMA_SERVER_REGISTRATION  (
   ID                   NUMBER(9)                        not null,
   ODBC_TYPE_ID         NUMBER(9)                        not null,
   NAME                 VARCHAR2(255)                    not null,
   USERNAME             VARCHAR2(255)                    not null,
   PASSWORD             VARCHAR2(255),
   URL                  VARCHAR2(500)
);

drop table GDMA.GDMA_TABLE cascade constraints;
create table GDMA.GDMA_TABLE  (
   ID                   NUMBER(9)                        not null,
   SERVER_ID            NUMBER(9)                        not null,
   NAME                 VARCHAR2(255)                    not null,
   DISPLAYED            CHAR(1)                        default 'N'  not null,
   ALLOWDELETE          CHAR(1)                        default 'N'  not null
);

drop table GDMA.GMDA_AUDIT_HEADER cascade constraints;
create table GDMA.GMDA_AUDIT_HEADER  (
   ID                   NUMBER(12)                       not null,
   TABLE_ID             NUMBER(9)                        not null,
   TYPE                 CHAR(1)                          not null,
   MODIFIED_BY          VARCHAR2(255)                    not null,
   MODIFIED_ON          DATE                             not null
);

drop table GDMA.GMDA_AUDIT_RECORD cascade constraints;
create table GDMA.GMDA_AUDIT_RECORD  (
   ID                   NUMBER(12)                       not null,
   AUDIT_HEADER_ID      NUMBER(12)                       not null,
   COLUMN_ID            NUMBER(9)                        not null,
   OLD_VALUE            VARCHAR2(1000),
   NEW_VALUE            VARCHAR2(1000)
);

drop table GDMA.GMDA_COLUMN cascade constraints;
create table GDMA.GMDA_COLUMN  (
   ID                   NUMBER(9)                        not null,
   TABLE_ID             NUMBER(9)                        not null,
   NAME                 VARCHAR2(255)                    not null,
   TYPE                 NUMBER(2)                        not null,
   TYPESTRING           VARCHAR2(255),
   DD_LOOKUP_COLUMN_DISPLAY NUMBER(9),
   DD_LOOKUP_COLUMN_STORE NUMBER(9),
   DISPLAYED            CHAR(1)                        default 'N'  not null,
   ALLOWINSERT          CHAR(1)                        default 'N'  not null,
   ALLOWUPDATE          CHAR(1)                        default 'N'  not null,
   NULLABLE             CHAR(1)                        default 'N'  not null
);

