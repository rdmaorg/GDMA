package ie.clients.gdma.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.Assert;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class SessionExpirationFilter implements Filter, InitializingBean {
    //~ Instance fields ================================================================================================

    protected static Logger LOG = Logger.getLogger(SessionExpirationFilter.class);
    
    private String expiredUrl;

    //~ Methods ========================================================================================================

    public void afterPropertiesSet() throws Exception {
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("                IN SessionExpirationFilter.afterPropertiesSet() ");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");

        Assert.hasText(expiredUrl, "/index.htm");
    }

    /**
     * Does nothing. We use IoC container lifecycle services instead.
     */
    public void destroy() {
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("                IN SessionExpirationFilter.destroy() ");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("                IN SessionExpirationFilter.doFilter() ");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        Assert.isInstanceOf(HttpServletRequest.class, request, "Can only process HttpServletRequest");
        Assert.isInstanceOf(HttpServletResponse.class, response, "Can only process HttpServletResponse");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);

        
        LOG.debug("session = " + session);
        
        if (session == null &&
            httpRequest.getRequestedSessionId() != null &&
            !httpRequest.isRequestedSessionIdValid())
        {    
            LOG.debug("SessionId = " + httpRequest.getRequestedSessionId());

            String targetUrl = httpRequest.getContextPath() + expiredUrl;
            httpResponse.sendRedirect(httpResponse.encodeRedirectURL(targetUrl));
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Does nothing. We use IoC container lifecycle services instead.
     *
     * @param arg0 ignored
     *
     * @throws ServletException ignored
     */
    public void init(FilterConfig arg0) throws ServletException {
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("                IN SessionExpirationFilter.init() ");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        
    }

    public void setExpiredUrl(String expiredUrl) {
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("                IN SessionExpirationFilter.setExpiredUrl(" + expiredUrl + ") ");
        LOG.debug("-----------------------------------------------------------");
        LOG.debug("-----------------------------------------------------------");
        
        this.expiredUrl = expiredUrl;
    }
}
