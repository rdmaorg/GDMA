package ie.clients.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ie.clients.gdma.dao.ConnectionTypeDao;
import ie.clients.gdma.domain.ConnectionType;
import ie.clients.gdma.util.BaseSpringImplTest;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class OdbcProviderDaoImplTest extends BaseSpringImplTest {
    protected static Logger LOG = Logger.getLogger(OdbcProviderDaoImplTest.class);

    @Autowired
    private ConnectionTypeDao odbcProviderDao;

    @Test
    public void loadAll() {
        assertNotNull(odbcProviderDao);
        List odbcProviders = odbcProviderDao.get();
        LOG.debug(odbcProviders.getClass().getName());
        LOG.debug(odbcProviderDao.getClass().getName());
        assertTrue(odbcProviders.size() > 0);
        for (Object o : odbcProviders) {
            ConnectionType odbcProvider = (ConnectionType) o;
            LOG.debug(odbcProvider);
        }

    }
}
