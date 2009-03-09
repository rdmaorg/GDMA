package ie.clients.gdma.dao;

import ie.clients.gdma.domain.AuditRecord;

import java.util.List;


/**
 * @author Ronan Gill
 * 
 */
public interface AuditRecordDao {

    public List getAuditRecords();

    public AuditRecord getAuditRecordById(Long id);

    public AuditRecord saveAuditRecord(AuditRecord auditRecord);

    public void saveAuditRecords(List<AuditRecord> auditRecords);

}
