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
    <c:set var="jsType" scope="page" value="min"/>

    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}reset-fonts-grids.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}resize.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}layout.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}logger.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}container.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}button.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}calendar.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}treeview.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}fonts-min.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}datatable.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}datatable-skin.css" />
    <link rel="stylesheet" type="text/css" href="${fn:escapeXml(csslink)}simpleeditor.css" />
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

    <script type="text/javascript" src="${fn:escapeXml(jslink)}date.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/yahoo-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/event-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/dom-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/logger-${jsType}.js" >&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/utilities.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/container-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/element-beta-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/dragdrop-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/resize-beta-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/button-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/menu-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/treeview-${jsType}.js" >&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/datasource-beta-${jsType}.js" >&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/datatable-beta-${jsType}.js" >&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/animation-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/calendar-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/layout-beta-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/json-${jsType}.js">&#xA0;</script>

    <script type="text/javascript" src="${fn:escapeXml(jslink)}dialogs-${jsType}.js" >&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}utilities-${jsType}.js" >&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}gdma-datatable-${jsType}.js" >&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}dwr/interface/GdmaAdmin.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}dwr/interface/GdmaAjax.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}dwr/engine.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}dwrds-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}toolbar-${jsType}.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}layout-${jsType}.js">&#xA0;</script>

    <title>Generic Data Maintenance Application - <tiles:getAsString name="title"/></title>
</head>
<body id="body" class="yui-skin-sam">
    <div id="divLoading"></div>
    <div id="top1">
        <tiles:insertAttribute name="header" />
    </div>
    <div id="left1">
        <tiles:insertAttribute name="left" />
     </div>
     <div id="center1">
        <tiles:insertAttribute name="right" />
     </div>
     <div id="footer1">
        <tiles:insertAttribute name="footer" />
     </div>
</body>
</html>
</jsp:root>

