package ie.clients.gdma.domain;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author rgill
 *
 */
public class User implements Serializable {
    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String domain;

    private boolean admin;

    private boolean locked;

    private boolean active;

    private Set<UserAccess> userAccess = new LinkedHashSet<UserAccess>();
    
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<UserAccess> getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(Set<UserAccess> tables) {
        this.userAccess = userAccess;
    }
    
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof User))
            return false;
        final User that = (User) other;

        // aUser without a userName is invalid - so let the equals always be
        // null
        if (this.userName == null || that.getUserName() == null)
            return false;

        return this.userName.equals(that.getUserName());
    }

    public int hashCode() {
        return userName.hashCode();
    }

}
