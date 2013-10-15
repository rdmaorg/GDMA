<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:fn="http://java.sun.com/jsp/jstl/functions"
          xmlns:c="http://java.sun.com/jsp/jstl/core" 
          version="2.0">           
    <jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>

    <div id="divSQlTop" class="yui-skin-sam gdma-toolbar">
        <div id="divToolbar" class="yui-skin-sam gdma-toolbar"><select id="selServers" name="selServers"><option>Please Select</option></select></div>
        <div id="divSQlText" class="yui-skin-sam gdma-toolbar">
            <textarea id="txtSqlEditor" name="txtSqlEditor" rows="5" cols="75" style="width:99%" >&#xA0;</textarea>
        </div>
    </div>
    <div id="divServerTable" class="yui-skin-sam">&#xA0;</div>
    <div id="divPager" class="yui-skin-sam">&#xA0;</div>

     <c:url value="/js/" var="jslink"/>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}yui/simpleeditor-beta-min.js">&#xA0;</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}admin/sql.js">&#xA0;</script>

    <script type="text/javascript">
        // var myLogReader = new YAHOO.widget.LogReader();
    </script>
</jsp:root>


