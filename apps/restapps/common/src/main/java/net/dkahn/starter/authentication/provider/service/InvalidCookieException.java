package net.dkahn.starter.authentication.provider.service;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by dev on 29/11/16.
 */
public class InvalidCookieException  extends AuthenticationException {
    public InvalidCookieException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidCookieException(String msg) {
        super(msg);
    }
}
