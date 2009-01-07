package com.vodafone.gdma.security;

import org.springframework.security.AuthenticationException;

public class AccessException extends AuthenticationException {

    public AccessException(String msg) {
        super(msg);
    }

    public AccessException(String msg, Throwable t) {
        super(msg, t);
    }

}
