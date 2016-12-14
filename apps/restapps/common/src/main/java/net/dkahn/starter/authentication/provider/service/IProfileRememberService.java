package net.dkahn.starter.authentication.provider.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dev on 29/11/16.
 */
public interface IProfileRememberService {

    void loginFail(HttpServletRequest request, HttpServletResponse response);

    void loginSuccess(HttpServletRequest request, HttpServletResponse response,
                      Authentication successfulAuthentication);

    String getUsername(HttpServletRequest request);

    void unvalidateToken(HttpServletRequest request, HttpServletResponse response);
}
