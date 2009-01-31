CREATE TABLE GDMA2_COLUMN
(
  id                       INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  table_id                 INT NOT NULL,
  name                     VARCHAR(255) NOT NULL,
  type                     TINYINT NOT NULL,
  typestring               VARCHAR(255),
  dd_lookup_column_display INT,
  dd_lookup_column_store   INT,
  displayed                CHAR(1) DEFAULT 'N' NOT NULL,
  allowinsert              CHAR(1) DEFAULT 'N' NOT NULL,
  allowupdate              CHAR(1) DEFAULT 'N' NOT NULL,
  nullable                 CHAR(1) DEFAULT 'N' NOT NULL,
  primarykey               CHAR(1) DEFAULT 'N' NOT NULL,
  special                  CHAR(1) DEFAULT 'N' NOT NULL,
  minwidth                 SMALLINT,
  maxwidth                 SMALLINT,
  orderby                  SMALLINT
);
