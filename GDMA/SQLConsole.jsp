<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,                 
                 java.util.ArrayList"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title> Generic Data Maintenance Application - SQL Console</title>
        <script>
            function jfnMSKeyPress()
            {
              if(window.event.keyCode == 118)
                   document.forms[0].submit();
            }            
            if (navigator.appName == 'Netscape') {
                window.onKeyPress = '';
            }
        </script>
    </head>
<body class="nomargin nopadding" style="border:1 solid #000000" 
      onkeydown="jfnMSKeyPress();">
<form action="SQLResults.jsp" method="put" target="sql_bottom" >
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">   
    <tr>
        <td width="100%"  class="toolBar" style="background-color: #D4D0C8">
            <table border="0" cellpadding="0" cellspacing="0">   
                <tr height="25px">
                    <td width="25px" >&nbsp;</td>
                    <td width="25px" class="greyButton" align="center" valign="middle"
                        onmouseover="this.className = 'greyButtonHover';"
                        onmouseout="this.className = 'greyButton';"
                    ><a href="javascript:document.forms[0].submit()"><img border="0" 
                            src="images/run.gif" alt="Execute (F7)"></a></td>
                    <td width="25px">&nbsp;</td>         
                    <td class="formLabel">Server&nbsp;</td>
                    <td>
                        <select class="formInput" name="server" id="server">
<%
    ArrayList servers = ServerRegistrationFactory.getInstance().getList();
    for(int i = 0 ; i < servers.size(); i++){
%>
                            <option value="<%=((ServerRegistration)servers.get(i)).getId()%>"><%=((ServerRegistration)servers.get(i)).getName()%></option>
<%    
    }
%>                
                    
                        </select>
                    </td>
                </tr>
            </table>
        </td>
    </tr>              
    <tr>
        <td  height="100%"><textarea class="nomargin nopadding" name="sql" id="sql"
                style="width:100%;height:100%;border: 1 inset #000000;padding:2px"></textarea></td>
    </tr>
</table>
</form>
</body>
</html>