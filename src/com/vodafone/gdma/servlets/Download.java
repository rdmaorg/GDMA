/*
 * Created on Mar 26, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.servlets;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vodafone.gdma.dbaccess.EditData;
import com.vodafone.gdma.dbaccess.ServerRegistration;
import com.vodafone.gdma.dbaccess.ServerRegistrationFactory;
import com.vodafone.gdma.dbaccess.Table;
import com.vodafone.gdma.dbaccess.TableFactory;

/**
 * @author RGILL
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Download extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServerRegistration reg = null;
        Table table = null;
        Long lngServerID;
        Long lngTableID;

        final PrintStream printer = new PrintStream(response.getOutputStream());

        try {
            if (request.getSession().getAttribute("USER") == null) {
                printer.print("You do not appear to be logged in");
                return;
            }

            String serverID = request.getParameter("server_id");
            String tableID = request.getParameter("table_id");
            String mode = request.getParameter("mode");

            if (serverID == null || tableID == null || mode == null) {
                response.setContentType("text/html");
                printer.print("This page was called incorrectly");
                return;
            }
            
            Hashtable htSearch = new Hashtable();
            for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
                String param = (String)e.nextElement();
                if(param.startsWith("search_")){
                    String value = request.getParameter(param);
                    if(value != null && !"".equals(value.trim())){
                        htSearch.put(param.substring(7),value);
                    }
                }
            }

            lngServerID = new Long(serverID);
            lngTableID = new Long(tableID);
            reg = ServerRegistrationFactory.getInstance()
                    .getServerRegistration(lngServerID);
            table = TableFactory.getInstance().getTable(lngTableID);

            EditData ed = new EditData();
            if ("XML".equals(mode)) {
                response.setContentType("text/xml");
                response.setHeader("Content-Disposition",
                        "attachment; filename=export.xml");
                printer.print(ed.select(reg,table,request,"XML"));
            } else {
                response.setContentType("text/html");
                response.setHeader("Content-Disposition",
                        "attachment; filename=export.csv");
                printer.print(ed.select(reg,table,request,"CSV"));
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
