/*
 * Created on Mar 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.security;

import org.apache.log4j.Logger;

import com.tagish.auth.win32.NTSystem;

/**
 * @author RGILL
 * 
 * This class handles all the security for the system
 */
public class Security {

    // Log4j logger
    private static Logger logger = Logger.getLogger("Security");

    public static String logon(User user) {
        String ret = null;
        if (user.getUserName() == null)
            ret = "Please enter a username";
        else if (user.getPassword() == null)
            ret = "Please enter a password";
        else if (user.getDomain() == null)
            ret = "Please select a domain";
        else {
            try {
                //TODO: Fix this
                /*
                NTSystem ntSystem = new NTSystem();
                ntSystem.logon(user.getUserName(), user.getPassword()
                        .toCharArray(), user.getDomain());
                logger
                        .debug("User [" + user.getUserName()
                                + "] validated using domain ["
                                + user.getDomain() + "]");
                //Now that the user was validated - we don't need the password
                user.setPassword(null);
                //see if the user is allowed access and is admin
                String[] groups = ntSystem.getGroupNames(false);
                if(!isMemberOf(groups,Config.getProperty("UserGroup")))
                    throw new Exception(" User ["+ user.getUserName() +  "] is not member of the group ["+ Config.getProperty("UserGroup") +  "]");
                user.setAdmin(isMemberOf(groups,Config.getProperty("AdminGroup")));
                */
                user.setAdmin(true);
            } catch (Exception e) {
                ret = e.getMessage();
                logger.debug("Exception in logon - " + e.getMessage());
                logger.debug(user.toString());
            }
        }
        return ret;
    }

    //Check that the user is a normal user
    private static boolean isMemberOf(String[] groups, String group) throws Exception{
        for(int i = 0; i < groups.length; i++){
            if(group.equalsIgnoreCase(groups[i]))
                return true;
        }
        return false;
    }
}
