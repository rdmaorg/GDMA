<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.sql.Statement,
                 java.sql.ResultSet,
                 java.util.ArrayList"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>View Data</title>
        <script language="javascript" src="js/general.js"></script>
        <script language="javascript" src="js/ViewData.js"></script>
    </head>
<body class="nomargin nopadding" style="background-color: #D4D0C8">
<form action="EditData.jsp" method="post">
<table border="0" cellpadding="2" cellspacing="0" class="dataTable">                 
<%

    String serverID = request.getParameter("server_id");
    String tableID = request.getParameter("table_id");
    if(serverID != null){
        ServerRegistration reg = ServerRegistrationFactory
                                          .getInstance()
                                          .getServerRegistration(Long.parseLong(serverID));
        if(reg !=  null){
            if(tableID != null){
            //give the contents of the table
                Table table = TableFactory.getInstance().getTable(Long.parseLong(tableID));
                ArrayList columns = table.getIncludedColumns();

                ODBCProvider odbc = ODBCProviderFactory.getInstance().getODBCProvider(
                reg.getOdbcTypeID());
		        java.sql.Connection con = Connection.getConnection(
		                            odbc.getConnectionClass(), 
		                            reg.getUsername(), 
		                            reg.getPassword(), 
		                            reg.getConnectionURL());
		        Statement stmt = con.createStatement();
		        StringBuffer sbQuery = new StringBuffer();
		        sbQuery.append("SELECT ");
		        if(columns != null && columns.size() > 0)
		        {
			        for(int i = 0; i < columns.size(); i++){
			          sbQuery.append(((Column)columns.get(i)).getName());
			          sbQuery.append(i == columns.size() - 1 ? " ":", ");
			        }
			        sbQuery.append("FROM  ");
	                sbQuery.append( table.getName());	
                }else{
                    sbQuery.append("'No columns selected for this table'");
                }
		        ResultSet rs = stmt.executeQuery(sbQuery.toString());
                int idx = 1;
                if(rs != null)
                { 
%>
    <tr id="trHeader">
        <td class="dataHeader" width="30px" nowrap>&nbsp;&nbsp;&nbsp;</td>                        
<%              
                    for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
%>
        <td class="dataHeader" nowrap ><%=rs.getMetaData().getColumnName(i)%>&nbsp;<input
            type="hidden" id="old_<%=rs.getMetaData().getColumnName(i)%>"
            name="old_<%=rs.getMetaData().getColumnName(i)%>"></td>                    
<%
                    }
%>
    </tr>
<%                  
                    while(rs.next()){
                       idx++;
%>
    <tr onmouseover="mouseEntered(this)" onmouseout="mouseExited(this)" 
        onclick="mouseClicked(this,<%=idx%>);"
        ondblclick="mouseDblClicked(this,<%=idx%>);" 
        id="trRow<%=idx%>" class="dataBody">
        <td class="dataHeader" align="left" style=" font-weight: normal"><%=idx%></td>      
<%
                        String value;
                        for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){ 
                        // we need a hidden field for each column  
                        value =  rs.getString(i);                
%>
        <td class="dataGreyBorder" nowrap><%=value==null?"":value%></td>
<%
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
        }
    }
%>  
</table>
<input type="hidden" id="server_id" name="server_id" value="<%=serverID%>">
<input type="hidden" id="table_id" name="table_id" value="<%=tableID%>">
</form>
</body>
</html>