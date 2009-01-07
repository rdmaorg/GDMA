<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:fn="http://java.sun.com/jsp/jstl/functions"
          xmlns:c="http://java.sun.com/jsp/jstl/core" 
          xmlns:tiles="http://tiles.apache.org/tags-tiles"
          version="2.0">           
    <jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>

    <div id="divToolbar" class="yui-skin-sam gdma-toolbar">&#xA0;</div>
    <div id="divDataTable" class="yui-skin-sam">&#xA0;</div>
    <div id="divPager" class="yui-skin-sam">&#xA0;</div>

	<c:url value="/" var="root"/>
    <c:url value="/js/" var="jslink"/>
	<script type="text/javascript">
		var GDMA_CONTEXT_ROOT = "${fn:escapeXml(root)}";
	</script>
    <script type="text/javascript" src="${fn:escapeXml(jslink)}datagrid-min.js">&#xA0;</script>
    <script type="text/javascript">
        // var myLogReader = new YAHOO.widget.LogReader();
    </script>
</jsp:root>


