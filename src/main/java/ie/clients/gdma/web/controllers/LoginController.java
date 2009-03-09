package ie.clients.gdma.web.controllers;

import ie.clients.gdma.web.command.LoginFormCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;


public class LoginController extends SimpleFormController {

    private static Logger LOG = Logger.getLogger(LoginController.class);

    public LoginController() {
        setCommandClass(LoginFormCommand.class);
        setCommandName("command");
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
            BindException errors) throws Exception {
        LoginFormCommand loginFormCommand = (LoginFormCommand) command;
        return new ModelAndView(new RedirectView(getSuccessView()));
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        LoginFormCommand loginFormCommand = new LoginFormCommand();
        return loginFormCommand;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        ArrayList<String> domains = new ArrayList<String>();
        domains.add("one");
        domains.add("two");
        model.put("domains", domains);
        return model;
    }

}
