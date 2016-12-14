package net.dkahn.starter.services.security.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;

/**
 * Created by dev on 26/11/16.
 */
@Getter
public class RestAuthenticationException extends AuthenticationException {

    private final int attemps;
    private final String messageCode;
    private final LocalDateTime lockedUntil;

    public RestAuthenticationException(String msg, Throwable t, int attemps, String messageCode, LocalDateTime lockedUntil) {
        super(msg, t);
        this.attemps = attemps;
        this.messageCode = messageCode;
        this.lockedUntil = lockedUntil;
    }

    public RestAuthenticationException(String msg, int attemps, String messageCode, LocalDateTime lockedUntil) {
        super(msg);
        this.attemps = attemps;
        this.messageCode = messageCode;
        this.lockedUntil = lockedUntil;
    }


    public RestAuthenticationException(String msg) {
        super(msg);
        this.attemps = 3;
        this.messageCode = null;
        this.lockedUntil = null;
    }
}
