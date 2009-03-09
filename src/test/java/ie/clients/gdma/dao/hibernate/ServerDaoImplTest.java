package ie.clients.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ie.clients.gdma.dao.ConnectionTypeDao;
import ie.clients.gdma.dao.ServerDao;
import ie.clients.gdma.domain.Column;
import ie.clients.gdma.domain.ConnectionType;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.domain.User;
import ie.clients.gdma.util.BaseSpringImplTest;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ServerDaoImplTest extends BaseSpringImplTest {

    protected static Logger LOG = Logger.getLogger(ServerDaoImplTest.class);

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private ConnectionTypeDao connectionTypeDao;

    private ConnectionType connectionType;

    @Before
    public void getConnectionType() {
        assertNotNull(serverDao);
        assertNotNull(connectionTypeDao);
        List<ConnectionType> connectionTypes = connectionTypeDao.get();
        for (ConnectionType connectionType2 : connectionTypes) {
        	LOG.debug("Connection Name : " + connectionType2.getName());
            if ("JDBC Oracle 8 9".equals(connectionType2.getName())) {
                connectionType = connectionType2;
                break;
            }
        }
        assertNotNull(connectionType);
    }

    /**
     * Test most of the methods in here and let the TM roll it back
     */
    @Test
    public void all() {
        boolean deleteCalled = false;

        Server server = new Server();
        server.setName("junit-test-server");
        server.setUsername("username");
        server.setPassword("password");
        server.setConnectionUrl("connectionUrl");
        server.setConnectionType(connectionType);
        server.setPrefix("junit");

        serverDao.save(server);

        List<Server> servers = serverDao.get();
        assertTrue(servers.size() > 0);
        for (Server serverTemp : servers) {
            // make sure we find the server we created
            if (serverTemp.getName().equals(server.getName())) {
                serverDao.delete(serverTemp);
                deleteCalled = true;
            }
        }
        assertTrue(deleteCalled);
    }

    @Test
    public void getServerTableList() {
        // get a list of tables for the admin user
        List servers = serverDao.getServerTableList("admin");

        assertTrue(servers.size() > 0);

        for (Object o : servers) {
            Server server = (Server) o;
            assertTrue(server.getTables().size() > 0);
        }
    }

    @Test
    public void checkLazyLoad() {
        // get a list of tables for the admin user
        List servers = serverDao.get();

        assertTrue(servers.size() > 0);

        for (Object o : servers) {
            Server server = (Server) o;
            LOG.debug("Server: " + server.getName());
            LOG.debug("        " + server.getConnectionType().getName());
            LOG.debug("        " + server.getUsername());
            for (Table table : server.getTables()) {
                LOG.debug("        Table: " + table.getName());
                LOG.debug("               " + (table.isDisplayed() ? "visible" : "hidden"));
                for (Column column : table.getColumns()) {
                    LOG.debug("                       Column: " + column.getName());
                    LOG.debug("                               " + (column.isDisplayed() ? "visible" : "hidden"));
                }
                
                for (User user : table.getUsers()) {
                    LOG.debug("                       User: " + user.getUserName());
                }
            }
        }
    }

    @Test
    public void getServerTableColumnList() {
        List servers = serverDao.getServerTableColumnList(271L, 298L);
        assertTrue(servers.size() > 0);
    }

    @Test
    public void deleteServer() {
        Server server = serverDao.get(287L);
        LOG.debug("Loaded server : " + server.getName());
        serverDao.delete(server);
        LOG.debug("Deleted server : " + server.getName());
    }

    @Test
    public void getByColumn() {
        Server server = serverDao.getByColumn(318L);
        assertNotNull("Server is null", server);
        assertNotNull("Tables in server is null", server.getTables());
        assertTrue("Table list is empty ", server.getTables().size() != 0);
        assertTrue("More than one table returned ", !(server.getTables().size() > 1));
        Table table = server.getTables().iterator().next();
        assertNotNull("Columns in table is null", table.getColumns());
        assertTrue("Column list is empty ", table.getColumns().size() != 0);
        assertTrue("More than one column returned ", !(table.getColumns().size() > 1));
        Column column = table.getColumns().iterator().next();
        LOG.debug("Loaded server : " + column.getName());
    }
}
