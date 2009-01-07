CREATE TABLESPACE GDMA
    DATAFILE '/opt/oracle/oracle/product/10.2.0/oradata/orcl/GDMA.DBF'
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

CREATE OR REPLACE DIRECTORY DUMPDIR AS '/opt/oracle/oracle/product/10.2.0/oradata/orcl/DUMPDIR/'
/
GRANT READ, WRITE ON DIRECTORY DUMPDIR TO GDMA;
/