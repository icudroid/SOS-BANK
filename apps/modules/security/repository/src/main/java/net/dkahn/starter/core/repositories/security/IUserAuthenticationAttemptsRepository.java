package net.dkahn.starter.core.repositories.security;

import net.dkahn.starter.domains.security.UserAuthenticationAttempts;
import net.dkahn.starter.tools.repository.IGenericRepository;

/**
 * Created by dev on 25/11/16.
 */
public interface IUserAuthenticationAttemptsRepository extends IGenericRepository<UserAuthenticationAttempts,Long> {
    UserAuthenticationAttempts findByUsername(String username);

}
