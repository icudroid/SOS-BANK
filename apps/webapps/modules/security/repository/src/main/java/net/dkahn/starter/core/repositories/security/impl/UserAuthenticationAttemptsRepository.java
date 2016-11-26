package net.dkahn.starter.core.repositories.security.impl;

import com.querydsl.jpa.impl.JPAQuery;
import net.dkahn.starter.core.repositories.security.IUserAuthenticationAttemptsRepository;
import net.dkahn.starter.domains.security.QUser;
import net.dkahn.starter.domains.security.QUserAuthenticationAttempts;
import net.dkahn.starter.domains.security.Role;
import net.dkahn.starter.domains.security.UserAuthenticationAttempts;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;

/**
 * Created by dev on 25/11/16.
 */
public class UserAuthenticationAttemptsRepository extends GenericRepositoryJpa<UserAuthenticationAttempts,Long> implements IUserAuthenticationAttemptsRepository {
    @Override
    public UserAuthenticationAttempts findByUsername(String username) {
        JPAQuery query = new JPAQuery<Role>(getEntityManager());


        QUserAuthenticationAttempts attempts = QUserAuthenticationAttempts.userAuthenticationAttempts;
        QUser user = QUser.user;

        query.select(attempts).from(attempts).join(attempts.user,user).where(user.login.eq(username));
        return (UserAuthenticationAttempts) query.fetchOne();
    }
}
