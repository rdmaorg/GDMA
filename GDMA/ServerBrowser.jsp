<%@ page language="java" %> 
<%@ page import="com.vodafone.gdma.*,
                 com.vodafone.gdma.dbaccess.*,
                 java.util.ArrayList,
                 java.util.Date"%>
<%
    ServerRegistration reg = null; 
    Table table = null;
    Column column = null;
    ArrayList servers;
    ArrayList tables;
    ArrayList columns;  
    
    servers = ServerRegistrationFactory.getInstance().getList();     
    String imagesPath = "images/";
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
        <style type="text/css" media="all">
            @import "<%=Config.getProperty("app_path")%>/css/style.css";
        </style>
		<title>List Connections</title>
		<script language="javascript">
		    function toggleDisplay(id){
		        if( document.getElementById('tr'+id )){
		            if( document.getElementById('tr'+id).style.display == 'none'){
		                document.getElementById('tr'+id).style.display = 'block';
		                if( document.getElementById('img'+id )){
		                    document.getElementById('img'+id).src = '<%=imagesPath%>minus.gif';
		                }
		            }else{
		                document.getElementById('tr'+id).style.display = 'none';
		                if( document.getElementById('img'+id )){
		                    document.getElementById('img'+id).src = '<%=imagesPath%>plus.gif';
		                }                
		            }
		        }
		    }  
		</script>
    </head>
<body >
<table border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align="left"><a href="ServerBrowser.jsp?ts=<%=(new Date()).getTime()%>" class="aServer">Refresh</a><br><br></td>
    </tr>
<%
    if(servers.size() == 0){
%>
    
    <tr>
        <td>No Server Registrations Found</td>
    </tr>
    
<%    
    }
    else
    {
      
        for(int i = 0; i < servers.size() ; i++){
            reg = (ServerRegistration)servers.get(i);
%>
  <tr>
    <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr width="100%">
                <td width="20px" valign="top"><a href="javascript:toggleDisplay('Tables<%=i%>');" 
                        class="aServer"><img 
                        width="20px" border="0" id="imgTables<%=i%>"
                        src="<%=imagesPath%>plus.gif"></a></td>
                <td width="20px"><img width="20px" border="0"
                        src="<%=imagesPath%>db.gif"></td>
                <td align="left" width="100%" valign="bottom"><a 
                        href="javascript:toggleDisplay('Tables<%=i%>');" 
                        class="aServer"><%=reg.getName()%></a></td>
            </tr>
            <tr width="100%">
                <td colspan="3">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" 
                           style="display:none" id="trTables<%=i%>" >
<%
            
            tables = reg.getDisplayedTables();
            for(int j = 0; j < tables.size() ; j++){
                table = (Table)tables.get(j);
%>                      
                        <tr>
                            <td width="20px" <%=(i==servers.size() - 1?"":"class=\"dotted\"")%>><img 
                                width="20px" border="0" src="<%=imagesPath%>spacer.gif"></td>
                            <td width="20px" <%=(j==tables.size() - 1?"":"class=\"dotted\"")%> valign="top"><img 
                                width="20px" border="0" src="<%=imagesPath%>dotted_corner.gif"></td>   
                            <td width="20px"><img width="20px" border="0"
                                src="<%=imagesPath%>table.gif"></td>                                                     
                            <td><a href="ViewData.jsp?server_id=<%=reg.getId()%>&table_id=<%=table.getId()%>" 
                                target="main" class="aServer"><%=table.getName()%></a></td>
                        </tr>
<%
            }
%>                        
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="3" <%=(i==servers.size() - 1?"":"class=\"dotted\"")%>><img height="10px" border="0"
                        src="<%=imagesPath%>spacer.gif"></td>
            </tr>            
        </table>
    </td>
  </tr>
<%        
        }
    }
%>

</table>
</body>
</html>