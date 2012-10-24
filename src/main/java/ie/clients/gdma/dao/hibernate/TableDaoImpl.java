package ie.clients.gdma.dao.hibernate;

import ie.clients.gdma.dao.TableDao;
import ie.clients.gdma.domain.Table;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ronan Gill
 * 
 */
public class TableDaoImpl extends HibernateDaoSupport implements TableDao {

	public List<Table> get() {
        return getHibernateTemplate().loadAll(Table.class);
    }

    public Table get(Long id) {
        return (Table) getHibernateTemplate().load(Table.class, id);
    }

    public Table save(Table table) {
        getHibernateTemplate().saveOrUpdate(table);
        return table;
    }

    public void save(List<Table> tables) {
        for (Table table : tables) {
            getHibernateTemplate().saveOrUpdate(table);
        }
    }
}
