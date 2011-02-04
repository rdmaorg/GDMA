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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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

<title>Generic Data Maintenance Application - Login</title>
</head>

<body>
<c:url value="/j_spring_security_check" var="jsck"/>

<table style="width: 100%; height:100%; font-family: Calibri;">
    <tr>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td colspan="4" align="center">
            <table style="width: 850px">
                <tr>
                    <td style="width: 848px; text-align: left;">
                        <img alt="" src="images/CustomerLogo.png" width="138" height="35" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="4" align="center">
            <table style="width: 850px">
                <tr>
                    <td style="width: 848px; text-align: left; font-size: x-large;"><strong>Generic Data Maintenance Application</strong></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="4"></td>
    </tr>
    <tr>
        <td colspan="4" align="center"  style="height: 500px; background-image: url('Networking_and_Security.jpg'); background-repeat:no-repeat; background-position:center;">
            <table style="width: 420px; border-left-style: none; border-left-width: 4px; border-top-width: 0px; border-bottom-style: solid; border-bottom-width: 4px; border-bottom-color:#000000;"   cellspacing="0" cellpadding="0">
                <tr>
                    <td colspan="9" style="background-color: #FFFFFF; height: 20px; border-top-style: solid; border-top-width: 3px; border-left-style: solid; border-left-width: 3px; border-right-style: solid; border-right-width: 3px;"></td>
                </tr>
                <tr>
                    <td rowspan="10" style="background-color: #FFFFFF; width: 20px; border-left-style: solid; border-left-width: 3px;"></td>
                    <td colspan="7" style="text-align: left; background-color: #FFFFFF; color: #333333; font-size: x-large;> ">
                        <strong>LOG IN</strong>
                    </td>
                    <td rowspan="10" style="background-color: #FFFFFF; width: 20px; border-right-style: solid; border-right-width: 3px;"></td>
                </tr>
                <tr>
                    <td colspan="7" style="text-align: left; background-color: #FFFFFF; color: #333333; font-size: medium;">
                        <strong>Log in to the Generic Data Maintenance Application</strong>
                    </td>
                </tr>
                <tr>
                    <td colspan="7" style="text-align: left; background-color: #FFFFFF"></td>
                </tr>
                <tr style="height:20px">
                    <!-- <td colspan="7" style="height: 20px; background-color: #CCCCCC"> -->
                    <td colspan="7" style="height: 20px; "><!--  background-color: #CCCCCC">-->
                        <form:form action="${fn:escapeXml(jsck)}" method="POST" name="loginForm" >
                            
                            <table style="height: 20px; width: 390px; background-color: #CCCCCC">
                                
                                    <tr>
                                        <td>
                                            <label for="j_username"><fmt:message key="loginform.username" /></label>
                                        </td>
                                        <td align="left">
                                            <input type="text" id="j_username" name="j_username" class="text"/>
                                        </td>
                                    </tr>
                                    <!--
                                    <input type="hidden" id="j_username" name="j_username" class="text"/>
                                    -->           
                                    <br />
`                                   <tr>
                                        <td>
                                            <label for="j_password"><fmt:message key="loginform.password" /></label>
                                        </td>
                                       <td align="left">
                                            <input type="password" name="j_password" class="text"/>
                                        </td>
                                    </tr>
                                    <br />                                
                                
                            </table>
                                <table style="height: 20px; width: 390px;">
                                    <!-- 
                                    <label for="domain"><fmt:message key="loginform.domain" /></label>
                                    <form:select path="domain">
                                    <form:options items="${domains}" />
                                    </form:select>
                                    <br />
                                    -->
                                    <tr>
                                        <td colspan="2"></td>
                                        <td style="width: 10px"></td>
                                        <td style="width: 14px" colspan="2"></td>
                                    </tr>
                                    <tr style="height:10px"><td colspan="7"></td></tr>
            
                                    <tr>
                                        <td align="right" style="background-color: #FFFFFF; height: 20px; text-align: right;" colspan="7">
                                            <div id="divLoginButtons" class="gdma-toolbar">
                                                <input id="btnReset" type="reset" name="btnReset" value="Reset" />
                                                <input id="btnSubmit" type="submit" name="btnSubmit" value="Login" /> 
                                            </div>
                                        </td>
                                    </tr>                                    
                                    <br />
                                </table>
                            
                            <br />
                            <c:if test="${not empty param.login_error}">
                                <span class="error">Login failed</span><br/>
                                <span class="error">${SPRING_SECURITY_LAST_EXCEPTION.message}</span>
                            </c:if>
                        </form:form>
                    </td>
                </tr>
                
                 <!-- <tr><td colspan="9" style="background-color: #FFFFFF; height: 20px; border-bottom-style: solid; border-bottom-width: 3px; border-right-style: solid; border-right-width: 3px"></td></tr> -->
            </table>   
        </td>
    </tr>
    <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td colspan="4" align="right"><span style="font-size: x-small;">(C) Customer 2010</span></td>
    </tr>
</table>

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