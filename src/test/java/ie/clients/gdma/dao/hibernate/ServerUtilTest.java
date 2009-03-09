package ie.clients.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ie.clients.gdma.dao.ServerDao;
import ie.clients.gdma.dao.TableDao;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.util.BaseSpringImplTest;
import ie.clients.gdma.util.ServerUtil;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


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
