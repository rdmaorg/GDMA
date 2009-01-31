CREATE TABLE GDMA2_AUDIT_RECORD
(
  id              BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  audit_header_id BIGINT NOT NULL,
  column_id       INT NOT NULL,
  old_value       VARCHAR(1000),
  new_value       VARCHAR(1000)
);
