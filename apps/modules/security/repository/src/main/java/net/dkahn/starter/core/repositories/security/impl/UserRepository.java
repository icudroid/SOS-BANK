package net.dkahn.starter.core.repositories.security.impl;

import net.dkahn.starter.core.repositories.security.IUserRepository;
import net.dkahn.starter.domains.security.QProfile;
import net.dkahn.starter.domains.security.QUser;
import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:12
 * Goal:
 */
@Repository
public class UserRepository extends GenericRepositoryJpa<User,Long> implements IUserRepository {


    @Override
    public User findByLoginEnabled(String username) {
        JPAQuery query = createQuery();

        QUser user = QUser.user;

        query.select(user).from(user).where(user.login.eq(username).and(user.enabled.eq(true)));

        return (User) query.fetchOne();
    }

    @Override
    public List<User> findUsersWithProfile(Integer idProfile) {
        JPAQuery query = createQuery();

        QUser user = QUser.user;
        QProfile profile = QProfile.profile;

        query.select(user).from(user).join(user.profiles,profile).where(profile.id.eq(idProfile));

        return query.fetch();

    }

    @Override
    public User findByLogin(String username) {
        JPAQuery query = createQuery();

        QUser user = QUser.user;

        query.select(user).from(user).where(user.login.eq(username));

        return (User) query.fetchOne();
    }





}
