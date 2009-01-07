package com.vodafone.gdma.dao;

import java.util.List;

import com.vodafone.gdma.domain.AuditRecord;

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
