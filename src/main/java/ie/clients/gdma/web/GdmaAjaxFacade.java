package ie.clients.gdma.web;

import ie.clients.gdma.GdmaFacade;
import ie.clients.gdma.domain.Column;
import ie.clients.gdma.domain.Server;
import ie.clients.gdma.domain.Table;
import ie.clients.gdma.domain.User;
import ie.clients.gdma.domain.UserAccess;
import ie.clients.gdma.web.command.PaginatedRequest;
import ie.clients.gdma.web.command.PaginatedResponse;
import ie.clients.gdma.web.command.UpdateRequest;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
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

		this.gdmaFacade = gdmaFacade;
	}

	public List<Server> getServerTableList() {
        LOG.debug("-----------------------------------------------------");
        LOG.debug("----------------  getServerTableList  ---------------");
        LOG.debug("-----------------------------------------------------");
	    
		Assert.notNull(gdmaFacade, "gdmaFacade is null");
		
		// Ensure that the user session is valid
		authenticateUser();
		
		String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName();
        Assert.notNull(username, "username is null");

		return gdmaFacade.getServerDao().getServerTableList(username);
	}

	
	public UserAccess getUserAccessDetails(Long serverId, Long tableId) {
		LOG.debug("-----------------------------------------------------");
        LOG.debug("----------------  getUserAccessDetails  ---------------");
        LOG.debug("-----------------------------------------------------");
	    
		Assert.notNull(gdmaFacade, "gdmaFacade is null");
		
		// Ensure that the user session is valid
		authenticateUser();
		
		Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		LOG.debug("userId : " + userId);
		
		UserAccess userAccess = gdmaFacade.getUserAccessDao().get(tableId, userId);		
		return userAccess;
	}
	
	public List<Server> getTableDetails(Long serverId, Long tableId) {
        LOG.debug("-----------------------------------------------------");
        LOG.debug("-----------------  getTableDetails  -----------------");
        LOG.debug("-----------------------------------------------------");
	    
        // Ensure that the user session is valid
        authenticateUser();
        	    
		Server server = gdmaFacade.getServerDao().get(serverId);
		Table table = gdmaFacade.getTableDao().get(tableId);
		return gdmaFacade.getServerDao().getServerTableColumnList(serverId, tableId);
	}

	public PaginatedResponse getData(PaginatedRequest paginatedRequest) {
        LOG.debug("-----------------------------------------------------");
        LOG.debug("-----------------      getData      -----------------");
        LOG.debug("-----------------------------------------------------");
	    
        // Ensure that the user session is valid
        authenticateUser();
        
		return gdmaFacade.getDynamicDao().get(paginatedRequest);
	}

	public void addRecord(UpdateRequest updateRequest) {
        LOG.debug("-----------------------------------------------------");
        LOG.debug("-----------------     addRecord     -----------------");
        LOG.debug("-----------------------------------------------------");
	    
        // Ensure that the user session is valid
        authenticateUser();
        
		gdmaFacade.getDynamicDao().addRecord(updateRequest);
	}

	public int deleteRecords(UpdateRequest updateRequest) {
        LOG.debug("-----------------------------------------------------");
        LOG.debug("-----------------   deleteRecords   -----------------");
        LOG.debug("-----------------------------------------------------");
        
        // Ensure that the user session is valid
        authenticateUser();
        
		return gdmaFacade.getDynamicDao().deleteRecords(updateRequest);
	}

	public int updateRecords(UpdateRequest updateRequest) {
        LOG.debug("-----------------------------------------------------");
        LOG.debug("-----------------   updateRecords   -----------------");
        LOG.debug("-----------------------------------------------------");
        
        // Ensure that the user session is valid
        authenticateUser();
        
		return gdmaFacade.getDynamicDao().updateRecords(updateRequest);
	}

	public List getDropDownData(Column display, Column store) {
        LOG.debug("-----------------------------------------------------");
        LOG.debug("---------------   getDropDownData   -----------------");
        LOG.debug("-----------------------------------------------------");
	    
        // Ensure that the user session is valid
        authenticateUser();
        
		return gdmaFacade.getDynamicDao().getDropDownData(display, store);
	}
	
	private void authenticateUser()
	{
        LOG.debug("----------------------------------------------------");
        LOG.debug("-------------    authenticateUser    ---------------");
        LOG.debug("----------------------------------------------------");
        User user = null;
        try{
            LOG.debug("SecurityContextHolder.getContext()");
            SecurityContext security = SecurityContextHolder.getContext();
            LOG.debug("security = " + security);
            Authentication authentication = security.getAuthentication();
            LOG.debug("authentication = " + authentication);
            LOG.debug("------------- 1 --------------");
            user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LOG.debug("------------- 2 --------------");
        }
        catch(Exception e){
            LOG.debug("------------- exception --------------");
            LOG.debug("cought exception e " + e);
            
            String principal = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LOG.debug("principal = " + principal);
            Assert.notNull(user, "Your Session has Expired please log out.");
        }	    
	}
}
