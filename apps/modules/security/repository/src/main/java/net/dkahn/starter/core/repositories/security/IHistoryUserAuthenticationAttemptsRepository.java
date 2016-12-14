package net.dkahn.starter.core.repositories.security;

import net.dkahn.starter.domains.security.HistoryUserAuthenticationAttempts;
import net.dkahn.starter.tools.repository.IGenericRepository;

import java.util.List;

/**
 * History
 */
public interface IHistoryUserAuthenticationAttemptsRepository extends IGenericRepository<HistoryUserAuthenticationAttempts,Long> {

    List<HistoryUserAuthenticationAttempts> findByUsername(String login);
}
