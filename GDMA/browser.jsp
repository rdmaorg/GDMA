<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.*,java.util.ArrayList"%>
<%
    ServerRegistrationFactory obj = ServerRegistrationFactory.getInstance();    
    ArrayList servers = obj.getServerRegistrations();
    String imagesPath = "images/";
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
        <link rel="stylesheet" href="<%=Config.getProperty("app_path")%>/css/style.css">
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
<body bgcolor="#FFFFFF">
<table border="0" cellpadding="0" cellspacing="0">
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
                        class="aServer"><%=((ServerRegistration)servers.get(i)).getName()%></a></td>
            </tr>
            <tr width="100%">
                <td colspan="3">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" 
                           style="display:none" id="trTables<%=i%>" >
<%
    ServerRegistration reg = (ServerRegistration)servers.get(i);
    if(reg != null){ 
        ArrayList tables = obj.getTableList(reg) ;
        for(int j = 0; j < tables.size() ; j++){
%>                      
                        <tr>
                            <td width="20px" <%=(i==servers.size() - 1?"":"class=\"dotted\"")%>><img 
                                width="20px" border="0" src="<%=imagesPath%>spacer.gif"></td>
                            <td width="20px" <%=(j==tables.size() - 1?"":"class=\"dotted\"")%> valign="top"><img 
                                width="20px" border="0" src="<%=imagesPath%>plus.gif"></td>                        
                            <td><a href="view.jsp?server=<%=((ServerRegistration)servers.get(i)).getName()%>&table=<%=(String)tables.get(j)%>" 
                                target="main" class="aServer"><%=(String)tables.get(j)%></a></td>
                        </tr>
<%
        }
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