package net.dkahn.starter.authentication.provider.service;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by dev on 29/11/16.
 */
public class CookieTheftException extends AuthenticationException {
    public CookieTheftException(String msg, Throwable t) {
        super(msg, t);
    }

    public CookieTheftException(String msg) {
        super(msg);
    }
}
