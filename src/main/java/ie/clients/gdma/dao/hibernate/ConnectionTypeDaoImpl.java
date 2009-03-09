package ie.clients.gdma.dao.hibernate;

import ie.clients.gdma.dao.ConnectionTypeDao;
import ie.clients.gdma.domain.ConnectionType;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


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
