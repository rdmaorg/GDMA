-- Create sequence
-- get max first 
-- select max(id) + 1 from GDMA2_SERVER_REGISTRATION
drop sequence SEQ_GDMA2_SERVER_REGISTRATION;
create sequence SEQ_GDMA2_SERVER_REGISTRATION
minvalue 1
maxvalue 9999999999999999999999999999
start with 46
increment by 1
cache 20;



