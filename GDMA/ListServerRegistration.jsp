<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.util.ArrayList"%>
<%
    ServerRegistrationFactory obj = ServerRegistrationFactory.getInstance();    
    ArrayList servers = obj.getList();
    ServerRegistration reg = null;
%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>List Servers</title>
        <script language="javascript" src="js/general.js"></script>
        <script language="javascript" src="js/ListServerRegistration.js"></script>
    </head>
<body class="nomargin nopadding">
<table border="0" cellpadding="5" cellspacing="5" width="100%">
    <tr>
        <td width="50px">&nbsp;</td> 
        <td class="formHeader" >Server Registrations</td>
    </tr>
    <tr>
        <td width="50px">&nbsp;</td> 
        <td>
            <table border="0" cellpadding="2" cellspacing="0" 
                   width="20%" class="dataTable" >
                <tr>
                    <td class="dataHeader">Servers</td>
                </tr>
<%
    for(int i = 0; i < servers.size() ; i++){
        reg = (ServerRegistration)servers.get(i);
%>
                <tr onmouseover="mouseEntered(this)" onmouseout="mouseExited(this)" 
                    onclick="mouseClicked(this,'<%=reg.getId()%>')" 
                    ondblclick="mouseDblClicked(this,'<%=reg.getId()%>')"
                    class="dataBody">
                    <td class="dataGreyBorder" ><%=reg.getName()%></td>
                </tr>
<%
    }
%>
            </table>
        </td>
    </tr>
    <tr>
        <td width="50px">&nbsp;</td>
        <td align="left">
            <input type="button" class="button" id="btnTable" name="btnTable" value="Tables" onclick="doTables()">&nbsp;        
            <input type="button" class="button" id="btnDelete" name="btnDelete" value="Delete" onclick="doDelete()">&nbsp;
            <input type="button" class="button" id="btnEdit" name="btnEdit" value="Edit" onclick="doEdit()">&nbsp;
            <input type="button" class="button" id="btnAdd" name="btnAdd" value="Add" onclick='doNew()'></td>
    </tr>
 </table>
</body>
</html>