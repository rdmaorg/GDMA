<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.security.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
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
                title="Add or Edit the list of available servers"
                target="main" class="topnav">Servers</a></td>
            <td valign="middle"><a href="SQLConsoleFrame.jsp" 
                title="A basic SQL console"
                target="main" class="topnav">SQL Console</a></td> 

           <%-- SOCO 03/03/06 ADD LINK TO UserMaintenance.jsp --%>
            <td valign="middle"><a href="UserMaintenance.jsp" 
                title="User Maintenance Screen"
                target="main" class="topnav">User Maintenance</a></td> 
            <%-- END SOCO --%>
<%
    }
%>                            
            <td valign="middle"><a href="javascript:document.forms[0].submit();" 
                title="Log out of the application"
                class="topnav">Logout</a></td>            
<%
    }else{
%>   
            <td valign="middle"><a href="javascript:document.forms[0].submit();"
                title="Login to the application"
                class="topnav">Login</a></td>     
<%
    }
%>    
            <td valign="middle"><a href="help.jsp" 
                title="View the help page"
                class="topnav">Help</a></td> 
                   
            <td valign="middle"><div id="divLoading" name="divLoading" 
            style="display:none;"><img src="images/loading.gif"></div></td> 
        </tr>
    </table>
    <form action="Login.jsp" method="post" id="frmLogout" target="_top">
        <input type="hidden" name="action" value="LOGOUT" >
    </form>
    <iframe name="frmHidden" id="frmHidden" style="visibility:hidden"></iframe>
</body>
</html>