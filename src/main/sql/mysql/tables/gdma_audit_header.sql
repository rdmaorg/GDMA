CREATE TABLE GDMA2_AUDIT_HEADER
(
  id          BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  table_id    INT NOT NULL,
  type        CHAR(1) NOT NULL,
  modified_by VARCHAR(255) NOT NULL,
  modified_on DATE NOT NULL
);
