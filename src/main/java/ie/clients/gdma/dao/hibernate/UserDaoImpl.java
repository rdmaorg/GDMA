package ie.clients.gdma.dao.hibernate;

import ie.clients.gdma.dao.UserDao;
import ie.clients.gdma.domain.User;
import ie.clients.gdma.web.GdmaAdminAjaxFacade;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.apache.log4j.Logger;

/**
 * @author Ronan Gill
 * 
 */
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

	
	private static Logger LOG = Logger.getLogger(UserDaoImpl.class);
	
    /*public List<User> get() {
        return getHibernateTemplate().loadAll(User.class);
    }*/
    
    /*public List<User> get() {
		String queryString = "from gdma2_users where active = true";
		return getHibernateTemplate().find(queryString);
    }*/
	public List<User> get() {
    	//Criterion crit = Restrictions.eq("active", Boolean.TRUE);
    	final Session session = getSession(); 

    	Criteria crit = session.createCriteria(User.class).add(Restrictions.eq("active", Boolean.TRUE));
    	List<User> result = crit.list(); 

    	return result;    	
    }
	
	

   public User get(Long id) {
        return (User) getHibernateTemplate().load(User.class, id);
    }
    

    public User get(String username) {
        //List users = getHibernateTemplate().find("from User user where user.active = true and user.userName = ? ", username);
    	List users = getHibernateTemplate().find("from User user where user.userName = ? ", username);
        if (users == null || users.size() == 0)
            return null;
        else
            return (User) users.get(0);
    }

    public void save(List<User> users) {
        for (User user : users) {
            getHibernateTemplate().saveOrUpdate(user);
        }
    }

    public void delete(User User) {
        getHibernateTemplate().delete(User);
    }

    @SuppressWarnings("unchecked")
    public List<User> getAccess(Long tableId) {
    	
        return getHibernateTemplate()
                .find(
                        //"select distinct user from User user where user.id in ( select user.id from User user join user.tables table where table.id = ? and user.active = true) order by userName asc",
                		"select distinct user from User user where user.id in ( select userAccess.userId from UserAccess userAccess where userAccess.tableId = ? and user.active = true) order by userName asc",
                        tableId);
    }

    @SuppressWarnings("unchecked")
    public List<User> getCannotAccess(Long tableId) {
        return getHibernateTemplate()
                .find(
                        //"select distinct user from User user where user.id not in ( select user.id from User user join user.tables table where table.id = ? and user.active = true) order by userName asc",
                		"select distinct user from User user where user.id not in ( select userAccess.userId from UserAccess userAccess where userAccess.tableId = ? and user.active = true) order by userName asc",
                        tableId);
    }
}