package ie.clients.gdma.util;

import ie.clients.gdma.domain.Server;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;


public interface DataSourcePool {

    public DataSourceTransactionManager getTransactionManager(Server server);

}
