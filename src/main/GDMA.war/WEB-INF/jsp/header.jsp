<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:fn="http://java.sun.com/jsp/jstl/functions"
          xmlns:sec="http://www.springframework.org/security/tags"
          xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
          xmlns:c="http://java.sun.com/jsp/jstl/core" 
          version="2.0">           
    <jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
    <c:url value="/" var="link"/>


    <div id="divInnerHeader">
        <ul>
			<sec:authorize ifAllGranted="ROLE_ADMIN">
            <li>
                <a href="${fn:escapeXml(link)}admin/admin.htm" title="GDMA Admin">Admin</a>
            </li>
            <li>
                <a href="${fn:escapeXml(link)}index.htm" title="The main application">GDMA</a>
            </li>
            <li>
                <a href="${fn:escapeXml(link)}admin/sql.htm" title="A basic SQL console">SQL Console</a>
            </li>
            </sec:authorize>
            <li>
                <fmt:message key="link.help" var="helpLink"/>
                <a href="${fn:escapeXml(helpLink)}" title="View the help page" target="_blank"><fmt:message key="link.help.text"/></a>
            </li>			
            <li >
                <img src="/GDMA/images/blank.gif" width="65%" style="display:block" />
            </li>
            <li stlye="">
                <a style="border-left: 1px #FFFFFF solid;border-right: 0px #FFFFFF solid;" href="${fn:escapeXml(link)}j_spring_security_logout" title="Log out of the application">Logout</a>
            </li>
        </ul>
    </div>
		<div style="float:left;z-index:0;"><img alt="GDMA" src="/GDMA/images/gdma_logo.png"  width="162" height="60"/></div>
<div style="float:right;z-index:0;"></div>

</jsp:root>    