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

import org.apache.log4j.Logger;

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
    // Log4j logger
    private static Logger logger = Logger
            .getLogger("ServerRegistrationFactory");
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

            //the following codes handles the fact that IE makes multiple requests
            //for the same doc
            String accLang = request.getHeader("accept-language");
            String userAgent = request.getHeader("user-agent");
            logger.debug("accept-language: " + accLang);
            logger.debug("user-agent     : " + userAgent);
            
            for (Enumeration e = request.getHeaderNames() ; e.hasMoreElements() ;) {
                String name = (String)e.nextElement();
                logger.debug(name + " : " + request.getHeader(name));
            }
            logger.debug("\n\n\n\n");
            
            // IE5 sends a user-agent of contype on the first request, so this can be ignored
            /*if (userAgent != null && "contype".equalsIgnoreCase(userAgent.trim())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            
            // IE 5.5 and IE 6 only has an accept-language header on the first request so if present
            // ignore this request
            if (userAgent !=null && userAgent.indexOf("MSIE") != -1){
                if (accLang != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                     return;
                }
            }*/
            
            String serverID = request.getParameter("server_id");
            String tableID = request.getParameter("table_id");

            if (serverID == null || tableID == null) {
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
            String content = null;
            if (request.getServletPath().indexOf("export.xml") > -1) {
                response.setContentType("text/xml");
                response.setHeader("Content-Disposition",
                        "attachment");
                content = ed.select(reg,table,request,"XML");
            } else {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition",
                        "attachment");
                content = ed.select(reg,table,request,"CSV");                
            }
            response.setContentLength(content.getBytes().length);
            printer.print(content);
        } catch (Exception ex) {
            logger.error("Esception while trying to generate downlaod",ex);
        }
    }
}
