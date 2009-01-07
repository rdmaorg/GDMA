package com.vodafone.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.vodafone.gdma.dao.AuditRecordDao;
import com.vodafone.gdma.domain.AuditRecord;
import com.vodafone.gdma.util.BaseSpringImplTest;

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
