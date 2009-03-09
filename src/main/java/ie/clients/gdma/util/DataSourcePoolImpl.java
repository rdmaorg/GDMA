package ie.clients.gdma.util;

import ie.clients.gdma.domain.Server;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


public class DataSourcePoolImpl implements DataSourcePool {

    private Map<Long, DataSourceTransactionManager> dataSoucePool = new HashMap<Long, DataSourceTransactionManager>();

    // TODO refresh DataSource when server has been updated
    public DataSourceTransactionManager getTransactionManager(Server server) {
        if (dataSoucePool.containsKey(server.getId())) {
            return dataSoucePool.get(server.getId());
        }

        return createDataSource(server);
    }

    private synchronized DataSourceTransactionManager createDataSource(Server server) {
        // just in case ...
        if (dataSoucePool.containsKey(server.getId())) {
            return dataSoucePool.get(server.getId());
        }

        try {
            // TODO configure properly - i.e. maube add max and min to server
            // config
            BasicDataSource basicDataSource = new BasicDataSource();

            basicDataSource.setUrl(server.getConnectionUrl());
            basicDataSource.setUsername(server.getUsername());
            basicDataSource.setPassword(server.getPassword());
            basicDataSource.setDriverClassName(server.getConnectionType().getConnectionClass());
            DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(basicDataSource);
            dataSoucePool.put(server.getId(), transactionManager);

            server.setConnected(true);
            return transactionManager;
        } catch (Throwable e) {
            server.setConnected(false);
            server.setLastError(e.getMessage());
        }
        return null;
    }
}
