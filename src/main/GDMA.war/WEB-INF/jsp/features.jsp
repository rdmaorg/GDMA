<?xml version="1.0" encoding="UTF-8"?>  
<%@ page import="java.util.Iterator" %>  
<%@ page import="ie.clients.gdma.util.FeaturesManager" %>
<%@ page import="ie.clients.gdma.util.AdminContactManager" %>

<script>
   var enabledFeatures = new Object;
<%
for(Iterator<String> i = FeaturesManager.getFeaturesManager().getEnabledFeatures(); i.hasNext();) {
	%>
	enabledFeatures['<%=i.next()%>'] = true;
	<%
}
%>

var adminContactDetails = new Array();
<%
for(int i = 0; i < 3; i++){
%>
    adminContactDetails[<%=i%>] = '<%=AdminContactManager.getAdminContactManager().getAdminContactDetails()[i]%>';
    
    
    <%
}
%>



</script>