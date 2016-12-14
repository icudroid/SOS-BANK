package net.dkahn.starter.authentication.provider.service;

import net.dkahn.starter.authentication.provider.ProfileLoginAuthenticationException;
import net.dkahn.starter.domains.security.token.ProfileToken;
import net.dkahn.starter.services.security.IProfileTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by dev on 29/11/16.
 */
public class ProfileRememberService implements IProfileRememberService{

    private static final Logger logger = LoggerFactory.getLogger(ProfileRememberService.class);

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private static final int TWO_WEEKS_S = 1209600;

    private static final String DELIMITER = ":";


    private static final String SPRING_SECURITY_PROFILE_LOGIN_COOKIE_KEY = "profile-token";

    private static final String DEFAULT_PARAMETER = "remember-profile";

    private String cookieName = SPRING_SECURITY_PROFILE_LOGIN_COOKIE_KEY;
    private String cookieDomain;
    private String parameter = DEFAULT_PARAMETER;
    private int tokenValiditySeconds = TWO_WEEKS_S;
    private Boolean useSecureCookie = null;


    private Method setHttpOnlyMethod;


    private IProfileTokenService profileTokenService;
    private UserDetailsService userDetailsService;


    @Override
    public void loginFail(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {

        if (!rememberMeRequested(request, parameter)) {
            logger.debug("Remember-me login not requested.");
            return;
        }

        String username = successfulAuthentication.getName();

        logger.debug("Creating new persistent profile remember for user " + username);

        try {
            addCookie(profileTokenService.generateToken(username), request, response);
        }
        catch (Exception e) {
            logger.error("Failed to save persistent token profile remember ", e);
        }
    }

    @Override
    public String getUsername(HttpServletRequest request) {
        String rememberMeCookie = extractRememberMeCookie(request);

        if (rememberMeCookie == null) {
            return null;
        }

        logger.debug("Remember-me cookie detected");

        if (rememberMeCookie.length() == 0) {
            logger.debug("Cookie was empty");
            return null;
        }

        String[] cookieTokens = decodeCookie(rememberMeCookie);


        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2
                    + " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        ProfileToken token = profileTokenService.findByTokenAndId(presentedToken,presentedSeries);
        if(token!=null){
            return token.getUsername();
        }else{
            return null;
        }

    }

    @Override
    public void unvalidateToken(HttpServletRequest request, HttpServletResponse response) {
        String username = getUsername(request);
        if(username !=null){
            profileTokenService.removeUserTokens(username);
            cancelCookie(request,response);
        }
    }


    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {

        String paramValue = request.getParameter(parameter);

        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
                    || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                return true;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Did not send remember-me cookie (principal did not set parameter '"
                    + parameter + "')");
        }

        return false;
    }



    public String extractRememberMeCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if ((cookies == null) || (cookies.length == 0)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }



    public String[] decodeCookie(String cookieValue) throws InvalidCookieException {
        for (int j = 0; j < cookieValue.length() % 4; j++) {
            cookieValue = cookieValue + "=";
        }

        if (!Base64.isBase64(cookieValue.getBytes())) {
            throw new InvalidCookieException(
                    "Cookie token was not Base64 encoded; value was '" + cookieValue
                            + "'");
        }

        String cookieAsPlainText = new String(Base64.decode(cookieValue.getBytes()));

        String[] tokens = StringUtils.delimitedListToStringArray(cookieAsPlainText,
                DELIMITER);

        if ((tokens[0].equalsIgnoreCase("http") || tokens[0].equalsIgnoreCase("https"))
                && tokens[1].startsWith("//")) {
            // Assume we've accidentally split a URL (OpenID identifier)
            String[] newTokens = new String[tokens.length - 1];
            newTokens[0] = tokens[0] + ":" + tokens[1];
            System.arraycopy(tokens, 2, newTokens, 1, newTokens.length - 1);
            tokens = newTokens;
        }

        return tokens;
    }



    /**
     * Sets a "cancel cookie" (with maxAge = 0) on the response to disable persistent
     * logins.
     */
    public void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("Cancelling cookie");
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath(getCookiePath(request));
        if (cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }
        response.addCookie(cookie);
    }



    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }


    private void addCookie(ProfileToken token, HttpServletRequest request,
                           HttpServletResponse response) {
        setCookie(new String[] { token.getId(), token.getToken() },
                getTokenValiditySeconds(), request, response);
    }


    /**
     * Sets the cookie on the response.
     *
     * By default a secure cookie will be used if the connection is secure. You can set
     * the {@code useSecureCookie} property to {@code false} to override this. If you set
     * it to {@code true}, the cookie will always be flagged as secure. If Servlet 3.0 is
     * used, the cookie will be marked as HttpOnly.
     *
     * @param tokens the tokens which will be encoded to make the cookie value.
     * @param maxAge the value passed to {@link Cookie#setMaxAge(int)}
     * @param request the request
     * @param response the response to add the cookie to.
     */
    private void setCookie(String[] tokens, int maxAge, HttpServletRequest request,
                           HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setPath(getCookiePath(request));
        if (cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }
        if (maxAge < 1) {
            cookie.setVersion(1);
        }

        if (useSecureCookie == null) {
            cookie.setSecure(request.isSecure());
        }
        else {
            cookie.setSecure(useSecureCookie);
        }

        if (setHttpOnlyMethod != null) {
            ReflectionUtils.invokeMethod(setHttpOnlyMethod, cookie, Boolean.TRUE);
        }
        else if (logger.isDebugEnabled()) {
            logger.debug("Note: Cookie will not be marked as HttpOnly because you are not using Servlet 3.0 (Cookie#setHttpOnly(boolean) was not found).");
        }

        response.addCookie(cookie);
    }

    /**
     * Inverse operation of decodeCookie.
     *
     * @param cookieTokens the tokens to be encoded.
     * @return base64 encoding of the tokens concatenated with the ":" delimiter.
     */
    private String encodeCookie(String[] cookieTokens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cookieTokens.length; i++) {
            sb.append(cookieTokens[i]);

            if (i < cookieTokens.length - 1) {
                sb.append(DELIMITER);
            }
        }

        String value = sb.toString();

        sb = new StringBuilder(new String(Base64.encode(value.getBytes())));

        while (sb.charAt(sb.length() - 1) == '=') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }


    public UserDetails processAutoLoginCookie(String[] cookieTokens,

                                              HttpServletRequest request, HttpServletResponse response) {

        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2
                    + " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        ProfileToken token = profileTokenService.get(presentedSeries);

        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new ProfileLoginAuthenticationException(
                    "No persistent token found for series id: " + presentedSeries);
        }

        // We have a match for this user/series combination
        if (!presentedToken.equals(token.getToken())) {
            // Token doesn't match series value. Delete all logins for this user and throw
            // an exception to warn them.
            profileTokenService.removeUserTokens(token.getUsername());

            throw new CookieTheftException(
                    messages.getMessage(
                            "PersistentTokenBasedRememberMeServices.cookieStolen",
                            "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
        }

        LocalDateTime date = token.getCreationDate();
        if(token.getUpdateDate()!=null){
            date = token.getUpdateDate();
        }

        if (date.plusSeconds(getTokenValiditySeconds()).isBefore(LocalDateTime.now())) {
            throw new ProfileLoginAuthenticationException("Profile login has expired");
        }

        // Token also matches, so login is valid. Update the token value, keeping the
        // *same* series number.
        if (logger.isDebugEnabled()) {
            logger.debug("Refreshing persistent login token for user '"
                    + token.getUsername() + "', series '" + token.getId() + "'");
        }

        try {
            token = profileTokenService.updateToken(token);
            addCookie(token, request, response);
        } catch (Exception e) {
            logger.error("Failed to update token: ", e);
            throw new ProfileRememberAuthenticationException(
                    "Autologin failed due to data access problem");
        }

        return getUserDetailsService().loadUserByUsername(token.getUsername());
    }


    public IProfileTokenService getProfileTokenService() {
        return profileTokenService;
    }


    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    private int getTokenValiditySeconds() {
        return tokenValiditySeconds;
    }

    public void setTokenValiditySeconds(int tokenValiditySeconds) {
        this.tokenValiditySeconds = tokenValiditySeconds;
    }

    public Boolean getUseSecureCookie() {
        return useSecureCookie;
    }

    public void setUseSecureCookie(Boolean useSecureCookie) {
        this.useSecureCookie = useSecureCookie;
    }

    public void setProfileTokenService(IProfileTokenService profileTokenService) {
        this.profileTokenService = profileTokenService;
    }



    public Method getSetHttpOnlyMethod() {
        return setHttpOnlyMethod;
    }

    public void setSetHttpOnlyMethod(Method setHttpOnlyMethod) {
        this.setHttpOnlyMethod = setHttpOnlyMethod;
    }

    private UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }






}
