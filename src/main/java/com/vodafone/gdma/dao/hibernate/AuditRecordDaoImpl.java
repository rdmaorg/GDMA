package com.vodafone.gdma.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vodafone.gdma.dao.AuditRecordDao;
import com.vodafone.gdma.domain.AuditRecord;

/**
 * @author Ronan Gill
 * 
 */
public class AuditRecordDaoImpl extends HibernateDaoSupport implements AuditRecordDao {

    public List getAuditRecords() {
        return getHibernateTemplate().loadAll(AuditRecord.class);
    }

    public AuditRecord getAuditRecordById(Long id) {
        return (AuditRecord) getHibernateTemplate().load(AuditRecord.class, id);
    }

    public AuditRecord saveAuditRecord(AuditRecord auditRecord) {
        getHibernateTemplate().saveOrUpdate(auditRecord);
        return auditRecord;
    }

    public void saveAuditRecords(List<AuditRecord> auditRecords) {
        getHibernateTemplate().saveOrUpdateAll(auditRecords);
    }

}
