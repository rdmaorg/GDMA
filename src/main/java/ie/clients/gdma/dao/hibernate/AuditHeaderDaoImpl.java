package ie.clients.gdma.dao.hibernate;

import ie.clients.gdma.dao.AuditHeaderDao;
import ie.clients.gdma.domain.AuditHeader;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ronan Gill
 * 
 */
public class AuditHeaderDaoImpl extends HibernateDaoSupport implements AuditHeaderDao {

    public List getAuditHeaders() {
        return getHibernateTemplate().loadAll(AuditHeader.class);
    }

    public AuditHeader getAuditHeaderById(Long id) {
        return (AuditHeader) getHibernateTemplate().load(AuditHeader.class, id);
    }

    public AuditHeader saveAuditHeader(AuditHeader auditHeader) {
        getHibernateTemplate().saveOrUpdate(auditHeader);
        return auditHeader;
    }

}
