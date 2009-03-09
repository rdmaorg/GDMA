package ie.clients.gdma.dao;

import ie.clients.gdma.domain.ConnectionType;

import java.util.List;


/**
 * @author Ronan Gill
 * 
 */
public interface ConnectionTypeDao {

    public List get();

    public ConnectionType get(Long id);

}
