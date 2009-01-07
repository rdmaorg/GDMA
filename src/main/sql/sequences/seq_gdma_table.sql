-- Create sequence
-- get max first 
-- select max(id) + 1 from GDMA2_TABLE
drop sequence SEQ_GDMA2_TABLE;
create sequence SEQ_GDMA2_TABLE
minvalue 1
maxvalue 9999999999999999999999999999
start with 1683
increment by 1
cache 20;
 