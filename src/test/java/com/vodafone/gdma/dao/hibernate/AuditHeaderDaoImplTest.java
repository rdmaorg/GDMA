package com.vodafone.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.vodafone.gdma.dao.AuditHeaderDao;
import com.vodafone.gdma.domain.AuditHeader;
import com.vodafone.gdma.util.BaseSpringImplTest;

public class AuditHeaderDaoImplTest extends BaseSpringImplTest {

    protected static Logger LOG = Logger.getLogger(AuditHeaderDaoImplTest.class);

    @Autowired
    private AuditHeaderDao auditHeaderDao;

    @Ignore
    @Test
    public void loadAll() {
        assertNotNull(auditHeaderDao);
        List auditHeaders = auditHeaderDao.getAuditHeaders();
        assertTrue(auditHeaders.size() > 0);
        for (Object o : auditHeaders) {
            AuditHeader auditHeader = (AuditHeader) o;
            LOG.debug(auditHeader);
        }

    }
}
