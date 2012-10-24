package ie.clients.gdma.dao;

import ie.clients.gdma.domain.Server;

import java.util.List;


/**
 * @author Ronan Gill
 * 
 */
public interface ServerDao {

    public List<Server> get();

    public Server get(Long id);

    public Server getByColumn(Long columnId);

    public Server save(Server server);

    public void save(List<Server> servers);

    public void delete(Server server);

    public List<Server> getServerTableList(String username);

    public List<Server> getServerTableColumnList();

    public List<Server> getServerTableColumnList(Long serverId, Long tableId);

	public List<Server> getServerTableColumnListForDDDropdown();

}
