<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Edit Data</title
        <script language="javascript" src="js/EditData.js"></script>        
    </head>
<body>
<%
    StringBuffer script = new StringBuffer();
    String strMode = request.getParameter("mode");
    //mode indicates the form mode i.e. INSERT,UPDATE or DELETE
    
    if("POST".equalsIgnoreCase(request.getMethod()))
    {
        String serverID = request.getParameter("server_id");
        String tableID = request.getParameter("table_id");
        String mode = request.getParameter("mode");
        Long lngServerID = new Long(serverID);
        Long lngTableID = new Long(tableID);
        ServerRegistrationFactory servFac = ServerRegistrationFactory.getInstance();
        ServerRegistration reg = servFac.getServerRegistration(lngServerID); 
        Table table = TableFactory.getInstance().getTable(lngTableID);   
        ArrayList columns = table.getDisplayedColumns();
        if("SAVE".equals(request.getParameter("action"))){
            //do update here
            EditData edit = new EditData();
            try{
                edit.save(reg,table, request);
                script.append("window.location.href='ViewData.jsp?server_id="+serverID+"&table_id="+tableID+"&ts="+(new Date()).getTime()+"'" );

            }catch(Exception e){
                script.append("alert('An exception occurred while try to update the database.\\n");
                script.append(e.getMessage());
                script.append("');\n"); 
            }
        }
%>
<form method="POST" action="EditData.jsp">
<table border="0" cellpadding="3" cellspacing="2">
    <tr>
        <td class="formHeader" colspan="2">Edit Data in Table - <%=table.getName()%></td>
    </tr> 
<%
    Column column = null;
    String disbaled = null;
    for(int i= 0; i < columns.size(); i++){
        column = (Column)columns.get(i);
        if("INSERT".equals(strMode)){
           disbaled = column.isAllowInsert() ? "": "DISABLED"; 
        }
        else
        if("UPDATE".equals(strMode)){
            disbaled = column.isAllowUpdate()? "": "DISABLED"; 
        }
        else{
            disbaled = "DISABLED";
        }
%>       
    <tr>
        <td class="formLabel"><%=column.getName()%></td>
        <td class="formValue">
            <input class="formInput" type="text" 
                   name="new_<%=column.getName()%>" 
                   id="new_<%=column.getName()%>" 
                   value="<%=request.getParameter("new_"+ column.getName()) == null ?
                   request.getParameter("old_"+ column.getName()):request.getParameter("new_"+ column.getName())%>"
                   <%=disbaled%>>
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
                onclick="window.location.href='ViewData.jsp?server_id=<%=serverID%>&table_id=<%=tableID%>&ts=<%=(new Date()).getTime()%>'">&nbsp;
<%
        if("INSERT".equals(strMode) || "UPDATE".equals(strMode)){
%>           
                <input type="button" id="btnSubmit" name="btnSubmit" value="Save" class="button" onclick="doSave();">
<%      
        }
        else
        if("DELETE".equals(strMode)){
%>      
                <input type="button" id="btnDelete" name="btnDelete" value="Delete" class="button" onclick="doDelete();">
<%
        }
%>
            </td>
        </tr> 
     
</table>            
<input type="hidden" name="server_id" id="server_id" value="<%=serverID%>">
<input type="hidden" name="action" id="action" value="">
<input type="hidden" name="mode" id="mode" value="<%=strMode%>">
<input type="hidden" name="table_id" id="table_id" value="<%=tableID%>">
</form>
<%
    }
    else
    {
        script.append("alert('This page called incorrectly.');\n");
        script.append("history.go(-1);\n");
    }
%>   
<script language="javascript">
    <%=script.toString()%>
</script>
</body>
</html>


