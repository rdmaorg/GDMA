<%@ page language="java" %>
<%
    if(session.getAttribute("USER") != null){
%>  
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Generic Data Maintenance Application</title>
    </head>
<body class="nopadding nomargin"> 
<table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%">
  <tr>
    <td colspan="2" width="100%"><iframe width="100%" name="top" src="menu.jsp" height="40px"></td>
  </tr>
  <tr height="100%">
    <td width="25%"><iframe width="25%" name="left" src="ServerBrowser.jsp" height="100%"></td>
    <td width="75%"><iframe width="75%" name="main" src="ViewData.jsp" height="100%"></tr>
  </tr>
</body>        
</html>
<%
    }else{
%>   
<jsp:forward page="Login.jsp" />   
<%
    }
%> 