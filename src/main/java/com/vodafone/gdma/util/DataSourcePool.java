package com.vodafone.gdma.util;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.vodafone.gdma.domain.Server;

public interface DataSourcePool {

    public DataSourceTransactionManager getTransactionManager(Server server);

}
