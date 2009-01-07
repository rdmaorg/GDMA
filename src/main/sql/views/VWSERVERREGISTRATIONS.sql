CREATE OR REPLACE VIEW VWServerS AS
SELECT
   		GDMA2_server_registration.id as id,
		GDMA2_server_registration.name as ServerName,
  		GDMA2_odbc_types.id as ODBCConnectionTypeID,
		GDMA2_odbc_types.name as ODBCConnectionType,
		username,
		NVL(password,'') as password,
		GDMA2_odbc_types.connectionClass,
    	url
	FROM 
		GDMA2_server_registration,GDMA2_odbc_types
	WHERE
		GDMA2_server_registration.odbc_type_id = GDMA2_odbc_types.id
