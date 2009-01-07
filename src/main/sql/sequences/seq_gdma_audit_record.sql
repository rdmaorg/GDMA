-- Create sequence
-- get max first 
-- select max(id) + 1 from GDMA2_AUDIT_RECORD
drop sequence SEQ_GDMA2_AUDIT_RECORD;
create sequence SEQ_GDMA2_AUDIT_RECORD
minvalue 1
maxvalue 9999999999999999999999999999
start with 9
increment by 1
cache 20;






