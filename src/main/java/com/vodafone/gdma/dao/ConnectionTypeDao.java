package com.vodafone.gdma.dao;

import java.util.List;

import com.vodafone.gdma.domain.ConnectionType;

/**
 * @author Ronan Gill
 * 
 */
public interface ConnectionTypeDao {

    public List get();

    public ConnectionType get(Long id);

}
