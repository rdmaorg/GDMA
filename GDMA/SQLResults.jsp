<%@ page language="java" %>
<%@ page import="com.vodafone.gdma.dbaccess.*,
                 java.sql.*"%>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
    <head>
        <style type="text/css" media="all">
            @import "css/style.css";
        </style>
        <title>Generic Data Maintenance Application - SQL Results</title>
    </head>
<body class="nomargin nopadding" style="background-color: #D4D0C8">
<table border="0" cellpadding="2" cellspacing="0" class="dataTable">
<%
    String serverID = request.getParameter("server");
    String SQL = request.getParameter("sql");
    if(serverID != null && SQL != null){
        ServerRegistration reg = ServerRegistrationFactory
                                          .getInstance()
                                          .getServerRegistration(new Long(serverID));
        if(reg !=  null){
            ODBCProvider odbc = ODBCProviderFactory.getInstance().getODBCProvider(reg.getOdbcTypeID());
            Connection con = DBUtil.getConnection(
                                    odbc.getConnectionClass(), 
                                    reg.getUsername(), 
                                    reg.getPassword(), 
                                    reg.getConnectionURL());
            Statement stmt = con.createStatement();
            ResultSet rs ;
            try{
                rs = stmt.executeQuery(SQL);
        
                int idx = 1;
                if(rs != null)
                { 
%>
    <tr>
        <td class="dataHeader" width="30px" nowrap>&nbsp;&nbsp;&nbsp;</td>                        
<%
                    for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
%>
        <td class="dataHeader" nowrap ><%=rs.getMetaData().getColumnName(i)%>&nbsp;</td>                    
<%
                    }
%>
    </tr>
<%
                    while(rs.next()){
%>
    <tr class="dataBody">
        <td class="dataHeader" align="left" style=" font-weight: normal"><%=idx++%></td>      
<%
                        for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++){                    
%>
        <td class="dataGreyBorder" nowrap><%=rs.getString(i)%></td>
<%
                       }
%>
    </tr>
<%
                }
            }  
            
            rs.close();
            stmt.close();
            }catch(Exception e){
                out.println("<BR>" + e.getMessage() + "<BR>");
            }finally{
            con.close();
            }
        }
    }
%>  
</table>
</body>
</html>