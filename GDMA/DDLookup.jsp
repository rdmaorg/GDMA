<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
    
    String serverID = request.getParameter("server_id");
    String tableID = request.getParameter("table_id");
    String columnDisplayID = request.getParameter("column_display_id");
    String columnStoreID = request.getParameter("column_store_id");
    
    Long lngServerID = null;
    Long lngTableID = null;
    Long lngColumnDisplayID = null;
    Long lngColumnStoreID = null;
    
    if(serverID != null && !"".equals(serverID) )
        lngServerID = new Long(serverID);

    if(tableID != null && !"".equals(tableID) )
        lngTableID = new Long(tableID);

    if(columnDisplayID != null && !"".equals(columnDisplayID) )
        lngColumnDisplayID = new Long(columnDisplayID);

    if(columnStoreID != null && !"".equals(columnStoreID) )
        lngColumnStoreID = new Long(columnStoreID);

    ArrayList regs = ServerRegistrationFactory.getInstance().getList();
    ArrayList tables = TableFactory.getInstance().getList();
    ArrayList columns = ColumnFactory.getInstance().getList();
    
    
                    
%>                 
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Generic Data Maintenance Application - List Columns</title>
        <script language="javascript" src="js/DDLookup.js"></script>
        <script>
        function ObjectKeyPair(){}


            var arrTables = new Array();
            var obj;
<%
    for(int i = 0 ; i < tables.size(); i++){
        Table table = (Table)tables.get(i);
%>
                obj = new ObjectKeyPair();
                obj.id = <%=table.getId()%>;
                obj.parentid = <%=table.getServerID()%>;
                obj.name = "<%=table.getName()%>";
                arrTables[<%=i%>] = obj;
            
<%    
   }
%> 

            var arrColumns = new Array();
            var obj;
<%
    for(int i = 0 ; i < columns.size(); i++){
        Column column = (Column)columns.get(i);
%>
                obj = new ObjectKeyPair();
                obj.id = <%=column.getId()%>;
                obj.parentid = <%=column.getTableID()%>;                
                obj.name = "<%=column.getName()%>";
                arrColumns[<%=i%>] = obj;
            
<%    
   }
   
%> 
        </script>
    </head>
<body class="nomargin nopadding" onload="doOnLoad();">
<form method="post" action="ListColumns.jsp">
<table border="0" cellpadding="5" cellspacing="5" >
  <tr>
    <td class="formLabel">Server</td>
    <td class="formLabel">
      <select class="formDDSelect" name="server_id" id="server_id" value="" onchange="doRefreshTableList();">
        <option selected value="-1"  >-----</option>
<%
    for(int i = 0 ; i < regs.size(); i++){
        ServerRegistration reg = (ServerRegistration)regs.get(i);
%>
        <option value="<%=reg.getId()%>"  ><%=reg.getName()%></option>
    <%    
        }
    %>                
      </select>                    
    </td>
    <td class="formLabel">Table</td>
    <td class="formLabel">
      <select class="formDDSelect" name="table_id" id="table_id" value="" onchange="doRefreshColumnList();"></select>        
    </td>
  </tr>                         
  <tr>
    <td class="formLabel">Display Column</td>
    <td class="formLabel">
      <select class="formDDSelect" name="column_display_id" id="column_display_id" value=""></select>                      
    </td>                                
    <td class="formLabel">Stored Column</td>
    <td class="formLabel">
      <select class="formDDSelect" name="column_store_id" id="column_store_id" value=""></select>                     
    </td>                                
  </tr>                                                  
  <tr>
    <td  colspan="4" align="right">
      <input type="button" class="button" id="btnCancel" value="Cancel" onclick="doCancel();">&nbsp;        
      <input type="button" class="button" id="btnOk" value="OK" onclick="doOK();">&nbsp;</td>
  </tr>
</table>
</form>
</body>
</html>
