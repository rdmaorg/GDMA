<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
    String serverID = request.getParameter("server_id");
    String tableID = request.getParameter("table_id");
    Long lngServerID = new Long(serverID);
    Long lngTableID = new Long(tableID);
    ServerRegistrationFactory servFac = ServerRegistrationFactory.getInstance();
    ServerRegistration reg = servFac.getServerRegistration(lngServerID); 
    ColumnFactory colFac = ColumnFactory.getInstance();
    Table table = TableFactory.getInstance().getTable(lngTableID);   
    ArrayList columnDB = servFac.getColumnsFromDB(reg, table.getName());
    Column column = null;

    if("POST".equalsIgnoreCase(request.getMethod()) && "SAVE_COLUMNS".equals(request.getParameter("mode"))){
        colFac.hideAll(lngTableID);
        for(int i = 0; request.getParameter("fldName" +i) != null; i++){
            column = new Column();
            column.setId(request.getParameter("fldID" + i));
            column.setTableID(lngTableID);
            column.setName(request.getParameter("fldName" + i));            
            column.setColumnType(request.getParameter("fldColumnType" + i));
            column.setColumnTypeString(request.getParameter("fldColumnTypeString" + i));   
            
            if(request.getParameter("fldAllowLookup" + i) != null)
            {                     
                column.setDropDownColumnDisplay(request.getParameter("fldDropDownColumnDisplayID" + i));
                column.setDropDownColumnStore(request.getParameter("fldDropDownColumnStoreID" + i)); 
            }else{
                column.setDropDownColumnDisplay((Long)null);
                column.setDropDownColumnStore((Long)null);
            }
            column.setDisplayed(request.getParameter("fldDisplayed" + i) != null);
            column.setAllowInsert(request.getParameter("fldAllowInsert" + i) != null);
            column.setAllowUpdate(request.getParameter("fldAllowUpdate" + i) != null);
            column.setNullable("true".equals(request.getParameter("fldNullable" + i)));
            column.setSpecial(request.getParameter("fldSpecial" + i));
            colFac.save(column);    //new column
        }
        colFac.refreshList();
        response.sendRedirect("ListTables.jsp?server_id="+serverID);
        return;
    }
    
   
                        
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Generic Data Maintenance Application - List Columns</title>
        <script language="javascript" src="js/general.js"></script>
        <script language="javascript" src="js/ListColumns.js"></script>
    </head>
<body class="nomargin nopadding">
<form method="post" action="ListColumns.jsp">
<table border="0" cellpadding="5" cellspacing="5" >
    <tr>
        <td width="50px">&nbsp;</td> 
        <td class="formHeader" ><%=reg.getName()%> - <%=table.getName()%> - Columns</td>
    </tr>

 <%-- SOCO 24/01/2006 COPY of buttons for top of screen--%>
        <tr>
        <td width="50px">&nbsp;</td>
        <td align="right"> 
          <input type="button" class="button" id="btnBack" name="btnBack" value="Back" onclick="window.location.href='ListTables.jsp?server_id=<%=serverID%>&ts=<%=(new Date()).getTime()%>'">&nbsp;        
          <input type="submit" class="button" id="btnSave" name="btnSave" value="Save" >&nbsp;
        </td>
    </tr>
 <%-- END SOCO 24/01/2006 --%>

    <tr>
        <td width="50px">&nbsp;</td> 
        <td>
            <table border="0" cellpadding="3" cellspacing="0" 
                   class="dataTable">
                <tr>
                    <td class="dataHeader">Column Name</td>
                    <td class="dataHeader">Type</td>
                    <td class="dataHeader">Displayed</td>                    
                    <td class="dataHeader">Allow Insert</td>                                        
                    <td class="dataHeader">Allow Update</td>                                        
                    <td class="dataHeader">Lookup</td>
                    <td class="dataHeader">Special</td>
                </tr>
<%
    //convert tables ArrayList to a HashMap for easier look ups
    ArrayList columns = table.getColumns(); 
    HashMap columsMap = new HashMap();
    Column colFromDB = null;
    for(int i = 0; i < columns.size() ; i++)
        columsMap.put( ((Column)columns.get(i)).getName(),columns.get(i));
        
    for(int i = 0; i < columnDB.size() ; i++){
        column = (Column)columsMap.get(((Column)columnDB.get(i)).getName());
        colFromDB = (Column)columnDB.get(i);
%>
                <tr class="dataBody">
                    <td class="dataGreyBorder" >
                                
                        <input name="fldID<%=i%>" id="fldID<%=i%>" 
                               type="hidden" value="<%=column==null?"":""+column.getId()%>">
                        <input name="fldName<%=i%>" id="fldName<%=i%>" 
                               type="hidden" value="<%=colFromDB.getName()%>">
                        <input name="fldColumnType<%=i%>" id="fldColumnType<%=i%>" 
                               type="hidden" value="<%=colFromDB.getColumnType()%>">
                        <input name="fldNullable<%=i%>" id="fldNullable<%=i%>" 
                               type="hidden" value="<%=colFromDB.isNullable()%>"> 
                        <input name="fldColumnTypeString<%=i%>" id="fldColumnTypeString<%=i%>" 
                               type="hidden" value="<%=colFromDB.getColumnTypeString()%>">                                                                                            
                    <%=((Column)columnDB.get(i)).getName()%></td>
                    <td class="dataGreyBorder" nowrap><%=colFromDB.getColumnTypeString()%></td>
                    <td align="center" class="dataGreyBorder" ><input name="fldDisplayed<%=i%>" id="fldDisplayed<%=i%>" 
                               type="checkbox" <%=(column==null?"":(column.isDisplayed()? "checked":""))%>></td>                    
                    <td class="dataGreyBorder" nowrap><input name="fldAllowInsert<%=i%>" id="fldAllowInsert<%=i%>" 
                               type="checkbox" <%=(column==null?"":(column.isAllowInsert()? "checked":""))%> 
                               <%=(colFromDB.isAllowInsert()?"":"DISABLED")%>></td>
                    <td class="dataGreyBorder" nowrap><input name="fldAllowUpdate<%=i%>" id="fldAllowUpdate<%=i%>" 
                               type="checkbox" <%=(column==null?"":(column.isAllowUpdate()? "checked":""))%> 
                               <%=(colFromDB.isAllowUpdate()?"":"DISABLED")%>></td>    
                    <td class="dataGreyBorder" nowrap><input name="fldAllowLookup<%=i%>" id="fldAllowLookup<%=i%>" 
                               type="checkbox" <%=(column==null?"":(column.getDropDownColumnDisplay() == null ? "":"checked"))%> 
                               onclick="toggleColumnLookup(<%=i%>);"></td>  
                    <td class="dataGreyBorder" nowrap>
                        <select name="fldSpecial<%=i%>" id="fldSpecial<%=i%>" >
                            <option value="N" <%=(column==null ? "selected":(column.getSpecial() == null || "N".equalsIgnoreCase(column.getSpecial())? "selected":""))%>>No</option>
                            <option value="U" <%=(column==null ? "selected":("U".equalsIgnoreCase(column.getSpecial())? "selected":""))%>>User</option>
                            <option value="D" <%=(column==null ? "selected":("D".equalsIgnoreCase(column.getSpecial())? "selected":""))%>>Date</option>
                        </select></td>                                 
                </tr>
<%
    ServerRegistration columnServer = null;
    Table columnTable = null;
    Column colDropDownColumnDisplay = null;
    Column colDropDownColumnStore = null;
    if(column!=null && column.getDropDownColumnDisplay() != null)
    {
        colDropDownColumnDisplay = colFac.getColumn(column.getDropDownColumnDisplay().longValue());
        colDropDownColumnStore = colFac.getColumn(column.getDropDownColumnStore().longValue());
        columnTable = TableFactory.getInstance().getTable(colDropDownColumnDisplay.getTableID());
        columnServer = servFac.getServerRegistration(columnTable.getServerID());
    }
%> 
                <tr id="trLookup<%=i%>" <%=column==null || column.getDropDownColumnDisplay() == null?"style=\"display:none\"":""%> class="dataBody">               
                    <td colspan="7">
                        <table border="1" cellpadding="0" cellspacing="0" width="100%" style="border: 1px solid black">
                            <tr>
                                <td class="dataHeader">Server</td>
                                <td class="dataHeader">Table</td>
                                <td class="dataHeader">Display Column</td>
                                <td class="dataHeader">Stored Column</td>
                                <td class="dataHeader"><input type="button" value="..." onclick="doDDLookup(<%=i%>)"; style="height:15px;border:0px;font-weight:bold"></td>
                            </tr>                         
                            <tr class="dataBody">
                                <td class="dataGreyBorder">
                                  <input type="hidden" value="<%=columnServer == null ? "" : columnServer.getId().toString()%>" name="fldDropDownColumnDisplayServerID<%=i%>" id="fldDropDownColumnDisplayServerID<%=i%>">
                                  <input type="text" value="<%=columnServer == null ? "" : columnServer.getName()%>" name="fldDropDownColumnDisplayServerName<%=i%>" id="fldDropDownColumnDisplayServerName<%=i%>" readonly class="formInput"></td>
                                <td class="dataGreyBorder">
                                  <input type="hidden" value="<%=columnTable == null ? "" : columnTable.getId().toString()%>" name="fldDropDownColumnDisplayTableID<%=i%>" id="fldDropDownColumnDisplayTableID<%=i%>">
                                  <input type="text" value="<%=columnTable == null ? "" : columnTable.getName()%>" name="fldDropDownColumnDisplayTableName<%=i%>" id="fldDropDownColumnDisplayTableName<%=i%>" readonly class="formInput"></td>                                
                                <td class="dataGreyBorder">
                                  <input type="hidden" value="<%=colDropDownColumnDisplay == null ? "" : colDropDownColumnDisplay.getId().toString()%>" name="fldDropDownColumnDisplayID<%=i%>" id="fldDropDownColumnDisplayID<%=i%>">
                                  <input type="text" value="<%=colDropDownColumnDisplay == null ? "" : colDropDownColumnDisplay.getName()%>" name="fldDropDownColumnDisplayName<%=i%>" id="fldDropDownColumnDisplayName<%=i%>" readonly class="formInput"></td>   
                                <td class="dataGreyBorder">
                                  <input type="hidden" value="<%=colDropDownColumnStore == null ? "" : colDropDownColumnStore.getId().toString()%>" name="fldDropDownColumnStoreID<%=i%>" id="fldDropDownColumnStoreID<%=i%>">
                                  <input type="text" value="<%=colDropDownColumnStore == null ? "" : colDropDownColumnStore.getName()%>" name="fldDropDownColumnStoreName<%=i%>" id="fldDropDownColumnStoreName<%=i%>" readonly class="formInput"></td>                    
                                <td class="dataGreyBorder"></td>  
                            </tr>
                        </table>
                    </td>                    
                </tr>
<%
    }
%>
            </table>
        </td>
    </tr>
    <tr>
        <td width="50px">&nbsp;</td>
        <td align="right">
            <input name="mode" id="mode" type="hidden" value="SAVE_COLUMNS">
            <input name="server_id" id="server_id" type="hidden" value="<%=serverID%>">
            <input name="table_id" id="table_id" type="hidden" value="<%=tableID%>">            
            <input type="button" class="button" id="btnBack" name="btnBack" value="Back" onclick="window.location.href='ListTables.jsp?server_id=<%=serverID%>&ts=<%=(new Date()).getTime()%>'">&nbsp;        
            <input type="submit" class="button" id="btnSave" name="btnSave" value="Save" >&nbsp;
    </tr>
 </table>
</form> 
</body>
</html>