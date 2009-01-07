package com.vodafone.gdma.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vodafone.gdma.dao.AuditHeaderDao;
import com.vodafone.gdma.domain.AuditHeader;

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
