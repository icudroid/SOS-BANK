package net.dkahn.starter.core.repositories.security.impl;

import com.querydsl.jpa.impl.JPAQuery;
import net.dkahn.starter.core.repositories.security.IHistoryUserAuthenticationAttemptsRepository;
import net.dkahn.starter.domains.security.*;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dev on 25/11/16.
 */
@Repository
public class HistoryUserAuthenticationAttemptsRepository extends GenericRepositoryJpa<HistoryUserAuthenticationAttempts,Long> implements IHistoryUserAuthenticationAttemptsRepository {

    @Override
    public List<HistoryUserAuthenticationAttempts> findByUsername(String username) {
        JPAQuery query = new JPAQuery<Role>(getEntityManager());


        QHistoryUserAuthenticationAttempts history = QHistoryUserAuthenticationAttempts.historyUserAuthenticationAttempts;
        QUser user = QUser.user;

        query.select(history).from(history).join(history.user,user).where(user.login.eq(username));
        return query.fetch();
    }
}
