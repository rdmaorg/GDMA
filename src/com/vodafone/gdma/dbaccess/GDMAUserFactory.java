package com.vodafone.gdma.dbaccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author SOCONNELL
 * 
 * This class is used to create and retrieve USER Permissions records in the database.
 * For now, the code only works with an oracle database.
 * 
 */
public class GDMAUserFactory extends DBFactory {

    private static GDMAUserFactory instance = null;

    // Log4j logger
    private static Logger logger = Logger.getLogger(GDMAUserFactory.class);

    public GDMAUser getGDMAUser(Long id) {
        GDMAUser user = null;
        
        if(id == null)
            return null;
        
        for (int i = 0; i <= list.size(); i++) {
        	user = (GDMAUser) list.get(i);
            if (id.equals(user.getId())) return user;
        }
        return null;
    }
    
    public GDMAUserFactory() throws Exception {
        buildList();
    }

    /*
     * Build a list of users in the database 
     * 
     * (non-Javadoc)
     * @see com.vodafone.gdma.dbaccess.DBFactory#buildList()
     */
    public synchronized void buildList() throws ClassNotFoundException,
            SQLException, IOException, Exception {
    	
    	 // Create the ArrayList which will hold records
        list = new ArrayList();
        
    	 // Create the TreeMap whcih will hold the USERS
        java.sql.Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "select * from GDMA_USERS order by USERNAME";
        // Create the ArrayList which will hold the users
        list = new ArrayList();

        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs != null && rs.next()) {
                GDMAUser user = new GDMAUser();

                /* CREATE TABLE GDMA.GDMA_USERS 
                 * (
                 *     USER_ID    NUMBER(9)     NOT NULL,
                 *     FIRST_NAME VARCHAR2(255)     NULL,
                 *     LAST_NAME  VARCHAR2(255)     NULL,
                 *     USERNAME   VARCHAR2(255) NOT NULL
                 *  )                  
                 */
                user.setId(new Long(rs.getLong("USER_ID")));
                user.setFirstName(rs.getString("FIRST_NAME"));
                user.setLastName(rs.getString("LAST_NAME"));
                user.setUserName(rs.getString("USERNAME"));
                                
                list.add(user);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw e;
        } finally {
            closeAll(con, stmt, rs);
        }        		 
    }
    
    /*
     * Method to find permissions from this table using Userlist from db
     */
    public synchronized void buildAccessList(Long tableId) throws ClassNotFoundException, SQLException, IOException, Exception 
    {
    	Connection con = null;
	    Statement stmt = null;
	    try 
        {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
	    	ArrayList users = getList();
	    	for(int i=0; i< users.size(); i++)
	    	{
	    		StringBuffer query = new StringBuffer();
	    	
	    		GDMAUser user = (GDMAUser)users.get(i);
	    		query.append("select * from GDMA_USER_TABLE_ACCESS ");
	    		query.append(" where USER_ID= ");
	    		query.append(user.getId());
	    		query.append(" and TABLE_ID= ");
	    		query.append(tableId);

	    		ResultSet rs = null;
	    		rs = stmt.executeQuery(query.toString());

	            if(rs.next()) // record exists 
	            {
	            	user.setAllowedAccess(true);
	            }
	            else
	            {
	            	user.setAllowedAccess(false);
	            }
	    	}	
        } 
        catch (Exception e) 
        {
            logger.error(e.getMessage(),e);
            throw e;
        } 
        finally 
        {
            closeAll(con, stmt, null);
        }
    }
    
    /*
     * Method to check is a username is unique
     * (not checking same user i.e. update)
     */
    public boolean userNameUnique(GDMAUser userTest)
    {
    	String nameTest = userTest.getUserName();
    	Long idTest = userTest.getId();
    	
    	for(int i=0; i<list.size(); i++)
    	{
    	   GDMAUser user = (GDMAUser)list.get(i);
    	   if(user.getUserName().equalsIgnoreCase(nameTest.trim()))
    	   {   
        	   if(user.getId().equals(idTest)) 
    		   {
        		   // Logins are the same BUT we are comparing the same USER i.e. AN UPDATE
        		   // TEST OK
        		   return true; 
    		   }
        	   else
        	   { 
        		   // Logins are the same for a NEW USER
        		   // TEST NOT OK
        		   return false;
        	   }
    	   }
    	}    	
        return true;	
    }
    

    /*
     * INSERT a GDMAUser into GDMA_USERS
     * 
     */
    public void addGDMAUser(GDMAUser user) throws Exception 
    {    	
    	if(user.getId()!=null)
    	{
    		updateGDMAUser(user);
    	}
    	else 
    	{  
	        Connection con = null;
	        Statement stmt = null;
	        StringBuffer sbInsert = new StringBuffer();
	
	        sbInsert.append("INSERT INTO GDMA_USERS  ");
	        sbInsert.append("(FIRST_NAME, LAST_NAME, USERNAME) VALUES (");
	        sbInsert.append("'");
	        sbInsert.append(escapeSpecials(user.getFirstName()));
	        sbInsert.append("','");
	        sbInsert.append(escapeSpecials(user.getLastName()));
	        sbInsert.append("','");
	        sbInsert.append(escapeSpecials(user.getUserName()));
	        sbInsert.append("')");
		        
	        try 
	        {
	            con = DBUtil.getConnection();
	            stmt = con.createStatement();
	            stmt.executeUpdate(sbInsert.toString());
	        } 
	        catch (Exception e) 
	        {
	            logger.error(e.getMessage(),e);
	            throw e;
	        } 
	        finally 
	        {
	            closeAll(con, stmt, null);
	        }
    	}
    }
    
    /*
     * escapeSpecials for SQL String like APOSTROPHE
     * 
     */
    public String escapeSpecials(String escapeThis) throws Exception 
    {
        return escapeThis.replaceAll("'", "''");
    	
    }
    
    /*
     * <p>UPDATE a GDMAUser in GDMA_USERS
     * update GDMA_USERS set  FIRST_NAME='...', LAST_NAME='...', USERNAME='..' where USER_ID...
     */
    public void updateGDMAUser(GDMAUser user) throws Exception 
    {
    	
        Connection con = null;
        Statement stmt = null;
        StringBuffer sbUpdate = new StringBuffer();
        sbUpdate.append("update GDMA_USERS set ");
        sbUpdate.append(" FIRST_NAME='");
        sbUpdate.append(user.getFirstName());
        sbUpdate.append("',");
        sbUpdate.append(" LAST_NAME='");
        sbUpdate.append(user.getLastName());
        sbUpdate.append("',");
        sbUpdate.append(" USERNAME='");
        sbUpdate.append(user.getUserName());
        sbUpdate.append("'");
        sbUpdate.append(" where USER_ID=");
        sbUpdate.append(user.getId());
        
        try {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbUpdate.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw e;
        } finally {
            con.commit(); 
            closeAll(con, stmt, null);
        }
    
    }
    /*
     * DELETE a GDMAUser in GDMA_USERS
     * DELETE a GDMAUser's Priviledges in GDMA_USER_TABLE_ACCESS 
     * 
     */
    public void deleteGDMAUser(GDMAUser user) throws Exception 
    {
    	deleteGDMAUserEntry(user);
    	deleteGDMAUserPriviledges(user);
    }
    
    /*
     * DELETE a GDMAUser in GDMA_USERS
     * DELETE FROM GDMA_USERS WHERE USER_ID= ...
     * 
     */
    public void deleteGDMAUserEntry(GDMAUser user) throws Exception 
    {
        Connection con = null;
        Statement stmt = null;
        StringBuffer sbDelete = new StringBuffer();
        
        sbDelete.append("DELETE FROM GDMA_USERS WHERE USER_ID=");
        sbDelete.append(user.getId());

        try 
        {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbDelete.toString());
        } 
        catch (Exception e) 
        {
            logger.error(e.getMessage(),e);
            throw e;
        } 
        finally 
        {
            con.commit(); 
            closeAll(con, stmt, null);
        }    
    }
    
    /*
     * DELETE a GDMAUser's Priviledges in GDMA_USER_TABLE_ACCESS 
     * DELETE FROM GDMA_USER_TABLE_ACCESS  WHERE USER_ID= ...
     * 
     */
    public void deleteGDMAUserPriviledges(GDMAUser user) throws Exception 
    {
        Connection con = null;
        Statement stmt = null;
        StringBuffer sbDelete = new StringBuffer();
        
        sbDelete.append("DELETE FROM GDMA_USER_TABLE_ACCESS WHERE USER_ID=");
        sbDelete.append(user.getId());

        try 
        {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbDelete.toString());
        } 
        catch (Exception e) 
        {
            logger.error(e.getMessage(),e);
            throw e;
        } 
        finally 
        {
            con.commit(); 
            closeAll(con, stmt, null);
        }    
    }
    
    
    /*
     * Remove User/Table permissions 
     * 
     */
    public void removePermissions(Long tableId) throws Exception 
    {   
    	 Connection con = null;
         Statement stmt = null;
         StringBuffer sbDelete = new StringBuffer();
         
         sbDelete.append("DELETE FROM GDMA_USER_TABLE_ACCESS WHERE TABLE_ID=");
         sbDelete.append(tableId);
         
         try {
             con = DBUtil.getConnection();
             stmt = con.createStatement();
             stmt.executeUpdate(sbDelete.toString());
         } catch (Exception e) {
             logger.error(e.getMessage(),e);
             throw e;
         } finally {
             con.commit(); 
             closeAll(con, stmt, null);
         }
         
    }
    
    /*
     * Save User/Table permissions 
     * 
     */
    public void savePermissions(Long tableId, Long userId) throws Exception 
    {    	
    	  
        Connection con = null;
        Statement stmt = null;
                
        StringBuffer sbInsert = new StringBuffer();

        sbInsert.append("INSERT INTO GDMA_USER_TABLE_ACCESS  ");
        sbInsert.append("(USER_ID, TABLE_ID) VALUES ( ");
        sbInsert.append(userId);
        sbInsert.append(", ");
        sbInsert.append(tableId);
        sbInsert.append(")");
        
        try 
        {
            con = DBUtil.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(sbInsert.toString());
        } 
        catch (Exception e) 
        {
            logger.error(e.getMessage(),e);
            throw e;
        } 
        finally 
        {
            closeAll(con, stmt, null);
        }
    
    }
}
