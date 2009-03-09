package ie.clients.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ie.clients.gdma.dao.AuditRecordDao;
import ie.clients.gdma.domain.AuditRecord;
import ie.clients.gdma.util.BaseSpringImplTest;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class AuditRecordDaoImplTest extends BaseSpringImplTest {

    protected static Logger LOG = Logger.getLogger(AuditRecordDaoImplTest.class);

    @Autowired
    private AuditRecordDao auditRecordDao;

    @Ignore
    @Test
    public void testloadAll() {
        assertNotNull(auditRecordDao);
        List auditRecords = auditRecordDao.getAuditRecords();
        assertTrue(auditRecords.size() > 0);
        for (Object o : auditRecords) {
            AuditRecord auditRecord = (AuditRecord) o;
            LOG.debug(auditRecord);
        }

    }
}
