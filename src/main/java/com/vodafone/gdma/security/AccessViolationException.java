package com.vodafone.gdma.security;

import org.springframework.security.AuthenticationException;

public class AccessViolationException extends AuthenticationException {

    public AccessViolationException(String msg) {
        super(msg);
    }

    public AccessViolationException(String msg, Throwable t) {
        super(msg, t);
    }

}
