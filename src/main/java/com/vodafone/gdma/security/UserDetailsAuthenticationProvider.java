package com.vodafone.gdma.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationServiceException;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;

public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private UserDetailsService userDetailsService;
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser;
        try {
        	System.out.println("\n\n\n>>>>>" + username);
            loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        } catch (DataAccessException repositoryProblem) {
            throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
        if (loadedUser == null) throw new AuthenticationServiceException("User cannot be null");
        return loadedUser;
    }
    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		
	}
}
