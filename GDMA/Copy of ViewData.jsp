<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.sql.*,
                 java.util.*,
                 com.vodafone.gdma.util.*"%>
<%
//TODO: Error checking
    if(session.getAttribute("USER") == null){
        %><jsp:forward page="NotLoggedIn.jsp"/><%
    }        
    ServerRegistration reg = null;
    Table table = null;
    String serverID = request.getParameter("server_id");
    String tableID = request.getParameter("table_id");
    String mode =  request.getParameter("mode");
    if(serverID != null && tableID != null){
        Long lngServerID = new Long(serverID);
        Long lngTableID = new Long(tableID);
        reg = ServerRegistrationFactory.getInstance().getServerRegistration(lngServerID); 
        table = TableFactory.getInstance().getTable(lngTableID);
    }   

%>                 
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Generic Data Maintenance Application - View Data</title>
        <script language="javascript" src="js/general.js"></script>
        <script language="javascript" src="js/ViewData.js"></script>
    </head>
<body class="nomargin nopadding">
<form action="EditData.jsp" method="post">
<table border="0" cellpadding="1" cellspacing="0" width="100%" height="100%">   
    <tr>
        <td width="100%"  class="toolBar">
            <table border="0" cellpadding="0" cellspacing="0">   
                <tr height="25px">
                    <td width="60px" >&nbsp;</td>
<%
    if(table !=null){
%>                         
                    <td class="greyTextButton" align="center" valign="middle"
                        onmouseover="this.className = 'greyTextButtonHover';"
                        onmouseout="this.className = 'greyTextButton';"
                        onclick="window.location.reload();">Refresh</td>                                        
                    </td>                    
                    <td class="greyTextButton" align="center" valign="middle"
                        onmouseover="this.className = 'greyTextButtonHover';"
                        onmouseout="this.className = 'greyTextButton';"
                        onclick="doInsert();">Insert</td>
                    <td class="greyTextButton" align="center" valign="middle"
                        onmouseover="this.className = 'greyTextButtonHover';"
                        onmouseout="this.className = 'greyTextButton';"
                        onclick="doEdit();">Update</td>
<%
        if(table.isAllowDelete()){
%>                          
                 <td class="greyTextButton" align="center" valign="middle"
                        onmouseover="this.className = 'greyTextButtonHover';"
                        onmouseout="this.className = 'greyTextButton';"
                        onclick="doDelete();">Delete</td>                                        
                    </td>
<%
        }
%>         
                    <td class="greyTextButton" align="center" valign="middle"
                        onmouseover="this.className = 'greyTextButtonHover';"
                        onmouseout="this.className = 'greyTextButton';"
                        onclick="doDownload('XML');">XML</td>
                    <td class="greyTextButton" align="center" valign="middle"
                        onmouseover="this.className = 'greyTextButtonHover';"
                        onmouseout="this.className = 'greyTextButton';"
                        onclick="doDownload('CSV');">CSV</td> 
<%
    }
%>                                                            
                </tr>
            </table>
        </td>
    </tr> 
    <tr>
        <td class="dataHolder" height="100%" valign="top">
    <table border="0" cellpadding="2" cellspacing="0" class="dataTable">                 
<%
    if(reg != null && table != null){
        ArrayList columns = table.getDisplayedColumns();

        Connection con = DBUtil.getConnection(reg);
        Statement stmt = con.createStatement();
        StringBuffer sbQuery = new StringBuffer();
        sbQuery.append("SELECT ");
        if(columns != null && columns.size() > 0)
	    {
	        for(int i = 0; i < columns.size(); i++){
	          sbQuery.append(((Column)columns.get(i)).getName());
	          sbQuery.append(i == columns.size() - 1 ? " ":", ");
	        }
	        sbQuery.append(" FROM  ");
	        sbQuery.append( table.getName());
	        sbQuery.append(" ORDER BY  ");	
            for(int i = 0; i < columns.size(); i++){
              sbQuery.append(((Column)columns.get(i)).getName());
              sbQuery.append(i == columns.size() - 1 ? " ":", ");
            }	        
	    }else{
	        sbQuery.append("'No columns selected for this table'");
	    }
System.out.println(sbQuery);	    
	    ResultSet rs = stmt.executeQuery(sbQuery.toString());
	    ResultSetMetaData rsmd = rs.getMetaData();
	    if(rs != null)
	    { 
%>
    <tr id="trHeader">
        <td class="dataHeader" width="30px" nowrap>&nbsp;&nbsp;&nbsp;</td>                        
<%              
            for(int i = 1; i <= rsmd.getColumnCount(); i++){
%>
        <td class="dataHeader" nowrap ><%=rsmd.getColumnName(i)%>&nbsp;<input
            type="hidden" id="old_<%=rsmd.getColumnName(i)%>"
            name="old_<%=rsmd.getColumnName(i)%>"></td>                    
<%
            }
%>
    </tr>
<%
            for(int row = 1; rs.next(); row++){
%>
    <tr onmouseover="mouseEntered(this)" onmouseout="mouseExited(this)" 
        onclick="mouseClicked(this,<%=row%>);"
        ondblclick="mouseDblClicked(this,<%=row%>);" 
        id="trRow<%=row%>" class="dataBody">
        <td class="dataHeader" align="left" style=" font-weight: normal"><%=row%></td>      
<%
	            String value;
		        for(int i = 1; i <= rsmd.getColumnCount(); i++){ 
			        // we need a hidden field for each column  
			        if(DBUtil.isDate(rsmd.getColumnType(i))){
			           Timestamp date = rs.getTimestamp(i);
			           if(date == null){
%>
        <td class="dataGreyBorder" nowrap></td>
<%		           
			           }else{
%>
        <td class="dataGreyBorder" nowrap><%=Formatter.formatDate(date)%><input
                type="hidden" value="<%=date.getTime()%>"></td>
<%		           
	                    }
			        }else{
			          value =  rs.getString(i);                
%>
        <td class="dataGreyBorder" nowrap><%=value==null?"":value%></td>
<%
                    }
                }
%>
    </tr>
<%
            }
	       }  
		    rs.close();
		    stmt.close();
		    con.close();  
	    }          
%>  
</table>
</td>
</tr>
</table>
<input type="hidden" id="mode" name="mode" value="">
<input type="hidden" id="server_id" name="server_id" value="<%=serverID%>">
<input type="hidden" id="table_id" name="table_id" value="<%=tableID%>">
</form>
</body>
</html>