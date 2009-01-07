package com.vodafone.gdma.dao;

import java.util.List;

import com.vodafone.gdma.domain.Column;

/**
 * @author Ronan Gill
 * 
 */
public interface ColumnDao {

    public Column get(Long id);

    public Column save(Column column);

    public void save(List<Column> columns);

}
