package ie.clients.gdma.security;

import ie.clients.gdma.dao.UserDao;
import ie.clients.gdma.domain.User;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jcifs.Config;
import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.util.Assert;


public class JcifsAuthenticationProvider implements AuthenticationProvider, InitializingBean {
    private static Logger LOG = Logger.getLogger(JcifsAuthenticationProvider.class);

    private String domainController;

    private String defaultDomain;

    private String defaultRole;

    private String adminRole;

    private UserDao userDao;
    
    private Date expirationDate;

    public JcifsAuthenticationProvider() {
        Config.setProperty("jcifs.smb.client.soTimeout", "300000");
        Config.setProperty("jcifs.netbios.cachePolicy", "1200");
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.domainController, "A Domain controller must be set");
        Assert.notNull(this.defaultDomain, "A Default Domain must be set");
        Assert.notNull(this.userDao, "A User Dao must be set");
        Assert.notNull(this.defaultRole, "A default role must be set");
        Assert.notNull(this.adminRole, "An admin role must be set");
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
            return null;
        }
            
        if (expirationDate != null) {
       		if (new Date().after(expirationDate)) {
       			throw new AccessException("The application is expired");
       		}
        }

        NtlmPasswordAuthentication npa = getNtlmPasswordAuthentication((UsernamePasswordAuthenticationToken) authentication);
        try {
            UniAddress dc = UniAddress.getByName(domainController, true);
            SmbSession.logon(dc, npa);

            if (LOG.isDebugEnabled()) {
                LOG.debug("SmbSession logon successfull for [" + npa + "]");
            }
        } catch (UnknownHostException e) {
            LOG.error(e, e);
            throw new AccessException("Could not connect to domain controller - [" + domainController + "]");
        } catch (SmbAuthException e) {
            LOG.error("SmbAuthException: " + npa.getName() + ": 0x"
                    + jcifs.util.Hexdump.toHexString(e.getNtStatus(), 8) + ": " + e, e);
            if (e.getNtStatus() == SmbAuthException.NT_STATUS_ACCESS_VIOLATION) {
                throw new AccessViolationException("Access violation");
            } else {
                throw new AccessException(e.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e, e);
            throw new AccessException(e.toString());
        }

        return getAuthorisation(npa);

    }

    public NtlmPasswordAuthentication getNtlmPasswordAuthentication(UsernamePasswordAuthenticationToken upat)
            throws AuthenticationException {
        String username = upat.getPrincipal() == null ? "" : upat.getPrincipal().toString().trim();
        String password = upat.getCredentials() == null ? "" : upat.getCredentials().toString().trim();
        int index = username.indexOf('\\');
        if (index == -1)
            index = username.indexOf('/');
        String domain = (index != -1) ? username.substring(0, index) : defaultDomain;
        username = (index != -1) ? username.substring(index + 1) : username;
        NtlmPasswordAuthentication npa = new NtlmPasswordAuthentication(domain, username, password);
        if (LOG.isDebugEnabled()) {
            LOG.debug("NtlmPasswordAuthentication = domain[" + domain + "], user[" + username + "], password["
                    + "*****" + "], host[" + domainController + "]");
        }

        return npa;

    }

    private UsernamePasswordAuthenticationToken getAuthorisation(NtlmPasswordAuthentication npa) {

        // now get roles
        User user = null;
        try {
            user = userDao.get(npa.getUsername());
        } catch (Exception e) {
            LOG.error(e, e);
            throw new AccessException(e.getMessage());
        }

        //if user does not exist or has been made inactive, do not grant access
        if (user == null || user.isActive() != true) {
            throw new AccessException("User [" + npa + "] does not have access to GDMA");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Found user [" + npa + "] in GDMA");
        }

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new GrantedAuthorityImpl(defaultRole));
        if (user.isAdmin()) {
            authorities.add(new GrantedAuthorityImpl(adminRole));
        }

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user, npa
                .getPassword(), (GrantedAuthority[]) authorities.toArray(new GrantedAuthority[authorities.size()]));

        return result;
    }

    public boolean supports(Class authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setDomainController(String domainController) {
        this.domainController = domainController;
        Config.setProperty("jcifs.http.domainController", domainController);
    }

    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

}
