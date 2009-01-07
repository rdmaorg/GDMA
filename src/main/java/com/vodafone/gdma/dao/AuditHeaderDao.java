package com.vodafone.gdma.dao;

import java.util.List;

import com.vodafone.gdma.domain.AuditHeader;

/**
 * @author Ronan Gill
 * 
 */
public interface AuditHeaderDao {

    public List getAuditHeaders();

    public AuditHeader getAuditHeaderById(Long id);

    public AuditHeader saveAuditHeader(AuditHeader auditHeader);

}
