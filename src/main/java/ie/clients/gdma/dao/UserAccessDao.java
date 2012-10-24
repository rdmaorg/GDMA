package ie.clients.gdma.dao;

import ie.clients.gdma.domain.User;
import ie.clients.gdma.domain.UserAccess;

import java.util.List;


/**
 * @author Ronan Gill
 * 
 */
public interface UserAccessDao {

    public List get();
    
    public void save(UserAccess userAccess);
    
    public void delete(UserAccess UserAccess);
    
    public List<UserAccess> get(Long tableId);
    
    public UserAccess get(Long tableId, Long userId);
    
    //public UserAccess get(Long tableId);

    /*public List<UserAccess> getAccess(Long tableId);

    public List<UserAccess> getCannotAccess(Long tableId);*/

}
