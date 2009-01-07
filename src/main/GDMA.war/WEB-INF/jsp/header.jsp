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
                <a href="${fn:escapeXml(link)}j_spring_security_logout" title="Log out of the application">Logout</a>
            </li>
            <li>
                <fmt:message key="link.help" var="helpLink"/>
                <a href="${fn:escapeXml(helpLink)}" title="View the help page" target="_blank"><fmt:message key="link.help.text"/></a>
            </li>
           	<li>
				<span>	
                	<b>Logged in User : </b><sec:authentication property="principal.firstName"/> <sec:authentication property="principal.lastName"/>
				</span>
            </li>
        </ul>
    </div>
    <div class="clear"></div>
</jsp:root>    