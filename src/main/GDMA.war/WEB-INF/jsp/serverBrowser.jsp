<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:fn="http://java.sun.com/jsp/jstl/functions"
          xmlns:c="http://java.sun.com/jsp/jstl/core" 
          version="2.0">           
    <jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
    
    <div id="divServerBrowser">&#xA0;</div>
    <c:url value="/js/" var="jslink"/>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}serverbrowser.js">&#xA0;</script>
</jsp:root>
