package net.dkahn.starter.authentication.provider;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by dev on 29/11/16.
 */
public class ProfileLoginAuthenticationException extends AuthenticationException {
    public ProfileLoginAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public ProfileLoginAuthenticationException(String msg) {
        super(msg);
    }
}
