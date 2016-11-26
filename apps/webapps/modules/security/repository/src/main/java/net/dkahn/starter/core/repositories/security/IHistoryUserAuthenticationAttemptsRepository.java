package net.dkahn.starter.core.repositories.security;

import net.dkahn.starter.domains.security.HistoryUserAuthenticationAttempts;
import net.dkahn.starter.domains.security.UserAuthenticationAttempts;
import net.dkahn.starter.tools.repository.IGenericRepository;

/**
 * Created by dev on 25/11/16.
 */
public interface IHistoryUserAuthenticationAttemptsRepository extends IGenericRepository<HistoryUserAuthenticationAttempts,Long> {

}
