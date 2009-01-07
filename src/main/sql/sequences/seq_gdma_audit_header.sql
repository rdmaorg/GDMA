-- Create sequence
-- get max first 
-- select max(id) + 1 from GDMA2_AUDIT_HEADER
drop sequence SEQ_GDMA2_AUDIT_HEADER;
create sequence SEQ_GDMA2_AUDIT_HEADER
minvalue 1
maxvalue 9999999999999999999999999999
start with 3
increment by 1
cache 20;







