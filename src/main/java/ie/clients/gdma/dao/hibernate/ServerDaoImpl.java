package ie.clients.gdma.dao.hibernate;

import ie.clients.gdma.dao.ServerDao;
import ie.clients.gdma.domain.Server;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ronan Gill
 * 
 */
public class ServerDaoImpl extends HibernateDaoSupport implements ServerDao {

    protected static Logger LOG = Logger.getLogger(ServerDaoImpl.class);

    public List get() {
        return getHibernateTemplate().loadAll(Server.class);
    }

    public Server get(Long id) {
        return (Server) getHibernateTemplate().load(Server.class, id);
    }

    public Server save(Server server) {
        getHibernateTemplate().saveOrUpdate(server);
        return server;

    }

    public void save(List<Server> servers) {
        for (Server server : servers) {
            getHibernateTemplate().saveOrUpdate(server);
        }
    }

    public void delete(Server server) {
        try {
            getHibernateTemplate().delete(server);
            LOG.debug("Deleted server : " + server.getName());
        } catch (RuntimeException dive) {
            LOG.error(">>>>>>>>>>>>>>>" + dive);
        }

    }

    @SuppressWarnings("unchecked")
    public List<Server> getServerTableList(final String userName) {
        return (List) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String query = "";
                query += "select distinct server";
                query += "  from Server as server ";
                query += "    inner join fetch server.tables as table";
                query += "    join fetch table.users as user";
                query += "  where user.userName = ? and table.displayed = true ";
                return session.createQuery(query).setString(0, userName).list();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<Server> getServerTableColumnList() {
        String query = "";
        query += "select distinct server from Server as server ";
        query += "    inner join fetch server.tables as table";
        query += "    inner join fetch table.columns as column";
        List<Server> servers = getHibernateTemplate().find(query);
        return servers;
    }

    @SuppressWarnings("unchecked")
    public Server getByColumn(final Long columnId) {
        return (Server) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String query = "";
                query += "select distinct server from Server as server ";
                query += "    inner join fetch server.tables as table";
                query += "    inner join fetch table.columns as column";
                query += "  where column.id = ?";
                return session.createQuery(query).setLong(0, columnId).uniqueResult();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<Server> getServerTableColumnList(final Long serverId, final Long tableId) {
        return (List) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String query = "";
                query += "select distinct server";
                query += "  from Server as server ";
                query += "    inner join fetch server.tables as table";
                query += "    inner join fetch table.columns as column";
                query += "  where ";
                query += "    server.id = ? and";
                query += "    table.id = ?  ";
                return session.createQuery(query).setLong(0, serverId).setLong(1, tableId).list();
            }
        });
    }

}
