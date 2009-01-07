package com.vodafone.gdma.dao.hibernate;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vodafone.gdma.dao.UserDao;
import com.vodafone.gdma.domain.User;

/**
 * @author Ronan Gill
 * 
 */
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    public List<User> get() {
        return getHibernateTemplate().loadAll(User.class);
    }

    public User get(Long id) {
        return (User) getHibernateTemplate().load(User.class, id);
    }

    public User get(String username) {
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
                        "select distinct user from User user where user.id in ( select user.id from User user join user.tables table where table.id = ? ) order by userName asc",
                        tableId);
    }

    @SuppressWarnings("unchecked")
    public List<User> getCannotAccess(Long tableId) {
        return getHibernateTemplate()
                .find(
                        "select distinct user from User user where user.id not in ( select user.id from User user join user.tables table where table.id = ? ) order by userName asc",
                        tableId);
    }
}
