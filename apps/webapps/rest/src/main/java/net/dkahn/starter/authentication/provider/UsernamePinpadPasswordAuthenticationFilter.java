package net.dkahn.starter.authentication.provider;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter pour cree le notre Credential
 */
public class UsernamePinpadPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String SPRING_SECURITY_FORM_PINPAD_KEY   = "pinpad";
    private String pinpadParameter = SPRING_SECURITY_FORM_PINPAD_KEY;

    private boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String pinpad = obtainPinpad(request);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        if (pinpad == null) {
            pinpad = "";
        }


        username = username.trim();

        //create credential
        CredentialPinpad credential = new CredentialPinpad();
        credential.setPassword(password);
        credential.setPindpadId(pinpad);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, credential);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    private String obtainPinpad(HttpServletRequest request) {
        return request.getParameter(pinpadParameter);
    }

    @SuppressWarnings("unused")
    public void setPinpadParameter(String pinpadParameter) {
        Assert.hasText(pinpadParameter, "Pinpad parameter must not be empty or null");
        this.pinpadParameter = pinpadParameter;
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    @SuppressWarnings("unused")
    public final String getPinpadParameter() {
        return pinpadParameter;
    }

}
