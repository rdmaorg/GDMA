CREATE TABLE GDMA2_TABLE
(
  id          INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  server_id   INT NOT NULL,
  name        VARCHAR(255) NOT NULL,
  displayed   CHAR(1) DEFAULT 'N' NOT NULL,
  allowdelete CHAR(1) DEFAULT 'N' NOT NULL
);
