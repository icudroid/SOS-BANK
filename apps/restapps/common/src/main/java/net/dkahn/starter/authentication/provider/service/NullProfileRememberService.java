package net.dkahn.starter.authentication.provider.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dev on 29/11/16.
 */
public class NullProfileRememberService implements IProfileRememberService {
    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {

    }

    @Override
    public String getUsername(HttpServletRequest request) {
        return null;
    }

    @Override
    public void unvalidateToken(HttpServletRequest request, HttpServletResponse response) {

    }


}