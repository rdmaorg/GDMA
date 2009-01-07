package com.vodafone.gdma.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.util.Assert;

import com.vodafone.gdma.GdmaFacade;
import com.vodafone.gdma.domain.Column;
import com.vodafone.gdma.domain.Server;
import com.vodafone.gdma.domain.Table;
import com.vodafone.gdma.domain.User;
import com.vodafone.gdma.web.command.PaginatedRequest;
import com.vodafone.gdma.web.command.PaginatedResponse;
import com.vodafone.gdma.web.command.UpdateRequest;

/**
 * This is a utility class which will supply the resources to DWR. It will also
 * trap Exceptions and create a user friendly message
 * 
 * @author ronan
 * 
 */
public class GdmaAjaxFacade {

    private static Logger LOG = Logger.getLogger(GdmaAjaxFacade.class);

    private GdmaFacade gdmaFacade;

    public void setGdmaFacade(GdmaFacade gdmaFacade) {
        Assert.notNull(gdmaFacade, "a null gdmaFacade was passed to GdmaAdminAjaxFacade");
        this.gdmaFacade = gdmaFacade;
    }

    public List<Server> getServerTableList() {
        Assert.notNull(gdmaFacade, "gdmaFacade is null");
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        return gdmaFacade.getServerDao().getServerTableList(username);
    }

    public List<Server> getTableDetails(Long serverId, Long tableId) {
        Server server = gdmaFacade.getServerDao().get(serverId);
        Table table = gdmaFacade.getTableDao().get(tableId);
        return gdmaFacade.getServerDao().getServerTableColumnList(serverId, tableId);
    }

    public PaginatedResponse getData(PaginatedRequest paginatedRequest) {
        return gdmaFacade.getDynamicDao().get(paginatedRequest);
    }

    public void addRecord(UpdateRequest updateRequest) {
        gdmaFacade.getDynamicDao().addRecord(updateRequest);
    }

    public int deleteRecords(UpdateRequest updateRequest) {
        return gdmaFacade.getDynamicDao().deleteRecords(updateRequest);
    }

    public int updateRecords(UpdateRequest updateRequest) {
        return gdmaFacade.getDynamicDao().updateRecords(updateRequest);
    }

    public List getDropDownData(Column display, Column store) {
        return gdmaFacade.getDynamicDao().getDropDownData(display, store);
    }
}
