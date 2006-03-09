/*
 * Created on Mar 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.security;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tagish.auth.win32.NTSystem;
import com.vodafone.gdma.dbaccess.DBUtil;
import com.vodafone.gdma.dbaccess.GDMAUserFactory;
import com.vodafone.gdma.util.Config;

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
                NTSystem ntSystem = new NTSystem();
                ntSystem.logon(user.getUserName(), user.getPassword().toCharArray(), user.getDomain());
                logger.debug("GDMAUser [" + user.getUserName()
                                + "] validated using domain ["
                                + user.getDomain() + "]");
                //Now that the user was validated - we don't need the password
                user.setPassword(null);
                //see if the user is allowed access and is admin
                String[] groups = ntSystem.getGroupNames(false);
                for(int i = 0; i < groups.length; i++){
                    logger.debug("GDMAUser [" + user.getUserName()
                                + "] in Group ["
                                + groups[i] +"]");
                }
                if(!isMemberOf(groups,Config.getProperty("UserGroup")))
                    throw new Exception(" GDMAUser ["+ user.getUserName() +  "] is not member of the group ["+ Config.getProperty("UserGroup") +  "]");
               
                user.setAdmin(isMemberOf(groups,Config.getProperty("AdminGroup")));
                
                // valid NT User check if Valid GDMA user by getting ID
                Long userId  = Security.getGDMAUserIdFromDatabase(user.getUserName().trim());

                if (userId == null)
                { 	ret = "Username entered is not a valid GDMA username.";                
                	throw new Exception(ret);
                }
                               
                user.setUserId(userId);                
            } 
            catch (Exception e) 
            {
                ret = e.getMessage();
                logger.error(e.getMessage(),e);
                logger.debug("Exception in logon - " + e.getMessage());
                //logger.debug(user.toString());
            }
        }
        return ret;
    }

    //Check that the user is a normal user
    private static boolean isMemberOf(String[] groups, String group) throws Exception{
        if(group == null)
            return false;
        group = group.trim();
        for(int i = 0; i < groups.length; i++){
            if(group.equalsIgnoreCase(groups[i]))
                return true;
        }
        return false;
    }
    
    // GET THE GDMA USERID FROM THE GDMA_USERS TABLE
    public static Long getGDMAUserIdFromDatabase(String username) throws Exception 
    {
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "select USER_ID from GDMA_USERS where USERNAME='"+username+"'";
                
        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            if(rs.next()) // record exists 
            {
            	return new Long(rs.getLong("USER_ID"));
            }
            else
            {
            	return null;
            }
            
        } 
        catch (Exception ex) 
        {
            logger.error(ex.getMessage(),ex);
            throw ex;
        } 
        finally {
        	 try {
                 if (rs != null) rs.close();
             } catch (Exception e) {
                 logger.error("Exception while closing ResultSet", e);
             }
             try {
                 if (stmt != null) stmt.close();
             } catch (Exception e) {
                 logger.error("Exception while closing Statement", e);
             }
             try {
                 if (con != null) con.close();
             } catch (Exception e) {
                 logger.error("Exception while closing Connection", e);
             }
        }        		 
    }
}
