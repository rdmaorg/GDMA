/*==============================================================*/
/* Database name:  PhysicalDataModel_2                          */
/* DBMS name:      ORACLE Version 8i2 (8.1.6)                   */
/* Created on:     03/04/2004 19:49:12                          */
/*==============================================================*/


drop table GDMA.GMDA_COLUMN cascade constraints
/


drop table GDMA.GMDA_AUDIT_RECORD cascade constraints
/


drop table GDMA.GMDA_AUDIT_HEADER cascade constraints
/


drop table GDMA.GDMA_TABLE cascade constraints
/


drop table GDMA.GDMA_SERVER_REGISTRATION cascade constraints
/


drop table GDMA.GDMA_ODBC_TYPES cascade constraints
/


drop table GDMA.DELETE_ME cascade constraints
/


drop sequence S_DELETE_ME
/


drop sequence S_GDMA_ODBC_TYPES
/


drop sequence S_GDMA_SERVER_REGISTRATION
/


drop sequence S_GDMA_TABLE
/


drop sequence S_GMDA_AUDIT_HEADER
/


drop sequence S_GMDA_AUDIT_RECORD
/


drop sequence S_GMDA_COLUMN
/


create sequence S_DELETE_ME
/


create sequence S_GDMA_ODBC_TYPES
/


create sequence S_GDMA_SERVER_REGISTRATION
/


create sequence S_GDMA_TABLE
/


create sequence S_GMDA_AUDIT_HEADER
/


create sequence S_GMDA_AUDIT_RECORD
/


create sequence S_GMDA_COLUMN
/


/*==============================================================*/
/* Table : DELETE_ME                                            */
/*==============================================================*/


create table GDMA.DELETE_ME  (
   ID                   NUMBER(6)                        not null,
   NAME                 VARCHAR2(255)                    not null,
   NUM                  NUMBER(10),
   DEC                  NUMBER(10,2),
   DATEFROM             DATE
)
/


/*==============================================================*/
/* Table : GDMA_ODBC_TYPES                                      */
/*==============================================================*/


create table GDMA.GDMA_ODBC_TYPES  (
   ID                   NUMBER(9)                        not null,
   NAME                 VARCHAR2(255)                    not null,
   SELECT_GET_TABLES    VARCHAR2(1000)                   not null,
   CONNECTIONCLASS      VARCHAR2(500)                    not null
)
/


/*==============================================================*/
/* Table : GDMA_SERVER_REGISTRATION                             */
/*==============================================================*/


create table GDMA.GDMA_SERVER_REGISTRATION  (
   ID                   NUMBER(9)                        not null,
   ODBC_TYPE_ID         NUMBER(9)                        not null,
   NAME                 VARCHAR2(255)                    not null,
   USERNAME             VARCHAR2(255)                    not null,
   PASSWORD             VARCHAR2(255),
   URL                  VARCHAR2(500)
)
/


/*==============================================================*/
/* Table : GDMA_TABLE                                           */
/*==============================================================*/


create table GDMA.GDMA_TABLE  (
   ID                   NUMBER(9)                        not null,
   SERVER_ID            NUMBER(9)                        not null,
   NAME                 VARCHAR2(255)                    not null,
   DISPLAYED            CHAR(1)                        default 'N'  not null,
   ALLOWDELETE          CHAR(1)                        default 'N'  not null
)
/


/*==============================================================*/
/* Table : GMDA_AUDIT_HEADER                                    */
/*==============================================================*/


create table GDMA.GMDA_AUDIT_HEADER  (
   ID                   NUMBER(12)                       not null,
   TABLE_ID             NUMBER(9)                        not null,
   TYPE                 CHAR(1)                          not null,
   MODIFIED_BY          VARCHAR2(255)                    not null,
   MODIFIED_ON          DATE                             not null
)
/


/*==============================================================*/
/* Table : GMDA_AUDIT_RECORD                                    */
/*==============================================================*/


create table GDMA.GMDA_AUDIT_RECORD  (
   ID                   NUMBER(12)                       not null,
   AUDIT_HEADER_ID      NUMBER(12)                       not null,
   COLUMN_ID            NUMBER(9)                        not null,
   OLD_VALUE            VARCHAR2(1000),
   NEW_VALUE            VARCHAR2(1000)
)
/


/*==============================================================*/
/* Table : GMDA_COLUMN                                          */
/*==============================================================*/


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
)
/


