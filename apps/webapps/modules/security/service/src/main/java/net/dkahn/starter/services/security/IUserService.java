package net.dkahn.starter.services.security;

import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.services.security.exception.RestAuthenticationException;
import net.dkahn.starter.tools.service.GenericService;

/**
 * Service pour l'utilisateur
 */
public interface IUserService extends GenericService<User, Long> {

    void loginFailure(String username);

    void loginSuccess(String username);

    boolean checkLockedTimeExpired(String username) throws RestAuthenticationException;
}
