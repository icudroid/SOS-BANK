package net.dkahn.starter.authentication.provider.service;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by dev on 29/11/16.
 */
public class ProfileRememberAuthenticationException  extends AuthenticationException {
    public ProfileRememberAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public ProfileRememberAuthenticationException(String msg) {
        super(msg);
    }
}
