/*
 * Created on Mar 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.security;


/**
 * @author RGILL
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class User {
    private String userName;
    private String password;
    private boolean admin;
    private String domain;

    /**
     * @return Returns the admin.
     */
    public boolean isAdmin() {
        return admin;
    }
    /**
     * @param admin The admin to set.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    /**
     * @return Returns the domain.
     */
    public String getDomain() {
        return domain;
    }
    /**
     * @param domain The domain to set.
     */
    public void setDomain(String domain) {
        if(domain != null)
            this.domain = domain.trim();
        else
            this.domain = null; 
    }
    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        if(password != null)
            this.password = password.trim();
        else
            this.password = null;        
    }
    /**
     * @return Returns the userName.
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @param userName The userName to set.
     */
    public void setUserName(String userName) {
        if(userName != null)
            this.userName = userName.trim();
        else
            this.userName = null; 
    }
    
    public String toString(){
        StringBuffer sbTemp = new StringBuffer();
        sbTemp.append("Username      :");
        sbTemp.append(userName);
        sbTemp.append("\nPassword      :");
        sbTemp.append(password);
        sbTemp.append("\nDomain        :");
        sbTemp.append(domain);
        sbTemp.append("\nIsAdmin       :");
        sbTemp.append(admin);
        return sbTemp.toString();
    }
}
