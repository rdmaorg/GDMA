package ie.clients.gdma.web;

import ie.clients.gdma.GdmaFacade;
import ie.clients.gdma.domain.Column;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.domain.User;
import ie.clients.gdma.web.command.PaginatedRequest;
import ie.clients.gdma.web.command.PaginatedResponse;
import ie.clients.gdma.web.command.UpdateRequest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.util.Assert;

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

        Assert.notNull(gdmaFacade, "gdmaFacade is null");
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        Assert.notNull(username, "username is null");

        this.gdmaFacade = gdmaFacade;
	}

	public List<Server> getServerTableList() {
		Assert.notNull(gdmaFacade, "gdmaFacade is null");
		String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        Assert.notNull(username, "username is null");

		return gdmaFacade.getServerDao().getServerTableList(username);
	}

	public List<Server> getTableDetails(Long serverId, Long tableId) {
        Assert.notNull(gdmaFacade, "gdmaFacade is null");
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        Assert.notNull(username, "username is null");
	    
		Server server = gdmaFacade.getServerDao().get(serverId);
		Table table = gdmaFacade.getTableDao().get(tableId);
		return gdmaFacade.getServerDao().getServerTableColumnList(serverId, tableId);
	}

	public PaginatedResponse getData(PaginatedRequest paginatedRequest) {
	    Assert.notNull(gdmaFacade, "gdmaFacade is null");
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        Assert.notNull(username, "username is null");

		return gdmaFacade.getDynamicDao().get(paginatedRequest);
	}

	public void addRecord(UpdateRequest updateRequest) {
        Assert.notNull(gdmaFacade, "gdmaFacade is null");
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        Assert.notNull(username, "username is null");

		gdmaFacade.getDynamicDao().addRecord(updateRequest);
	}

	public int deleteRecords(UpdateRequest updateRequest) {
        Assert.notNull(gdmaFacade, "gdmaFacade is null");
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        Assert.notNull(username, "username is null");

		return gdmaFacade.getDynamicDao().deleteRecords(updateRequest);
	}

	public int updateRecords(UpdateRequest updateRequest) {
        Assert.notNull(gdmaFacade, "gdmaFacade is null");
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        Assert.notNull(username, "username is null");
	    
		return gdmaFacade.getDynamicDao().updateRecords(updateRequest);
	}

	public List getDropDownData(Column display, Column store) {
        Assert.notNull(gdmaFacade, "gdmaFacade is null");
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        Assert.notNull(username, "username is null");

        return gdmaFacade.getDynamicDao().getDropDownData(display, store);
	}
}
