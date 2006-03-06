<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
    String serverID = request.getParameter("server_id");
    String tableID = request.getParameter("table_id");
    Long lngServerID = new Long(serverID);
    Long lngTableID = new Long(tableID);
    ServerRegistrationFactory servFac = ServerRegistrationFactory.getInstance();
    ServerRegistration reg = servFac.getServerRegistration(lngServerID); 
    Table table = TableFactory.getInstance().getTable(lngTableID); 
    GDMAUserFactory userFac = new GDMAUserFactory();
    userFac.buildAccessList(table.getId());
    ArrayList users = userFac.getList(); 

  
   if("POST".equalsIgnoreCase(request.getMethod()))
    {
          userFac.removePermissions(table.getId());
          for(int i = 0; request.getParameter("fldUserId" +i) != null; i++)
          {
            //only save tables that are to be displayed
            if(request.getParameter("fldAccess" + i) != null)
            {
                userFac.savePermissions(table.getId(),Long.valueOf(request.getParameter("fldUserId" + i)));
            }
        }
        userFac.refreshList();
        userFac.buildAccessList(table.getId());
        users = userFac.getList(); 
    }
                            
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Generic Data Maintenance Application - Assign User/Table Permissions</title>
        <script language="javascript" src="js/general.js"></script>
        <script language="javascript" src="js/ListColumns.js"></script>
    </head>
<body class="nomargin nopadding">
<form method="post" action="TableAccessPermissions.jsp">
<table border="0" cellpadding="5" cellspacing="5" >
    <tr>
        <td width="50px">&nbsp;</td> 
        <td class="formHeader" ><%=reg.getName()%> - <%=table.getName()%> - Assign Permissions</td>
   </tr>
    <tr>
        <td width="50px">&nbsp;</td> 
        <td> 
          <input type="button" class="button" id="btnBack" name="btnBack" value="Back" onclick="window.location.href='ListTables.jsp?server_id=<%=serverID%>&ts=<%=(new Date()).getTime()%>'">&nbsp;        
          <input type="submit" class="button" id="btnSave" name="btnSave" value="Save" >&nbsp;
        </td>
    </tr>

    </tr>
    <tr>
        <td width="50px">&nbsp;</td> 
        <td>
            <table border="0" cellpadding="3" cellspacing="0" 
                   class="dataTable">
                <tr>
                    <td class="dataHeader" style="width:100">First Name</td>
                    <td class="dataHeader" style="width:100">Last Name</td>
                    <td class="dataHeader" style="width:100">Username</td> 
					<td class="dataHeader">Access</td> 
                </tr>
<%
    //convert tables ArrayList to a HashMap for easier look ups
    HashMap usersMap = new HashMap();
   
    for(int i = 0; i < users.size() ; i++)
    {       
        GDMAUser user = (GDMAUser)users.get(i);     
%>
         <tr class="dataBody">
                    
             <td class="dataGreyBorder"  style="width:100">
                   <%=user.getFirstName()%></td>
             </td>  
			 <td class="dataGreyBorder"  style="width:100">
                   <%=user.getLastName()%></td>
             </td>  
			 <td class="dataGreyBorder"  style="width:100">
                   <%=user.getUserName()%></td>
             </td> 
           
             <td class="dataGreyBorder" >
             <input name="fldAccess<%=i%>" id="fldAccess<%=i%>"  type="checkbox"  <%=(user==null?"":(user.isAllowedAccess()? "checked":""))%> >
             <input name="fldUserId<%=i%>" id="fldUserId<%=i%>"  type="hidden" value="<%=user.getId()%>">
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
        <td >
            <input name="mode" id="mode" type="hidden" value="SAVE_PERMISSIONS">
            <input name="server_id" id="server_id" type="hidden" value="<%=serverID%>">
            <input name="table_id" id="table_id" type="hidden" value="<%=tableID%>">            
            <input type="button" class="button" id="btnBack" name="btnBack" value="Back" onclick="window.location.href='ListTables.jsp?server_id=<%=serverID%>&ts=<%=(new Date()).getTime()%>'">&nbsp;        
            <input type="submit" class="button" id="btnSave" name="btnSave" value="Save" >&nbsp;
    </tr>
 </table>
</form> 
</body>
</html>