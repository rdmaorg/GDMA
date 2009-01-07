create table GDMA2_USERS 
(	
	USER_ID 				NUMBER(9,0) NOT NULL,
	FIRST_NAME				VARCHAR2(255) NOT NULL,
	LAST_NAME				VARCHAR2(255) NOT NULL, 
	USERNAME				VARCHAR2(255) NOT NULL, 
	ISADMIN     			CHAR(1) DEFAULT 'N' NOT null, 
	ISLOCKED				CHAR(1) DEFAULT 'N' NOT NULL
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
 
