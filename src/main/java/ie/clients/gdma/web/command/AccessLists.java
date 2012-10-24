package ie.clients.gdma.web.command;

import ie.clients.gdma.domain.User;
import ie.clients.gdma.domain.UserAccess;

import java.util.ArrayList;
import java.util.List;


public class AccessLists {
    private List<User> canAccess = new ArrayList<User>();

    private List<User> canNotAccess = new ArrayList<User>();
	
	public List<User> getCanAccess() {
        return canAccess;
    }

    public void setCanAccess(List<User> canAccess) {
        this.canAccess = canAccess;
    }

    public List<User> getCanNotAccess() {
        return canNotAccess;
    }

    public void setCanNotAccess(List<User> canNotAccess) {
        this.canNotAccess = canNotAccess;
    }
    
}
