CREATE TABLE GDMA2_USERS 
(	
	user_id 				INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	first_name				VARCHAR(255) NOT NULL,
	last_name				VARCHAR(255) NOT NULL, 
	username				VARCHAR(255) NOT NULL, 
	isadmin     			CHAR(1) DEFAULT 'N' NOT NULL, 
	islocked				CHAR(1) DEFAULT 'N' NOT NULL
); 
