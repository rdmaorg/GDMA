<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.security.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Generic Data Maintenance Application - Menu</title>
    </head>
<body class="redBackground nopadding nomargin">
    <table border="0" cellpadding="2" cellspacing="2" class="redBackground" height="100%"> 
        <tr height="25px">
            <td width="25px">&nbsp;</td>
<%
    User user = (User)session.getAttribute("USER");
    if(user != null){
        if(user.isAdmin()){
%>            
            <td valign="middle"><a href="ListServerRegistration.jsp" 
                target="main" class="topnav">Server Registrations</a></td>
            <td valign="middle"><a href="SQLConsoleFrame.jsp" 
                target="main" class="topnav">SQL Console</a></td> 
<%
    }
%>                            
            <td valign="middle"><a href="javascript:document.forms[0].submit();" 
                class="topnav">Logout</a></td>            
<%
    }else{
%>   
            <td valign="middle"><a href="javascript:document.forms[0].submit();" 
                class="topnav">Login</a></td>     
<%
    }
%>           
        </tr>
    </table>
    <form action="Login.jsp" method="post" id="frmLogout" target="_top">
        <input type="hidden" name="action" value="LOGOUT" >
    </form>
</body>
</html>