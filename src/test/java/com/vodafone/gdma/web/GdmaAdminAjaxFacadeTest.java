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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vodafone.gdma.domain.Column;
import com.vodafone.gdma.domain.ConnectionType;
import com.vodafone.gdma.domain.Server;
import com.vodafone.gdma.domain.Table;
import com.vodafone.gdma.domain.User;
import com.vodafone.gdma.util.BaseSpringImplTest;
import com.vodafone.gdma.web.command.AccessLists;

/**
 * 
 * @author ronan
 * 
 */
public class GdmaAdminAjaxFacadeTest extends BaseSpringImplTest {

    private static Logger LOG = Logger.getLogger(GdmaAdminAjaxFacadeTest.class);

    @Autowired
    private GdmaAdminAjaxFacade gdmaAdminAjaxFacade;

    @Before
    public void checkFacade() {
        assertNotNull("GdmaAdminAjaxFacade was not wired up correctly and is null", gdmaAdminAjaxFacade);
    }

    @Test
    public void getServers() {
        List<Server> servers = gdmaAdminAjaxFacade.getServers();
        assertNotNull("List of servers from gdmaFacade.getServerDao() Is null", servers);
        assertTrue("List of severs is empty", servers.size() > 0);
        if (LOG.isDebugEnabled()) {
            for (Server server : servers) {
                LOG.debug(server.getId() + ":" + server.getName());
            }
        }
    }

    @Test
    public void saveServers() {

        Server testServer;

        String testServerFile = "com/vodafone/gdma/web/server.xml";
        XStream xstream = new XStream(new PureJavaReflectionProvider(), new DomDriver());
        testServer = (Server) xstream.fromXML(getClass().getClassLoader().getResourceAsStream(testServerFile));

        List<Server> servers = gdmaAdminAjaxFacade.getServers();
        int count = servers.size();
        servers.add(testServer);
        gdmaAdminAjaxFacade.saveServers(servers);
        servers = gdmaAdminAjaxFacade.getServers();
        assertTrue("Does appear that the testServer was added to the collection", count + 1 == servers.size());
    }

    @Test
    public void getTablesForServer() {
        Server testServer = getServer();
        Set<Table> tables = gdmaAdminAjaxFacade.getTablesForServer(testServer.getId());
        assertNotNull("Returned list of tables for server is null", tables);
        // tables should have been populated by the facade
        assertTrue("Returned list of tables for server is empty", CollectionUtils.isNotEmpty(tables));
    }

    @Test
    public void saveTables() {
        Server testServer = getServer();
        Set<Table> tables = gdmaAdminAjaxFacade.getTablesForServer(testServer.getId());
        for (Table table : tables) {
            table.setDisplayed(true);
        }

    }

    @Test
    public void getColumnsForTable() {
        Set<Column> columns = gdmaAdminAjaxFacade.getColumnsForTable(271L, 298L);
        assertNotNull(columns);
    }

    @Test
    public void getConnectionTypes() {
        List<ConnectionType> connectionTypes = gdmaAdminAjaxFacade.getConnectionTypes();
        assertNotNull(connectionTypes);
        assertTrue(CollectionUtils.isNotEmpty(connectionTypes));
    }

    @Test
    public void getAccessListForTable() {
        AccessLists accessLists = gdmaAdminAjaxFacade.getAccessListForTable(115L);
        List<User> users = accessLists.getCanAccess();
        for (User user : users) {
            LOG.debug("Can:" + user.getUserName());
        }
        users = accessLists.getCanNotAccess();
        for (User user : users) {
            LOG.debug("Cannot:" + user.getUserName());
        }
    }

    private Server getServer() {
        List<Server> servers = gdmaAdminAjaxFacade.getServers();
        for (Server server : servers) {
            if ("old_hr".equals(server.getName())) {
                return server;
            }
        }
        return null;
    }

}
