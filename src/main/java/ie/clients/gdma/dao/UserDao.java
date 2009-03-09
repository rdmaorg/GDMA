package ie.clients.gdma.dao;

import ie.clients.gdma.domain.User;

import java.util.List;


/**
 * @author Ronan Gill
 * 
 */
public interface UserDao {

    public List get();

    public User get(Long id);

    public User get(String username);

    public void save(List<User> users);

    public void delete(User user);

    public List<User> getAccess(Long tableId);

    public List<User> getCannotAccess(Long tableId);

}
