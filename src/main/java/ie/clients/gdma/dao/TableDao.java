package ie.clients.gdma.dao;

import ie.clients.gdma.domain.Table;

import java.util.List;


/**
 * @author Ronan Gill
 * 
 */
public interface TableDao {

    public Table get(Long id);

    public Table save(Table table);

    public void save(List<Table> tables);

}
