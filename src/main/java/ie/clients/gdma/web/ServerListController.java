package ie.clients.gdma.web;

import ie.clients.gdma.GdmaFacade;
import ie.clients.gdma.domain.Server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;


public class ServerListController implements Controller {
    private static Logger LOG = Logger.getLogger(ServerListController.class);
    private GdmaFacade gdma;

    private View view;

    public void setView(View view) {
        this.view = view;
    }

    public void setGdma(GdmaFacade gdma) {
        this.gdma = gdma;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        LOG.debug("username : " + username);
        List<Server> servers = gdma.getServerDao().getServerTableList(username);
        return new ModelAndView(view, "servers", servers);
    }
}
