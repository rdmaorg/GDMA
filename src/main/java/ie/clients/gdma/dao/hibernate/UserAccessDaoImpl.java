package ie.clients.gdma.dao.hibernate;

import ie.clients.gdma.dao.UserAccessDao;
import ie.clients.gdma.domain.User;
import ie.clients.gdma.domain.UserAccess;
import ie.clients.gdma.web.GdmaAdminAjaxFacade;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * @author Ronan Gill
 * 
 */
public class UserAccessDaoImpl extends HibernateDaoSupport implements UserAccessDao {

	
	private static Logger LOG = Logger.getLogger(GdmaAdminAjaxFacade.class);
	
    @SuppressWarnings("unchecked")
	public List<UserAccess> get() {
        return getHibernateTemplate().loadAll(UserAccess.class);
    }
    
    /*public UserAccess get(Long tableId, Long userId) {
    	String queryString = "from ie.clients.gdma.domain.UserAccess where table_id = " + tableId + " and user_id = " + userId;
		return (UserAccess) getHibernateTemplate().find(queryString);
    	
        //List users = getHibernateTemplate().find("from User user where user.active = true and user.userName = ? ", username);
    	/*UserAccess userAccesss = getHibernateTemplate().find("from UserAccess userAccess where userAccess.tableId ? ", tableId);
        if (userAccesss == null || userAccesss.size() == 0)
            return null;
        else
            return (UserAccess) userAccesss.get(0);*/
    //}
    @SuppressWarnings("unchecked")
	public UserAccess get(Long tableId, Long userId) {
        //List users = getHibernateTemplate().find("from User user where user.active = true and user.userName = ? ", username);
    	Object[] params  = {tableId , userId};
    	List userAccess  = getHibernateTemplate().find("from UserAccess userAccess where userAccess.tableId = ? and userAccess.userId = ?", params);
        if (userAccess == null || userAccess.size() == 0)
            return null;
        else
            return (UserAccess) userAccess.get(0);
    }
    
    @SuppressWarnings("unchecked")
	public List<UserAccess> get(Long tableId) {
		//String queryString = "from gdma2_user_table_access where table_id = " + tableId;
		//return getHibernateTemplate().find(queryString);
    	List UserAccess = getHibernateTemplate().find("from UserAccess userAccess where userAccess.tableId = ? ", tableId);
    	return UserAccess;
    }
    /*@SuppressWarnings("unchecked")
	public List<UserAccess> get(Long tableId){
    	//Criterion crit = Restrictions.eq("active", Boolean.TRUE);
    	final Session session = getSession(); 

    	Criteria crit = session.createCriteria(UserAccess.class).add(Restrictions.eq("tableId", tableId));
    	List<UserAccess> result = crit.list(); 

    	return result;    	
    }*/
    
    
    
    
    
    public void save(UserAccess userAccess) {
        getHibernateTemplate().saveOrUpdate(userAccess);
    }

    public void delete(UserAccess UserAccess) {
        getHibernateTemplate().delete(UserAccess);
    }

    /*@SuppressWarnings("unchecked")
	public List<UserAccess> getAccess(Long tableId) {
    	return getHibernateTemplate()
                .find(
                        //"select distinct user from User user where user.id in ( select user.id from User user join user.tables table where table.id = ? and user.active = true) order by userName asc", tableId);
                		"select distinct userAccess from UserAccess userAccess where userAccess.tableId = ? ", tableId);
    }

    @SuppressWarnings("unchecked")
    public List<UserAccess> getCannotAccess(Long tableId) {
        return getHibernateTemplate()
                .find(
                        //"select distinct user from User user where user.id not in ( select user.id from User user join user.tables table where table.id = ? and user.active = true) order by userName asc",
                		"select distinct userAccess from UserAccess userAccess where userAccess.tableId = ? ", tableId);
    }*/

    
}
