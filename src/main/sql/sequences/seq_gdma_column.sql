-- Create sequence
-- get max first 
-- select max(id) + 1 from GDMA2_COLUMN
drop sequence SEQ_GDMA2_COLUMN;
create sequence SEQ_GDMA2_COLUMN
minvalue 1
maxvalue 9999999999999999999999999999
start with 3113
increment by 1
cache 20;





