package ie.clients.gdma.domain;

public class UserAccess extends User {
    private boolean allowedAccess = false;

    public boolean isAllowedAccess() {
        return allowedAccess;
    }

    public void setAllowedAccess(boolean allowedAccess) {
        this.allowedAccess = allowedAccess;
    }
}
