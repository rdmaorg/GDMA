package com.vodafone.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.vodafone.gdma.dao.ServerDao;
import com.vodafone.gdma.dao.TableDao;
import com.vodafone.gdma.domain.Server;
import com.vodafone.gdma.domain.Table;
import com.vodafone.gdma.util.BaseSpringImplTest;

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
