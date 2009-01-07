package com.vodafone.gdma.dao;

import java.util.List;

import com.vodafone.gdma.domain.Table;

/**
 * @author Ronan Gill
 * 
 */
public interface TableDao {

    public Table get(Long id);

    public Table save(Table table);

    public void save(List<Table> tables);

}
