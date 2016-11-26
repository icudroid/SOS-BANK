package net.dkahn.starter.core.repositories.security.impl;

import com.querydsl.jpa.impl.JPAQuery;
import net.dkahn.starter.core.repositories.security.IHistoryUserAuthenticationAttemptsRepository;
import net.dkahn.starter.domains.security.*;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;

/**
 * Created by dev on 25/11/16.
 */
public class HistoryUserAuthenticationAttemptsRepository extends GenericRepositoryJpa<HistoryUserAuthenticationAttempts,Long> implements IHistoryUserAuthenticationAttemptsRepository {

}
