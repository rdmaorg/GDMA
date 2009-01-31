CREATE TABLE GDMA2_LOG
(
  logged_at   DATE NOT NULL,
  loglevel    VARCHAR(50) NOT NULL,
  loggername  VARCHAR(100) NOT NULL,
  message     VARCHAR(1000) NOT NULL
);
