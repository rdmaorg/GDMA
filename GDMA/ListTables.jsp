<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
    String serverID = request.getParameter("server_id");
    Long lngServerID = new Long(serverID);
    ServerRegistrationFactory servFac = ServerRegistrationFactory.getInstance();
    ServerRegistration reg = servFac.getServerRegistration(lngServerID);  
    TableFactory tabFac = TableFactory.getInstance(); 
    //get the actual list of tables 
    ArrayList tableNames = servFac.getTablesFromDB(reg);
   
    Table table = null;

    if("POST".equalsIgnoreCase(request.getMethod())){
        
         for(int i = 0; request.getParameter("fldName" +i) != null; i++){
            table = new Table();
            table.setId(request.getParameter("fldID" + i));
            table.setServerID(lngServerID);
            table.setName(request.getParameter("fldName" + i));
            table.setDisplayed(request.getParameter("fldDisplayed" + i) != null);
            table.setAllowDelete(request.getParameter("fldAllowDelete" + i) != null);            
            
            if(table.getId() == null)                
                tabFac.addTable(table);    //new table
            else
                tabFac.updateTable(table); //old table 
        }
    }
    

      
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>List Tables</title>
        <script language="javascript" src="js/general.js"></script>
        <script language="javascript" src="js/ListTables.js"></script>
    </head>
<body class="nomargin nopadding">
<form method="post" action="ListTables.jsp">
<table border="0" cellpadding="5" cellspacing="5" >
    <tr>
        <td width="50px">&nbsp;</td> 
        <td class="formHeader" ><%=reg.getName()%> - Tables</td>
    </tr>
    <tr>
        <td width="50px">&nbsp;</td> 
        <td>
            <table border="0" cellpadding="3" cellspacing="0" 
                   width="50%" class="dataTable" >
                <tr>
                    <td class="dataHeader">Table Name</td>
                    <td class="dataHeader">Displayed</td>
                    <td class="dataHeader" nowrap>Allow Delete</td>
                </tr>
<%
    //get the list of tables in the DB
    ArrayList tables = reg.getTables(); 
    //convert tables ArrayList to a HashMap for easier look ups 
    HashMap tablesMap = new HashMap();
    for(int i = 0; i < tables.size() ; i++)
        tablesMap.put( ((Table)tables.get(i)).getName(),tables.get(i));
    //iterate throught eh names from the database
    for(int i = 0; i < tableNames.size() ; i++){
        table = (Table)tablesMap.get((String)tableNames.get(i));
%>
                <tr onmouseover="mouseEntered(this)" onmouseout="mouseExited(this)" 
                    onclick="mouseClicked(this,'<%=table == null ? "-1" : ""+ table.getId()%>')" 
                    ondblclick="mouseDblClicked(this,'<%=table == null ? "-1" : "" + table.getId()%>')"
                    class="dataBody">
                    <td class="dataGreyBorder" >
                        <input name="fldID<%=i%>" id="fldID<%=i%>" type="hidden" value="<%=table==null?"":""+table.getId()%>">
                        <input name="fldName<%=i%>" id="fldName<%=i%>" type="hidden" value="<%=(String)tableNames.get(i)%>">
                    <%=(String)tableNames.get(i)%></td>
                    <td align="center" class="dataGreyBorder" ><input name="fldDisplayed<%=i%>" id="fldDisplayed<%=i%>" 
                               type="checkbox" <%=(table==null?"":(table.isDisplayed()? "checked":""))%>></td>
                    <td align="center" class="dataGreyBorder" ><input name="fldAllowDelete<%=i%>" id="fldAllowDelete<%=i%>" 
                               type="checkbox" <%=(table==null?"":(table.isAllowDelete()? "checked":""))%>></td>
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
            <input type="button" class="button" id="btnBack" name="btnBack" value="Back" onclick="window.location.href='ListServerRegistration.jsp?ts=<%=(new Date()).getTime()%>'">&nbsp;        
            <input type="button" class="button" id="btnColumns" name="btnColumns" value="Columns" onclick="doColumns(<%=serverID%>);">&nbsp;            
            <input type="submit" class="button" id="btnSave" name="btnSave" value="Save" >&nbsp;
    </tr>
 </table>
</form> 
</body>
</html>