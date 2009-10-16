<?xml version="1.0" encoding="UTF-8"?>
<%@ page import="java.util.Iterator" %>
<%@ page import="ie.clients.gdma.util.FeaturesManager" %>

<script>
   var enabledFeatures = new Object;
<%
for(Iterator<String> i = FeaturesManager.getFeaturesManager().getEnabledFeatures(); i.hasNext();) {
	%>
	enabledFeatures['<%=i.next()%>'] = true;
	<%
}
%>
</script>