CREATE OR REPLACE VIEW VWServerS AS
SELECT
   		sr.id as id,
		sr.name as ServerName,
  		ot.id as ODBCConnectionTypeID,
		ot.name as ODBCConnectionType,
		username,
		ifnull(password,'') as password,
		ot.connectionClass,
    	url
	FROM 
		GDMA2_SERVER_REGISTRATION sr,GDMA2_ODBC_TYPES ot
	WHERE
		sr.odbc_type_id = ot.id;
