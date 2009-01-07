-- Create sequence
-- get max first 
-- select max(id) + 1 from GDMA2_ODBC_TYPES
drop sequence SEQ_GDMA2_ODBC_TYPES;
create sequence SEQ_GDMA2_ODBC_TYPES
minvalue 1
maxvalue 9999999999999999999999999999
start with 6
increment by 1
cache 20;




