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
    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
	<link rel="icon" type="image/png" href="images/favicon.png" />
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

<title>Generic Data Maintenance Application</title>
</head>

<body>
<c:url value="/j_spring_security_check" var="jsck"/>

<table width="100%">
<tr>
<td  colspan="2"><img src="images/blank.gif" width="995" height="50" style="display:block" /></td>
</tr>
<tr><td width="100%" align="center">
	<table style="width: 995; height:60%; font: 12px/1.3em Arial,Tahoma,Helvetica,sans-serif;">
	<tr>
	<td style="align: left; font-size: x-large;" align="left"><img alt="GDMA" src="images/gdma_logo.png"  width="162" height="60"/></td>
	<td style="height:20%" align="right"></td>
	</tr>
	<tr>
<td  colspan="2"><img src="images/blank.gif"  height="10" style="display:block" /></td>
</tr>
	<tr>
	<td width="100%" align="center" valign="middle"  colspan="2">
		<div >
			<img src="images/header_bg.png" alt="GDMA"  style="display:block" />
		</div>
		<div style="position:absolute;top:150px;left:56%;overflow:hidden;" >
			

			<fieldset style="border:0px;">
			<form:form action="${fn:escapeXml(jsck)}" method="POST" name="loginForm" >
			<table >
									<tr>
										<td  colspan="2" align="left"><h4>Login</h4></td>
									</tr>
									<tr>
										<td  colspan="2"><img src="images/blank.gif" height="5" style="display:block" /></td>
									</tr>
                                
                                    <tr>
                                        <td>
                                            <label for="j_username"><fmt:message key="loginform.username" /></label>
                                        </td>
                                        <td align="left">
                                            <input type="text" id="j_username" name="j_username" class="text" style="width:150px"/>
                                        </td>
                                    </tr>

                                   <tr>
                                        <td>
                                            <label for="j_password"><fmt:message key="loginform.password" /></label>
                                        </td>
                                       <td align="left">
                                            <input type="password" name="j_password" class="text" style="width:150px"/>
                                        </td>
                                    </tr>
										<tr>
											<td  colspan="2"><img src="images/blank.gif" height="5" style="display:block" /></td>
										</tr>
									<tr>
                                        <td align="left">
                                            <input  type="reset"  value="Reset" />  
                                        </td>
                                       <td align="right">
                                            <input  type="submit"  value="Login" />
                                        </td>
                                    </tr>

                            </table>
	                       
                        </form:form>
			</fieldset>
		</div>

	</td>
	</tr>
	<tr>
<td  colspan="2"><img src="images/blank.gif" height="20" style="display:block" /></td>
</tr>
<tr>
<td style="height:20%"  align="right" colspan="2"> <span style="font-size: x-small;"></span></td>
</tr>
	</table>
</td>
</tr>

</table>

    <script type="text/javascript">
   			<c:if test="${not empty param.login_error}">
				alert('${SPRING_SECURITY_LAST_EXCEPTION.message}');
			</c:if>

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
	/*
        YAHOO.util.Event.onDOMReady(function() {
            
            var btnReset = new YAHOO.widget.Button( "btnReset");
            var btnSubmit = new YAHOO.widget.Button( "btnSubmit");
            //btnSubmit.on("click", logonSubmit);

        });
		*/
    </script>

</body>

</html>
</jsp:root>