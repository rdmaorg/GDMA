-- Create sequence
-- get max first 
-- select max(user_id) + 1 from GDMA2_USERS;
drop sequence SEQ_GDMA2_USERS;
create sequence SEQ_GDMA2_USERS
minvalue 1
maxvalue 9999999999999999999999999999
start with 955
increment by 1
cache 20;







