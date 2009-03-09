package ie.clients.gdma.dao.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ie.clients.gdma.dao.UserDao;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.domain.User;
import ie.clients.gdma.util.BaseSpringImplTest;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class UserDaoImplTest extends BaseSpringImplTest {

    protected static Logger LOG = Logger.getLogger(UserDaoImplTest.class);

    @Autowired
    private UserDao userDao;

    @Test
    public void testloadAll() {
        assertNotNull(userDao);
        List users = userDao.get();
        assertTrue(users.size() > 0);
        for (Object o : users) {
            User user = (User) o;
            LOG.debug(user.getUserName());
            Set<Table> tables = user.getTables();
            for (Table table : tables) {
                LOG.debug(table.getName());
            }
        }

    }

    @Test
    public void loadByUserName() {
        assertNotNull(userDao);
        User user = userDao.get("admin");
        assertNotNull(user);
        LOG.debug(user.getId());
    }

    @Test
    public void getCannotListForTable() {
        assertNotNull(userDao);
        List<User> users = userDao.getAccess(111L);
        for (User user : users) {
            LOG.debug("Access   : " + user.getUserName());
        }
        users = userDao.getCannotAccess(111L);
        for (User user : users) {
            LOG.debug("NoAccess : " + user.getUserName());
        }
    }
}
