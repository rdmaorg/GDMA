<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.*,
                 com.vodafone.gdma.dbaccess.*,
                 java.util.ArrayList"%>
<%
System.out.println("1");
    ServerRegistrationFactory obj = ServerRegistrationFactory.getInstance();    
    System.out.println("1");
    ArrayList servers = obj.getServerRegistrations();
    System.out.println("1");
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "<%=Config.getProperty("app_path")%>/css/style.css";
        </style>
        <title>List Connections</title>
    </head>
<body bgcolor="#FFFFFF">
<table>
<%
System.out.println("1");
    for(int i = 0; i < servers.size() ; i++){
%>
  <tr>
    <td><%=(String)servers.get(i)%></td>
  </tr>
<%        
    }
%>
</table>
</body>
</html>