package net.dkahn.starter.services.security;

import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.domains.security.UserAuthenticationAttempts;
import net.dkahn.starter.services.security.exception.RestAuthenticationException;
import net.dkahn.starter.tools.service.GenericService;

import javax.transaction.Transactional;

/**
 * Service pour l'utilisateur
 */
public interface IUserService extends GenericService<User, Long> {

    boolean loginFailure(String username);

    void loginSuccess(String username);

    long getLockDuration();

    boolean checkLockedTimeExpired(String username) throws RestAuthenticationException;

    @Transactional
    UserAuthenticationAttempts findAttempsByUsername(String username) throws RestAuthenticationException;

    User findByUsername(String username);
}
