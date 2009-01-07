package com.vodafone.gdma.dao;

import java.util.List;

import com.vodafone.gdma.domain.Server;

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

}
