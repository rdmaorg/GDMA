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
    <frameset rows="40px,*" frameborder="1">
        <frame src="menu.jsp" name="top" SCROLLING='NO'>
        <frameset cols="25%,75%">
            <frame src="ServerBrowser.jsp" name="left">
            <frame src="ViewData.jsp" name="main">
        </frameset>        
    </frameset>
</html>
<%
    }else{
%>   
<jsp:forward page="Login.jsp" />   
<%
    }
%> 