<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
//TODO: Error checking
    if(session.getAttribute("USER") == null){
        %><jsp:forward page="NotLoggedIn.jsp"/><%
    }        
    ServerRegistration reg = null;
    Table table = null;
    Hashtable htSearch = new Hashtable();;
    String serverID = request.getParameter("server_id");
    String tableID = request.getParameter("table_id");
    String strMode = request.getParameter("mode");
    Long lngServerID = null;
    Long lngTableID = null;
    if(serverID != null && tableID != null){
        lngServerID = new Long(serverID);
        lngTableID = new Long(tableID);
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
<form action="EditData.jsp" method="post" id="frmMain">        
<table border="0" cellpadding="1" cellspacing="0" width="100%" height="100%">   
    <tr>
        <td width="100%"  class="toolBar">
            <table border="0" cellpadding="0" cellspacing="0">   
                <tr height="25px">
                    <td width="5px" >&nbsp;</td>
<%
    if(table !=null){
%>                     
                    <td class="formLabel" align="left" valign="middle" 
                        nowrap><%=reg.getName()%> - <%=table.getName()%> | </td> 
                    <td><a onclick="doAllRecords();" href="#" class="greyTextButton">Show All<a></td>
                    <td><a onclick="doToggleSearch();" href="#" class="greyTextButton">Search<a></td>                        
<%
        if(table.isAllowInsert()){
%>                                                                   
                    <td><a onclick="doInsert();" href="#" class="greyTextButton">Insert<a></td>
<%
        }
        if("ALL".equals(strMode) || "SEARCH".equals(strMode) ){
            if(table.isAllowUpdate()){
%>    
                    <td><a onclick="doEdit();" href="#" class="greyTextButton">Update<a></td>
<%
            }
            if(table.isAllowDelete()){
%>                          
                    <td><a onclick="doDelete();" href="#" class="greyTextButton">Delete<a></td>
<%
            }
%>         

                    <td><a onclick="doDownload('export.xml');" href="#" class="greyTextButton">XML<a></td>
                    <td><a onclick="doDownload('export.csv');" href="#" class="greyTextButton">CSV<a></td>
<%
        }
    }
%>                                                            
                </tr>
            </table>
        </td>
    </tr> 
    <tr style="display:none" id="trSearch">
        <td width="100%"  class="searchForm">
            <table border="0" cellpadding="2" cellspacing="2">  
                <tr height="25px">
                    <td colspan="3" align="left" valign="top" class="formHeader">Search Form</td>                                        
                </tr>            
<%
    if(table !=null){
        ArrayList columns = table.getDisplayedColumns();
        for(int i = 0; i < columns.size(); i++){
            Column column = (Column)columns.get(i);
            String value = (String)htSearch.get(column.getName());
%> 
                <tr height="25px">
                    <td align="left" valign="top" class="formLabel" width="20%">&nbsp;</td>                                                        
                    <td align="left" valign="top" class="formLabel"><%=column.getName()%></td>                                        
                    <td class="formInput"><input type="text" 
                        value="<%=request.getParameter("old_" + column.getName())==null?"":request.getParameter("old_" + column.getName())%>" 
                        id="old_<%=column.getName()%>" name="old_<%=column.getName()%>"></td>                                                          
                </tr>
<%
        }
%>  
                <tr>
                    <td align="right" colspan="3" >
                        <input type="button" class="button" value="Reset" onclick="clearSearch();">&nbsp;
                        <input type="button" class="button" id="btnSearch" name="btnSearch" 
                            value="Search" onclick='doSearch()'>
                    </td>
                </tr>
<%        
    }
%>                  
            </table>
        </td>
    </tr>     
    <tr>
        <td class="dataHolder" height="100%" valign="top">
<%
    if(reg != null && table != null && 
        ("ALL".equals(strMode) || "SEARCH".equals(strMode) ) ){
        EditData ed = new EditData();
        try{
            out.print(ed.select(reg,table,request,"HTML"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }          
%>  
            <input type="hidden" id="mode" name="mode" value="">
            <input type="hidden" id="server_id" name="server_id" value="<%=serverID%>">
            <input type="hidden" id="table_id" name="table_id" value="<%=tableID%>">
        </td>
    </tr>
</table>
</form> 
</body>
</html>