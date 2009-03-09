package ie.clients.gdma.dao;

import ie.clients.gdma.domain.Column;

import java.util.List;


/**
 * @author Ronan Gill
 * 
 */
public interface ColumnDao {

    public Column get(Long id);

    public Column save(Column column);

    public void save(List<Column> columns);

}
