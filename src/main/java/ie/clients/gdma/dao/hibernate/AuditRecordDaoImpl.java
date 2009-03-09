package ie.clients.gdma.dao.hibernate;

import ie.clients.gdma.dao.AuditRecordDao;
import ie.clients.gdma.domain.AuditRecord;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


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
