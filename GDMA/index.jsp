<%@ page language="java" %>
<%
    if(session.getAttribute("USER") != null){
%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <LINK href="css/style.css" rel="stylesheet" type="text/css">
        <script language="javascript" src="js/index.js"></script>          
        <title>MIS - Generic Data Maintenance Application</title>
    </head>
    <frameset rows="40px,*" frameborder="1" name="frmTop" id="frmTop">
        <frame src="menu.jsp" name="top" scrolling='no' id="frmMenu" name="frmMenu">
        <frameset cols="25%,75%" name="frmContent" id="frmContent">
            <frame src="ServerBrowser.jsp" name="frmLeft" id="frmLeft" scrolling='auto'>
            <frame src="ViewData.jsp" name="main" scrolling='auto' id="main">
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