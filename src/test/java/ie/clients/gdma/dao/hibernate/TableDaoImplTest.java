package ie.clients.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import ie.clients.gdma.dao.ServerDao;
import ie.clients.gdma.dao.TableDao;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.util.BaseSpringImplTest;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class TableDaoImplTest extends BaseSpringImplTest {

    protected static Logger LOG = Logger.getLogger(TableDaoImplTest.class);

    @Autowired
    private TableDao tableDao;

    @Autowired
    private ServerDao serverDao;

    private Server server;

    @Before
    public void getConnectionType() {
        assertNotNull(serverDao);
        assertNotNull(tableDao);
        // need to get a valid server id
        server = serverDao.get().get(0);

        assertNotNull("Server is null", server);
    }

    @Test
    public void save() throws Exception {

        Table table = new Table();
        table.setName("junit-test-table");
        table.setDisplayed(true);
        table.setAllowDelete(true);

        server.getTables().add(table);

        assertNull("Table id is not null", table.getId());
        serverDao.save(server);
        // assertNotNull("Table id null", table.getId());

    }
}
