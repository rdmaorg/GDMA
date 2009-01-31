CREATE TABLE GDMA2_SERVER_REGISTRATION
(
  id           INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  odbc_type_id INT NOT NULL,
  name         VARCHAR(255) NOT NULL,
  username     VARCHAR(255) NOT NULL,
  password     VARCHAR(255),
  url          VARCHAR(500),
  prefix       VARCHAR(50)
);
