package com.vodafone.gdma.dbaccess;


/**
 * @author SOCONNELL
 *
 * This class represents a GDMA USER as taken from the GDMA_USERS table.
 */
public class GDMAUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
          
    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
    
	public String toString()
	{
		
	  return "GDMAUser[" +
	  		 " id: "+ id +
      		 " firstName: "+ firstName +
      		 " lastName: "+ lastName +
      		 " userName: "+ userName+
      		 "]";	
	}
    
}
