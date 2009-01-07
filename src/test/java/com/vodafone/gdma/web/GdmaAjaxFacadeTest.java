package com.vodafone.gdma.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.vodafone.gdma.domain.Column;
import com.vodafone.gdma.domain.Server;
import com.vodafone.gdma.domain.Table;
import com.vodafone.gdma.util.BaseSpringImplTest;

/**
 * 
 * @author ronan
 * 
 */
public class GdmaAjaxFacadeTest extends BaseSpringImplTest {

    private static Logger LOG = Logger.getLogger(GdmaAjaxFacadeTest.class);

    @Autowired
    private GdmaAjaxFacade gdmaAjaxFacade;

    @Before
    public void checkFacade() {
        assertNotNull("GdmaAjaxFacade was not wired up correctly and is null", gdmaAjaxFacade);
    }

    @Test
    public void getColumnsForTable() {
        List<Server> servers = gdmaAjaxFacade.getTableDetails(271L, 298L);
        assertNotNull("Returned list of servers is null", servers);
        assertTrue("Returned list of servers is empty", CollectionUtils.isNotEmpty(servers));
        assertTrue("Returned list of servers has more than one element", servers.size() == 1);
        Set<Table> tables = servers.get(0).getTables();
        assertNotNull("Returned list of tables is null", tables);
        assertTrue("Returned list of tables is empty", CollectionUtils.isNotEmpty(tables));
        assertTrue("Returned list of tables has more than one element", tables.size() == 1);
        Set<Column> columns = tables.iterator().next().getColumns();
        assertNotNull("Returned list of columns is null", columns);
        assertTrue("Returned list of columns is empty", CollectionUtils.isNotEmpty(columns));
        assertTrue("Returned list of columns has only one elment", tables.size() == 1);

    }

    @Test
    public void getServerTableList() {
        List<Server> servers = gdmaAjaxFacade.getServerTableList("admin");
        assertNotNull("Returned list of servers is null", servers);
        assertTrue("Returned list of servers is empty", CollectionUtils.isNotEmpty(servers));
        for (Server server : servers) {
            assertNotNull("Server [" + server.getId() + "] name is null", server.getName());
            LOG.debug("Server: [" + server.getId() + "][" + server.getName() + "]");
            Set<Table> tables = server.getTables();
            assertNotNull("Returned list of tables for server [" + server.getId() + "] is null", tables);
            assertTrue("Returned list of tables for server [" + server.getId() + "] is empty", CollectionUtils
                    .isNotEmpty(tables));
            for (Table table : tables) {
                LOG.debug("  Table: [" + table.getId() + "][" + table.getName() + "]");
            }

        }
    }

}
