package ie.clients.gdma.dao;

import ie.clients.gdma.domain.AuditHeader;

import java.util.List;


/**
 * @author Ronan Gill
 * 
 */
public interface AuditHeaderDao {

    public List getAuditHeaders();

    public AuditHeader getAuditHeaderById(Long id);

    public AuditHeader saveAuditHeader(AuditHeader auditHeader);

}
