package ie.clients.gdma.util;

import static org.junit.Assert.assertNotNull;
import ie.clients.gdma.dao.ConnectionTypeDao;
import ie.clients.gdma.dao.ServerDao;
import ie.clients.gdma.dao.TableDao;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.util.SqlUtil;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author RGILL
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class SqlUtilTest extends BaseSpringImplTest {

    private static Logger LOG = Logger.getLogger("SqlUtil");

    @Autowired
    private ServerDao serverDao;

    @Autowired
    private TableDao tableDao;

    @Autowired
    private ConnectionTypeDao connectionTypeDao;

    @Test
    public void createSelect() {
        assertNotNull(serverDao);
        assertNotNull(tableDao);

        Server server = serverDao.get(287L);
        Table table = tableDao.get(316L);

        String sql = SqlUtil.createSelect(server, table, null, "asc", null);

        assertNotNull(sql);
        LOG.debug(sql);
    }

}