<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
    GDMAUserFactory userFac = new GDMAUserFactory();
     
    if("POST".equalsIgnoreCase(request.getMethod()))
    {
		if("Save".equalsIgnoreCase(request.getParameter("userAction")))
   		{
  			if(request.getParameter("newUserFirstName")!= null
				&& request.getParameter("newUserLastName")!= null
				&& request.getParameter("newUserName")!= null)
			{
               
        	   		GDMAUser user = new GDMAUser();
               		user.setFirstName(request.getParameter("newUserFirstName"));
               		user.setLastName(request.getParameter("newUserLastName")); 
               		user.setUserName(request.getParameter("newUserName"));
               		if(request.getParameter("oldUserId")!=null && !(request.getParameter("oldUserId").trim()).equals(""))
	              	{	 user.setId(Long.valueOf(request.getParameter("oldUserId")));
                    }
             	    
                    if(userFac.userNameUnique(user))
	                {  
                       userFac.addGDMAUser(user);
                    }
               		else
               		{%>
                  		<script>
                    		alert("Cannot save. A user exists with that username.");
	                  </script>
    	           <%}
            }
            else
            {%>
                <script>
                    alert("Cannot save. All user fields are manadatory.");
                </script>
           <% }
        }
        
        if("Delete".equalsIgnoreCase(request.getParameter("userAction")))
   		{
            GDMAUser user = new GDMAUser();
            user.setId(Long.valueOf(request.getParameter("oldUserId")));
            userFac.deleteGDMAUser(user);            
        }

        userFac.refreshList();
    }
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Generic Data Maintenance Application - User Maintenance</title>
        <script language="javascript" src="js/general.js"></script>
        <script language="javascript" src="js/UserMaintenance.js"></script>
    </head>
<body class="nomargin nopadding">
<form name="userMaintenance" method="post" action="UserMaintenance.jsp">
<table border="0" cellpadding="5" cellspacing="5" >
    <tr>
        <td width="50px">&nbsp;</td> 
        <td class="formHeader" >User Maintenance</td>
   </tr>
    <tr>
        <td width="50px">&nbsp;</td> 
        <td> 
          <input type="hidden" id="userAction" name="userAction" value="">&nbsp;        
          <input type="button" class="button" id="btnSave" name="btnSave" value="Save" onClick="submitThisForm('Save');">&nbsp;
          <input type="button" class="button" id="btnDelete" name="btnDelete" value="Delete" onClick="submitThisForm('Delete');">&nbsp;
          <input type="button" class="button" id="btnEdit" name="btnEdit" value="Edit" onclick="populateUserForm();" >&nbsp;
          <input type="button" class="button" id="btnNew" name="btnNew" value="New" onclick="clearUserForm();" >&nbsp;
         </td>
    </tr>

    <tr  name="userForm" id="userForm">
        <td width="50px">&nbsp;</td> 
        <td>
        <table>
        	<tr>
                <%-- If the id==null then its an insert user if not then its an update user --%>
        		<input type="hidden" name="oldUserId" id="oldUserId">
        		<td align="left" valign="top" class="formLabel">First Name:</td>                                        
        		<td class="formInput"><input type="text" name="newUserFirstName" id="newUserFirstName"></td>
    		</tr>
    
     		<tr>
	        	<td align="left" valign="top" class="formLabel">Last Name:</td>                                        
       			<td class="formInput"><input type="text" name="newUserLastName" id="newUserLastName"></td>
    		</tr>
    
     		<tr>
        		<td align="left" valign="top" class="formLabel">Username:</td>                                        
		        <td class="formInput"><input type="text" name="newUserName" id="newUserName"></td>
    		</tr>
        </table>
        </td>
    <tr>
        <td width="50px">&nbsp;</td> 
        <td>
            <table border="0" cellpadding="3" cellspacing="0" 
                   class="dataTable">
                <tr>
                    <td class="dataHeader" style="width:100">First Name</td>
                    <td class="dataHeader" style="width:100">Last Name</td>
                    <td class="dataHeader" style="width:100">Username</td> 
                    <td class="dataHeader" style="width:50">Select</td> 
                </tr>
<%
    //convert tables ArrayList to a HashMap for easier look ups
    HashMap usersMap = new HashMap();
    ArrayList users = userFac.getList(); 
    
    for(int i = 0; i < users.size() ; i++)
    {             
%>
         <tr class="dataBody">
                    
              <input type="hidden" name="userFirstName<%=i%>" id="userFirstName<%=i%>" value="<%=((GDMAUser)users.get(i)).getFirstName()%>">                   
              <input type="hidden" name="userLastName<%=i%>" id="userLastName<%=i%>" value="<%=((GDMAUser)users.get(i)).getLastName()%>">                   
              <input type="hidden" name="userName<%=i%>" id="userName<%=i%>" value="<%=((GDMAUser)users.get(i)).getUserName()%>">                   
              <input type="hidden" name="userId<%=i%>" id="userId<%=i%>" value="<%=((GDMAUser)users.get(i)).getId()%>">                   

             <td class="dataGreyBorder"  style="width:100" >
                   <%=((GDMAUser)users.get(i)).getFirstName()%></td>
             </td>  
			 <td class="dataGreyBorder"  style="width:100">
                   <%=((GDMAUser)users.get(i)).getLastName()%></td>
             </td>  
			 <td class="dataGreyBorder"  style="width:100">
                   <%=((GDMAUser)users.get(i)).getUserName()%></td>
             </td> 
             <td class="dataGreyBorder"  style="width:50">
                  <input type="radio" <% if(i==0){%> checked <% }%> value="<%=i%>" name="selectedUserRadio" id="selectedUserRadio<%=i%>" ></td>
             </td> 
                
        </tr>
<%
    }   
%>

            </table>
        </td>
    </tr>
    <tr>
        <td width="50px">&nbsp;</td>
         <td> 
          <input type="button" class="button" id="btnSave" name="btnSave" value="Save" onClick="submitThisForm('Save');">&nbsp;
          <input type="button" class="button" id="btnDelete" name="btnDelete" value="Delete" onClick="submitThisForm('Delete');">&nbsp;
          <input type="button" class="button" id="btnEdit" name="btnEdit" value="Edit" onclick="populateUserForm();" >&nbsp;
          <input type="button" class="button" id="btnNew" name="btnNew" value="New" onclick="clearUserForm();" >&nbsp;
        </td>
    </tr>
 </table>
</form> 
</body>
</html>