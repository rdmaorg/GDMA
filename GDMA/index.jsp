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
        <LINK href="css/style.css" rel="stylesheet" type="text/css">
        <script language="javascript" src="js/index.js"></script>          
        <title>Generic Data Maintenance Application</title>
    </head>
    <frameset rows="40px,*" frameborder="1">
        <frame src="menu.jsp" name="top" scrolling='no'>
        <frameset cols="25%,75%" name="frmContent" id="frmContent">
            <frame src="ServerBrowser.jsp" name="frmLeft" id="frmLeft" scrolling='auto'>
            <frame src="ViewData.jsp" name="main" scrolling='auto'>
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