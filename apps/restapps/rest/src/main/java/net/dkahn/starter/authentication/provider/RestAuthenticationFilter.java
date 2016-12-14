package net.dkahn.starter.authentication.provider;

import net.dkahn.starter.authentication.RestUser;
import net.dkahn.starter.authentication.provider.service.IProfileRememberService;
import net.dkahn.starter.authentication.provider.service.NullProfileRememberService;
import net.dkahn.starter.authentication.provider.service.ProfileRememberService;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Filter pour cree le notre Credential
 */
@SuppressWarnings("unused")
public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private static final String SPRING_SECURITY_FORM_BIRTHDATE_KEY   = "birthdate";
    private String birthdateParameter = SPRING_SECURITY_FORM_BIRTHDATE_KEY;

    private static final String SPRING_SECURITY_FORM_PINPAD_KEY   = "pinpad";
    private String pinpadParameter = SPRING_SECURITY_FORM_PINPAD_KEY;

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

    private UserDetailsService userDetailsService;
    private IProfileRememberService profileRememberService = new NullProfileRememberService();

    public RestAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    private boolean postOnly = true;

    public void setProfileTokenService(ProfileRememberService profileTokenService) {
        this.profileTokenService = profileTokenService;
    }

    private ProfileRememberService profileTokenService = null;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String rememberMeCookie = profileTokenService.extractRememberMeCookie(request);

        if (rememberMeCookie != null) {
            return byProfileTokenAuth(request,response,rememberMeCookie);
        }else{
            return normAuth(request,response);
        }

    }

    private Authentication normAuth(HttpServletRequest request, HttpServletResponse response) {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String pinpad = obtainPinpad(request);
        String birthdate = obtainBirthdate(request);

        if (username == null) {
            username = username;
        }

        if (password == null) {
            password = "";
        }

        if (pinpad == null) {
            pinpad = "";
        }


        LocalDate birthdateLD;
        username = username.trim();
        try {
            birthdateLD = LocalDate.parse(birthdate);
        }catch (DateTimeParseException e){
            birthdateLD = LocalDate.now();
        }

        //create credential
        CredentialPinpad credential = new CredentialPinpad();
        credential.setPassword(password);
        credential.setPindpadId(pinpad);
        credential.setBirthdate(birthdateLD);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, credential);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private Authentication byProfileTokenAuth(HttpServletRequest request, HttpServletResponse response, String rememberMeCookie) {
        logger.debug("Remember-me cookie detected");

        if (rememberMeCookie.length() == 0) {
            logger.debug("Cookie was empty");
            profileTokenService.cancelCookie(request, response);
            return null;
        }

        try {
            String[] cookieTokens = profileTokenService.decodeCookie(rememberMeCookie);

            UserDetails userDetails = profileTokenService.processAutoLoginCookie(cookieTokens, request, response);

            String username = userDetails.getUsername();
            String password = obtainPassword(request);
            String pinpad = obtainPinpad(request);
            LocalDate birthdate = ((RestUser) userDetails).getBirthdate();

            //create credential
            CredentialPinpad credential = new CredentialPinpad();
            credential.setPassword(password);
            credential.setPindpadId(pinpad);
            credential.setBirthdate(birthdate);

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, credential);

            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);

            return this.getAuthenticationManager().authenticate(authRequest);
        }
        catch (CookieTheftException cte) {
            profileTokenService.cancelCookie(request, response);
            throw cte;
        }
        catch (UsernameNotFoundException noUser) {
            logger.debug("Remember-me login was valid but corresponding user not found.",
                    noUser);
        }
        catch (InvalidCookieException invalidCookie) {
            logger.debug("Invalid remember-me cookie: " + invalidCookie.getMessage());
        }
        catch (AccountStatusException statusInvalid) {
            logger.debug("Invalid UserDetails: " + statusInvalid.getMessage());
        }
        catch (RememberMeAuthenticationException e) {
            logger.debug(e.getMessage());
        }

        profileTokenService.cancelCookie(request, response);

        throw new BadCredentialsException("");

    }


    private String obtainPinpad(HttpServletRequest request) {
        return request.getParameter(pinpadParameter);
    }


    private String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    private void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }


    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }


    private UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }




    public boolean isPostOnly() {
        return postOnly;
    }



    public void setBirthdateParameter(String birthdateParameter) {
        this.birthdateParameter = birthdateParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }

    public void setPinpadParameter(String pinpadParameter) {
        this.pinpadParameter = pinpadParameter;
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    private String obtainBirthdate(HttpServletRequest request) {
        return request.getParameter(birthdateParameter);
    }




    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                    + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        getRememberMeServices().loginSuccess(request, response, authResult);
        getProfileRememberService().loginSuccess(request,response,authResult);

        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                    authResult, this.getClass()));
        }

        getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    public IProfileRememberService getProfileRememberService() {
        return profileRememberService;
    }

    public void setProfileRememberService(IProfileRememberService profileRememberService) {
        this.profileRememberService = profileRememberService;
    }


}
