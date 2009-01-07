<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:form="http://www.springframework.org/tags/form"
          xmlns:fn="http://java.sun.com/jsp/jstl/functions"
          xmlns:c="http://java.sun.com/jsp/jstl/core" 
          xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
          xmlns:spring="http://www.springframework.org/tags"
          xmlns:sec="http://www.springframework.org/security/tags"
          xmlns:tiles="http://tiles.apache.org/tags-tiles"
          version="2.0">           
    <jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>

    <jsp:output doctype-root-element="html" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
        doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" />

<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xml:lang="En" >
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />  
    <c:url value="/css/" var="csslink"/>
    <c:url value="/js/" var="jslink"/>
    
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}fonts-min.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}container.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}button.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}gdma-yui.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}gdma.css" />
    <![CDATA[
    <!--[if IE 6 ]>
        <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}gdma-ie6.css" />
    <![endif]-->
    <!--[if gt IE 6 ]>
        <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}gdma-ie7.css" />
    <![endif]-->
    ]]>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/yahoo-min.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/event-min.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/dom-min.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/element-beta-min.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/container-min.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/button-min.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}toolbar.js">&#xA0;</script>

    <title>Generic Data Maintenance Application - Logon</title>
    </head>
<body id="body" class="yui-skin-sam">
	<c:url value="/j_spring_security_check" var="jsck"/>
    <h1 class="logon"><fmt:message key="loginform.header" /></h1>
    <form:form action="${fn:escapeXml(jsck)}" method="POST" name="loginForm" cssClass="loginform" >
        <fieldset>
            <label for="j_username"><fmt:message key="loginform.username" /></label>
            <input type="text" id="j_username" name="j_username" class="text"/>
<!--
            <input type="hidden" id="j_username" name="j_username" class="text"/>
-->            
            <br />

            <label for="j_password"><fmt:message key="loginform.password" /></label>
            <input type="password" name="j_password" class="text"/>
            <br />
<!-- 
            <label for="domain"><fmt:message key="loginform.domain" /></label>
            <form:select path="domain">
                <form:options items="${domains}" />
            </form:select>
            <br />
 -->
            <div id="divLoginButtons" class="gdma-toolbar">
                <input id="btnReset" type="reset" name="btnReset" value="Reset" />
                <input id="btnSubmit" type="submit" name="btnSubmit" value="Login" /> 
            </div>
            <br />

        </fieldset>
		<br />
        <c:if test="${not empty param.login_error}">
        <span class="error">Login failed</span><br/>
        <span class="error">${SPRING_SECURITY_LAST_EXCEPTION.message}</span>
        </c:if>
    </form:form>

    <script type="text/javascript">
    
    logonSubmit = function(oArgs){
        var form = YAHOO.util.Dom.get("command");
        var j_username = YAHOO.util.Dom.get("j_username");
        var username = YAHOO.util.Dom.get("username");
        var domain = YAHOO.util.Dom.get("domain");
        if(domain.selectedIndex > -1){
            j_username.value = domain.value  + "\\" + username.value;
        }else{
            j_username.value = username.value;
        }
        form.submit();
    };
        YAHOO.util.Event.onDOMReady(function() {
            
            var btnReset = new YAHOO.widget.Button( "btnReset");
            var btnSubmit = new YAHOO.widget.Button( "btnSubmit");
            //btnSubmit.on("click", logonSubmit);

        });
    </script>
</body>
</html>
</jsp:root>