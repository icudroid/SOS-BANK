package net.dkahn.starter.authentication.provider;

import net.dkahn.starter.authentication.RestUser;
import net.dkahn.starter.domains.security.UserAuthenticationAttempts;
import net.dkahn.starter.services.security.IPinpadService;
import net.dkahn.starter.services.security.IUserService;
import net.dkahn.starter.services.security.exception.PinpadExpiredException;
import net.dkahn.starter.services.security.exception.RestAuthenticationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * Provider pour spring security
 */
public class PinpadAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    // ~ Static fields/initializers
    // =====================================================================================

    /**
     * The plaintext password used to perform
     * {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when the user is
     * not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    // ~ Instance fields
    // ================================================================================================

    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform
     * {@link PasswordEncoder#isPasswordValid(String, String, Object)} on when the user is
     * not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private String userNotFoundEncodedPassword;

    private SaltSource saltSource;

    private UserDetailsService userDetailsService;
    private IPinpadService pindpadService;
    private IUserService userService;

    public PinpadAuthenticationProvider() {
        setPasswordEncoder(new PlaintextPasswordEncoder());
    }

    // ~ Methods
    // ========================================================================================================

    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        Object salt = null;

        if (this.saltSource != null) {
            salt = this.saltSource.getSalt(userDetails);
        }

        CredentialPinpad credentials = (CredentialPinpad) authentication.getCredentials();
        if (credentials == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        try {

            if(!credentials.getBirthdate().equals(((RestUser)userDetails).getBirthdate())){
                failureLogin(userDetails);
            }

            String presentedPassword = pindpadService.decodePassword(credentials.getPindpadId(), credentials.getPassword());

            pindpadService.remove(credentials.getPindpadId());

            if (!passwordEncoder.isPasswordValid(userDetails.getPassword(),
                    presentedPassword, salt)) {
                logger.debug("Authentication failed: password does not match stored value");

                failureLogin(userDetails);
                return;
/*
                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
*/
            }

            userService.loginSuccess(userDetails.getUsername());

        } catch (JpaObjectRetrievalFailureException pinpadNotFound){
                throw new BadCredentialsException("Pindpad not found");
        } catch (PinpadExpiredException e) {
            throw new BadCredentialsException("Pindpad expired");
        }

    }

    private void failureLogin(UserDetails userDetails) {
        if(userService.loginFailure(userDetails.getUsername())){
            UserAuthenticationAttempts attemps = userService.findAttempsByUsername(userDetails.getUsername());
            LocalDateTime expiration = attemps.getCreationDate().plusMinutes(userService.getLockDuration());
            throw new RestAuthenticationException("Bad credentials",3,"account.locked",expiration);
        }
        UserAuthenticationAttempts attemps = userService.findAttempsByUsername(userDetails.getUsername());
        throw new RestAuthenticationException("Bad credentials",attemps.getAttempts(),"password.retry.err",null);
    }

    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    protected final UserDetails retrieveUser(String username,
                                             UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        UserDetails loadedUser;

        try {
            loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if(!loadedUser.isAccountNonLocked()){
                if(!userService.checkLockedTimeExpired(loadedUser.getUsername())){
                    UserAuthenticationAttempts attemps = userService.findAttempsByUsername(loadedUser.getUsername());
                    LocalDateTime expiration = attemps.getCreationDate().plusMinutes(userService.getLockDuration());
                    throw new RestAuthenticationException("Account locked",attemps.getAttempts(),"account.locked",expiration);
                }
            }

        }
        catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                CredentialPinpad credentials = (CredentialPinpad) authentication.getCredentials();
                try {
                    String presentedPassword = pindpadService.decodePassword(credentials.getPindpadId(), credentials.getPassword());
                    passwordEncoder.isPasswordValid(userNotFoundEncodedPassword,
                            presentedPassword, null);
                    pindpadService.remove(credentials.getPindpadId());
                } catch (JpaObjectRetrievalFailureException pinpadNotFound){
                    throw new BadCredentialsException("Pindpad not found");
                } catch (PinpadExpiredException e) {
                    throw new BadCredentialsException("Pindpad expired");
                }
            }
            throw notFound;
        }
        catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(
                    repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared as plain text.
     * <p>
     * For systems which are already using salted password which are encoded with a
     * previous release, the encoder should be of type
     * {@code org.springframework.security.authentication.encoding.PasswordEncoder}.
     * Otherwise, the recommended approach is to use
     * {@code org.springframework.security.crypto.password.PasswordEncoder}.
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     * types.
     */
    public void setPasswordEncoder(Object passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        if (passwordEncoder instanceof PasswordEncoder) {
            setPasswordEncoder((PasswordEncoder) passwordEncoder);
            return;
        }

        if (passwordEncoder instanceof org.springframework.security.crypto.password.PasswordEncoder) {
            final org.springframework.security.crypto.password.PasswordEncoder delegate = (org.springframework.security.crypto.password.PasswordEncoder) passwordEncoder;
            setPasswordEncoder(new PasswordEncoder() {
                public String encodePassword(String rawPass, Object salt) {
                    checkSalt(salt);
                    return delegate.encode(rawPass);
                }

                public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
                    checkSalt(salt);
                    return delegate.matches(rawPass, encPass);
                }

                private void checkSalt(Object salt) {
                    Assert.isNull(salt,
                            "Salt value must be null when used with crypto module PasswordEncoder");
                }
            });

            return;
        }

        throw new IllegalArgumentException(
                "passwordEncoder must be a PasswordEncoder instance");
    }

    private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        this.userNotFoundEncodedPassword = passwordEncoder.encodePassword(
                USER_NOT_FOUND_PASSWORD, null);
        this.passwordEncoder = passwordEncoder;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * The source of salts to use when decoding passwords. <code>null</code> is a valid
     * value, meaning the <code>DaoAuthenticationProvider</code> will present
     * <code>null</code> to the relevant <code>PasswordEncoder</code>.
     * <p>
     * Instead, it is recommended that you use an encoder which uses a random salt and
     * combines it with the password field. This is the default approach taken in the
     * {@code org.springframework.security.crypto.password} package.
     *
     * @param saltSource to use when attempting to decode passwords via the
     * <code>PasswordEncoder</code>
     */
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    protected SaltSource getSaltSource() {
        return saltSource;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setPinpadService(IPinpadService pindpadService){
        this.pindpadService = pindpadService;
    }

    protected UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }


    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}
