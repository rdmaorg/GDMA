CREATE TABLESPACE GDMA
    datafile 'c:\oracle\oradata\v5dev\gdma.dbf' 
            SIZE 5120K AUTOEXTEND ON NEXT 5120K MAXSIZE UNLIMITED
    LOGGING
    DEFAULT STORAGE(INITIAL 40K
                    NEXT 40K
                    MINEXTENTS 1
                    MAXEXTENTS UNLIMITED
                    PCTINCREASE 50)
    ONLINE
    PERMANENT
/
