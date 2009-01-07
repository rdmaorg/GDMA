package com.vodafone.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.vodafone.gdma.dao.ServerDao;
import com.vodafone.gdma.dao.TableDao;
import com.vodafone.gdma.domain.Server;
import com.vodafone.gdma.domain.Table;
import com.vodafone.gdma.util.BaseSpringImplTest;
import com.vodafone.gdma.util.ServerUtil;

public class ServerUtilTest extends BaseSpringImplTest {

    protected static Logger LOG = Logger.getLogger(ServerUtilTest.class);

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private TableDao tableDao;

    @Autowired
    private ServerUtil serverUtil;

    @Test
    public void resyncTableList() throws Exception {
        assertNotNull(serverDao);
        List servers = serverDao.get();

        assertTrue(servers.size() > 0);

        LOG.debug("Count: " + servers.size());

        for (Object o : servers) {
            Server server = (Server) o;
            if (!"DEV-TDW".equals(server.getName())) {
                LOG.debug(server.getId() + ":" + server.getName());
                for (Table table : server.getTables()) {
                    LOG.debug(table.getName());
                }
                serverUtil.resyncTableList(server);
            }
        }

    }

    @Test
    public void resyncColumnList() throws Exception {
        assertNotNull(serverDao);
        assertNotNull(tableDao);
        Server server = serverDao.get(43L);

        LOG.debug("Server: " + server.getName());
        Set<Table> tables = server.getTables();
        assertTrue(tables.size() > 0);

        LOG.debug("Count: " + tables.size());

        for (Object o : tables) {
            Table table = (Table) o;
            serverUtil.resyncColumnList(server, table);
        }

    }

}
