package com.vodafone.gdma.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vodafone.gdma.dao.TableDao;
import com.vodafone.gdma.domain.Table;

/**
 * @author Ronan Gill
 * 
 */
public class TableDaoImpl extends HibernateDaoSupport implements TableDao {

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
