<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.security.*,
                 com.vodafone.gdma.util.*,
                 java.util.StringTokenizer"%>
<%
    String message = null;
    if("POST".equals(request.getMethod())){
        if("LOGOUT".equals(request.getParameter("action"))){
            session.removeAttribute("USER");
        }else{        
	        User user = new User();
	        user.setUserName(request.getParameter("username"));
	        user.setPassword(request.getParameter("password"));
	        user.setDomain(request.getParameter("domain"));
	        message = Security.logon(user);
	        if(message == null){
	            session.setAttribute("USER",user);
	            response.sendRedirect("index.jsp");
	            return;
	        }
        }
    }
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Generic Data Maintenance Application - Logon</title
        <script language="javascript" src="js/EditData.js"></script>        
    </head>
<body>
<form method="POST">
<br><br><br><br><br><br>
<table border="0" cellpadding="3" cellspacing="2" width="100%">
  <tr>
    <td class="formHeader" align="center" >Generic Data Maintenance Application - Logon</td>
  </tr> 
  <tr>
    <td align="center">
      <table border="0" cellpadding="3" cellspacing="2">
        <tr>
          <td class="formLabel">Username </td>
          <td class="formValue"><input class="formValue" type="text" name="username" value="" style="width:150px"></td>
        </tr>
        <tr>
          <td class="formLabel">Password </td>
          <td class="formValue"><input class="formValue" type="password" name="password" value="" style="width:150px"></td>    
        </tr>
        <tr>
          <td class="formLabel">Domain </td> 
          <td class="formValue">
           <select class="formValue" name="domain" style="width:150px">
<%
    String domains = null;
    String domain = null;
    boolean first = true;
    try{
        domains = Config.getProperty("DomainList");
    }catch(Exception e){
        domains = "NO DOMAINS FOUND";
    }
    StringTokenizer token = new StringTokenizer(domains,",");
    while(token.hasMoreTokens()){
        domain = token.nextToken();        
%>
                <option value="<%=domain%>" <%=first?"selected":""%>><%=domain%></option>
<%    
        first = false;
    }        
%>
           </select>
          </td>    
        </tr>    
        <tr>
          <td colspan="2" align="right">
            <input type="reset" value="Reset" class="button">&nbsp;
            <input type="submit" value="Login" class="button">
          </td>
        </tr>
      </table>
    </td>
  <tr>
</table>
<%
    if(message != null){
%>
    <script language="Javascript">
        alert("<%=Formatter.escapeAll(message)%>");
    </script>    
<%
    }
%>
<!--
    <script language="Javascript">
        document.getElementById('username').value = 'tomcatadmin';
        document.getElementById('password').value = 'password';
        document.getElementById('domain').value = 'localhost';
        document.forms[0].submit();
    </script>  
-->    
</body>
</html>