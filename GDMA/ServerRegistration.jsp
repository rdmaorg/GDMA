<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
    ServerRegistrationFactory fac = ServerRegistrationFactory.getInstance();
    ServerRegistration reg = null; 
    String strMode = "EDIT";
    if("POST".equalsIgnoreCase(request.getMethod()))
    {
        if("NEW".equals(request.getParameter("fldMode")))
        {//save the new registration
	        ServerRegistrationFactory.getInstance().addServerRegistration(
						        request.getParameter("fldName"),
                                request.getParameter("fldType"),
						        request.getParameter("fldUsername"),
						        request.getParameter("fldPassword"),
						        request.getParameter("fldURL")
						        );
            response.sendRedirect("ListServerRegistration.jsp");
            return;						        
        }
        else
        if("EDIT".equals(request.getParameter("fldMode")))
        {//update the registration
            ServerRegistrationFactory.getInstance().updateServerRegistration(
                                Integer.parseInt(request.getParameter("fldID")),
                                request.getParameter("fldName"),
                                request.getParameter("fldType"),                                
                                request.getParameter("fldUsername"),
                                request.getParameter("fldPassword"),
                                request.getParameter("fldURL")
                                );
            response.sendRedirect("ListServerRegistration.jsp");
            return;                                
        }
    } 
    else
    {
        strMode = request.getParameter("action");
        if("EDIT".equals(strMode)){
            String id = request.getParameter("id");
            reg = fac.getServerRegistration(Integer.parseInt(request.getParameter("id")));
        }
        else
        if("DELETE".equals(strMode))
        {//delet the registration
            ServerRegistrationFactory.getInstance().deleteServerRegistration(
                                Integer.parseInt(request.getParameter("id"))
                                );
            response.sendRedirect("ListServerRegistration.jsp");
            return;
        }
    }
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Add Server</title>
    </head>
<body>
<form name="frmServerRegistration" method="POST">
<input type="hidden" name="fldID" id="fldID" value="<%=reg == null ? "" : "" + reg.getId()%>">
<input type="hidden" name="fldMode" id="fldMode" value="<%=strMode%>">
    <table border="0" cellpadding="3" cellspacing="2">
        <tr>
            <td class="formHeader" colspan="2"><%="EDIT".equals(strMode)? "Edit" : "New" %> Server Registration</td>
        </tr>    
        <tr>
            <td class="formLabel">Name</td><td class="formValue">
<%
    if("EDIT".equals(strMode))
    {
%>
                <input class="formInput" type="hidden" name="fldName" 
                id="fldName" value="<%=reg.getName()%>"><%=reg.getName()%>
<%    
    }
    else
    {
%>            
                <input class="formInput" type="text" name="fldName" id="fldName">
<%
    }
%></td>
        </tr>
        <tr>
            <td class="formLabel">Conenction Type</td><td>
                <select class="formInput" name="fldType" id="fldType" value="<%=reg == null ? "" : "" + reg.getOdbcTypeID()%>">
<%
    ArrayList providers = ODBCProviderFactory.getInstance().getODBCProviderList();
    for(int i = 0 ; i < providers.size(); i++){
%>
                    <option value="<%=((ODBCProvider)providers.get(i)).getId()%>"><%=((ODBCProvider)providers.get(i)).getName()%></option>
<%    
    }
%>                
                    
                </select></td>
        </tr>
        <tr>
            <td class="formLabel">Username</td><td><input type="text" name="fldUsername" id="fldUsername"
                 class="formInput" value="<%=reg == null ? "" : reg.getUsername()%>"></td>
        </tr>
        <tr>
            <td class="formLabel">Password</td><td><input type="text" name="fldPassword" id="fldPassword"
                 class="formInput" value="<%=reg == null || reg.getPassword() == null ? "" : reg.getPassword()%>"></td>
        </tr>
        <tr>
            <td class="formLabel">URL</td><td><input type="text" name="fldURL" id="fldURL" 
                 class="formInput" value="<%=reg == null ? "" : reg.getConnectionURL()%>"></td>
        </tr>               
        <tr>
            <td colspan="2" align="right"><input type="button" id="btnBack" 
                name="btnBack" value="Back" class="button" 
                onclick="window.location.href='ListServerRegistration.jsp?ts=<%=(new Date()).getTime()%>'">&nbsp;<input
                type="submit" id="btnSubmit" name="btnSubmit" value="Save" class="button"> </td>
        </tr>     
        
    </table>
</form>    
</body>
</html>