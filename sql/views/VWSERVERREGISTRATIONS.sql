CREATE OR REPLACE VIEW VWSERVERREGISTRATIONS AS
SELECT
   		gdma_server_registration.id as id,
		gdma_server_registration.name as ServerRegistrationName,
  		gdma_odbc_types.id as ODBCConnectionTypeID,
		gdma_odbc_types.name as ODBCConnectionType,
		username,
		NVL(password,'') as password,
		gdma_odbc_types.connectionClass,
    	url
	FROM 
		gdma_server_registration,gdma_odbc_types
	WHERE
		gdma_server_registration.odbc_type_id = gdma_odbc_types.id
