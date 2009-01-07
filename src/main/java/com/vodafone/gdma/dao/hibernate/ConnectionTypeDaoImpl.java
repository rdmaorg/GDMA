package com.vodafone.gdma.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vodafone.gdma.dao.ConnectionTypeDao;
import com.vodafone.gdma.domain.ConnectionType;

/**
 * @author Ronan Gill
 * 
 */
public class ConnectionTypeDaoImpl extends HibernateDaoSupport implements ConnectionTypeDao {

    public List get() {
        return getHibernateTemplate().loadAll(ConnectionType.class);
    }

    public ConnectionType get(Long id) {
        return (ConnectionType) getHibernateTemplate().load(ConnectionType.class, id);
    }
}
