<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.*"%>
<%
    String serverID = request.getParameter("server_id");
    long lngServerID = Long.parseLong(serverID);
    ServerRegistrationFactory servFac = ServerRegistrationFactory.getInstance();
    ServerRegistration reg = servFac.getServerRegistration(lngServerID);    
    ArrayList tableNames = servFac.getTablesFromDB(reg);
    
    Table table = null;

        
    if("POST".equalsIgnoreCase(request.getMethod())){
        TableFactory tabFac = TableFactory.getInstance();
        int i = 0;
        String fldID;
        String fldName;
        String fldEditable;
        
        while(request.getParameter("fldName" +i) != null){
            fldID = request.getParameter("fldID" + i);
            fldName = request.getParameter("fldName" + i);
            fldEditable = request.getParameter("fldEditable" + i);

            if(fldID == null || "".equals(fldID))
            {
                //new table
                tabFac.addTable(lngServerID,
                                fldName,
                                fldEditable != null);
            }else{
                //old table 
                tabFac.updateTable(Long.parseLong(fldID),
                                   lngServerID,
                                   fldName,
                                   fldEditable != null);
            }
            i++;
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
                    <td class="dataHeader">Include</td>
                </tr>
<%
    //convert tables ArrayList to a HashMap for easier look ups
    ArrayList tables = reg.getTables(); 
    HashMap tablesMap = new HashMap();
    for(int i = 0; i < tables.size() ; i++)
        tablesMap.put( ((Table)tables.get(i)).getName(),tables.get(i));
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
                    <td align="center" class="dataGreyBorder" ><input name="fldEditable<%=i%>" id="fldEditable<%=i%>" 
                               type="checkbox" <%=(table==null?"":(table.isEditable()? "checked":""))%>></td>
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