<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
    String serverID = request.getParameter("server_id");
    String tableID = request.getParameter("table_id");
    long lngServerID = Long.parseLong(serverID);
    long lngTableID = Long.parseLong(tableID);
    ServerRegistrationFactory servFac = ServerRegistrationFactory.getInstance();
    ServerRegistration reg = servFac.getServerRegistration(lngServerID); 
    Table table = TableFactory.getInstance().getTable(lngTableID);   
    ArrayList columnDB = servFac.getColumnsFromDB(reg, table.getName());
    Column column = null;

    if("POST".equalsIgnoreCase(request.getMethod())){
        ColumnFactory colFac = ColumnFactory.getInstance();

        for(int i = 0; request.getParameter("fldName" +i) != null; i++){
            column = new Column();
            column.setId(request.getParameter("fldID" + i));
            column.setTableID(new Long(lngTableID));
            column.setName(request.getParameter("fldName" + i));            
            column.setColumnType(request.getParameter("fldColumnType" + i));
            column.setColumnTypeString(request.getParameter("fldColumnTypeString" + i));            
            column.setDropDownColumnDisplay(request.getParameter("fldDropDownColumnDisplay" + i));
            column.setDropDownColumnStore(request.getParameter("fldDropDownColumnStore" + i)); 
            column.setEditable(request.getParameter("fldEditable" + i) != null);
            column.setIncluded(request.getParameter("fldInclude" + i) != null);
            column.setNullable("true".equals(request.getParameter("fldNullable" + i)));

            if(column.getId() == null)
                colFac.addColumn(column);    //new column
            else
                colFac.updateColumn(column); //old column 
        }
    }
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>List Columns</title>
        <script language="javascript" src="js/general.js"></script>
        <script language="javascript" src="js/ListTables.js"></script>
    </head>
<body class="nomargin nopadding">
<form method="post" action="ListColumns.jsp">
<table border="0" cellpadding="5" cellspacing="5" >
    <tr>
        <td width="50px">&nbsp;</td> 
        <td class="formHeader" ><%=reg.getName()%> - <%=table.getName()%> - Columns</td>
    </tr>
    <tr>
        <td width="50px">&nbsp;</td> 
        <td>
            <table border="0" cellpadding="3" cellspacing="0" 
                   width="50%" class="dataTable">
                <tr>
                    <td class="dataHeader">Column Name</td>
                    <td class="dataHeader">Type</td>
                    <td class="dataHeader">Editable</td>                    
                    <td class="dataHeader">Display</td>                                        
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
<!--
            fldDropDownColumnDisplay = request.getParameter("fldDropDownColumnDisplay" + i);
            fldDropDownColumnStore = request.getParameter("fldDropDownColumnStore" + i);
-->            

                                
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
                    <td class="dataGreyBorder" nowrap><input name="fldEditable<%=i%>" id="fldEditable<%=i%>" 
                               type="checkbox" <%=(column==null?"":(column.isEditable()? "checked":""))%> 
                               <%=(colFromDB.isEditable()?"":"DISABLED")%>></td>
                    <td align="center" class="dataGreyBorder" ><input name="fldInclude<%=i%>" id="fldInclude<%=i%>" 
                               type="checkbox" <%=(column==null?"":(column.isIncluded()? "checked":""))%>></td>
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
            <input name="server_id" id="server_id" type="hidden" value="<%=serverID%>">
            <input name="table_id" id="table_id" type="hidden" value="<%=tableID%>">            
            <input type="button" class="button" id="btnBack" name="btnBack" value="Back" onclick="window.location.href='ListTables.jsp?server_id=<%=serverID%>&ts=<%=(new Date()).getTime()%>'">&nbsp;        
            <input type="submit" class="button" id="btnSave" name="btnSave" value="Save" >&nbsp;
    </tr>
 </table>
</form> 
</body>
</html>