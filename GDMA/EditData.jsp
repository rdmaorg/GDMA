<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
    StringBuffer script = new StringBuffer();
    if("POST".equalsIgnoreCase(request.getMethod()))
    {
        String serverID = request.getParameter("server_id");
        String tableID = request.getParameter("table_id");
        String mode = request.getParameter("mode");
        long lngServerID = Long.parseLong(serverID);
        long lngTableID = Long.parseLong(tableID);
        ServerRegistrationFactory servFac = ServerRegistrationFactory.getInstance();
        ServerRegistration reg = servFac.getServerRegistration(lngServerID); 
        Table table = TableFactory.getInstance().getTable(lngTableID);   
        ArrayList columns = table.getIncludedColumns();
        if(mode != null && "UPDATE".equals(mode)){
            //do update here
            EditData edit = new EditData();
            try{
                edit.updateData(reg,table, request);
            }catch(Exception e){
                script.append("alert('An exception occurred while try to update the database.\\n");
                script.append(e.getMessage());
                script.append("');\n"); 
            }
        }
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Edit Data</title
    </head>
<body>
<form method="POST" action="EditData.jsp">
<table border="0" cellpadding="3" cellspacing="2">
    <tr>
        <td class="formHeader" colspan="2">Edit Data in Table - <%=table.getName()%></td>
    </tr> 
<%
    for(int i= 0; i < columns.size(); i++){
        Column column = (Column)columns.get(i);
%>       
    <tr>
        <td class="formLabel"><%=column.getName()%></td>
        <td class="formValue">
            <input class="formInput" type="text" 
                   name="new_<%=column.getName()%>" 
                   id="new_<%=column.getName()%>" 
                   value="<%=request.getParameter("new_"+ column.getName()) == null ?
                   request.getParameter("old_"+ column.getName()):request.getParameter("new_"+ column.getName())%>"
                   <%=column.isEditable()?"":"DISABLED"%>>
            <input type="hidden" 
                   name="old_<%=column.getName()%>" 
                   id="old_<%=column.getName()%>" 
                   value="<%=request.getParameter("old_"+ column.getName())%>">
        </td>
    <tr>
<%
    }
%>
        <tr>
            <td colspan="2" align="right"><input type="button" id="btnBack" 
                name="btnBack" value="Back" class="button" 
                onclick="window.location.href='ViewData.jsp?server_id=<%=serverID%>&table_id=<%=tableID%>&ts=<%=(new Date()).getTime()%>'">&nbsp;<input
                type="submit" id="btnSubmit" name="btnSubmit" value="Save" class="button"> </td>
        </tr> 
</table>            
<input type="hidden" name="server_id" id="server_id" value="<%=serverID%>">
<input type="hidden" name="mode" id="mode" value="UPDATE">
<input type="hidden" name="table_id" id="table_id" value="<%=tableID%>">
</form>
<script language="javascript">
    <%=script.toString()%>
</script>
</body>
</html>
<%
    }
%>

