<%@ page language="java" %> 
<%@ page import="com.vodafone.gdma.dbaccess.*,
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
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
<html>
<head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <script language="javascript" src="js/index.js"></script>                  
		<title>Generic Data Maintenance Application - Server Browser</title>
		<script language="javascript">
		    function toggleDisplay(id){
		        if( document.getElementById('tr'+id )){
		            if( document.getElementById('tr'+id).style.display == 'none'){
		                document.getElementById('tr'+id).style.display = 'block';
		                if( document.getElementById('img'+id )){
		                    document.getElementById('img'+id).src = 'images/minus.gif';
		                }
		            }else{
		                document.getElementById('tr'+id).style.display = 'none';
		                if( document.getElementById('img'+id )){
		                    document.getElementById('img'+id).src = 'images/plus.gif';
		                }                
		            }
		        }
		    }  
		</script>
    </head>
<body class="nomargin nopadding ">
<table border="0" cellpadding="0" cellspacing="0" width="100%" id="tblMain">
    <tr>
        <td width="100%"  class="toolBar">
            <table border="0" cellpadding="0" cellspacing="0" width="100">   
                <tr height="27px">
                    <td width="5px" >&nbsp;</td>
                    <td><a href="ServerBrowser.jsp?ts=<%=(new Date()).getTime()%>" class="greyTextButton">Refresh<a></td>
                    <td><a onclick="doHideLeftFrame();" href="#" class="greyTextButton">Hide<a></td>
                </tr>
            </table>
       </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
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
                        style="cursor:pointer"><img 
                        width="20px" border="0" id="imgTables<%=i%>"
                        src="images/plus.gif" style="cursor:pointer"></a></td>
                <td width="20px"><img width="20px" border="0"
                        src="images/db.gif"></td>
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
                                width="20px" border="0" src="images/spacer.gif"></td>
                            <td width="20px" <%=(j==tables.size() - 1?"":"class=\"dotted\"")%> valign="top"><img 
                                width="20px" border="0" src="images/dotted_corner.gif"></td>   
                            <td width="20px"><img width="20px" border="0"
                                src="images/table.gif"></td>                                                     
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
                        src="images/spacer.gif"></td>
            </tr>            
        </table>
    </td>
  </tr>
<%        
        }
    }
%>

</table>
<table border="0" style="display:none" cellpadding="0" 
       cellspacing="0" width="100%" height="100%" id="tblHidden"
       class="toolBar">
  <tr>
    <td onclick="doHideLeftFrame()" 
        onmouseover="this.style.cursor = 'pointer';"
        onmouseout="this.style.cursor = 'default';"
        width="100%" height="100%">&gt;</td>
  </tr>
</table>
</body>
</html>