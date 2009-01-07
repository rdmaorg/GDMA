package com.vodafone.gdma.dao;

import java.util.List;

import com.vodafone.gdma.domain.Column;
import com.vodafone.gdma.domain.Server;
import com.vodafone.gdma.domain.Table;
import com.vodafone.gdma.web.command.Filter;
import com.vodafone.gdma.web.command.PaginatedRequest;
import com.vodafone.gdma.web.command.PaginatedResponse;
import com.vodafone.gdma.web.command.PaginatedSqlResponse;
import com.vodafone.gdma.web.command.PaginatedSqlRequest;
import com.vodafone.gdma.web.command.UpdateRequest;

public interface DynamicDao {

    /**
     * @param paginatedRequest
     * @return
     */
    public PaginatedResponse get(PaginatedRequest paginatedRequest);

    /**
     * @param server
     * @param table
     * @return
     */
    public Long getCount(Server server, Table table, List<Filter> filters);

    /**
     * @param updateRequest
     */
    public void addRecord(UpdateRequest updateRequest);

    /**
     * Update one or more records
     * 
     * @param updateRequest
     * @return the numebr of records updated
     */
    public int updateRecords(UpdateRequest updateRequest);

    /**
     * Delete one or more records
     * 
     * @param updateRequest
     * @return the number of records delete
     */
    public int deleteRecords(UpdateRequest updateRequest);

    /**
     * Get a list of drop down values and text for a column
     * 
     * @param display
     *            the display column
     * @param store
     *            the column to store
     * @return
     */
    public List getDropDownData(Column display, Column store);

    /**
     * Get the list of columns for a select through the SQL Console
     * 
     * @param serverId
     * @param sql
     * @return
     */
    public List<Column> executeSelectGetColumns(Long serverId, String sql);

    /**
     * Execute a select from the SQL console
     * 
     * @param paginatedRequest
     * @return
     */
    public PaginatedSqlResponse executeSelect(PaginatedSqlRequest paginatedSqlRequest);

}
