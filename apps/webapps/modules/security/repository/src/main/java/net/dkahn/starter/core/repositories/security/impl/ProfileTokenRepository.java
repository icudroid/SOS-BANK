package net.dkahn.starter.core.repositories.security.impl;

import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import net.dkahn.starter.core.repositories.security.IProfileTokenRepository;
import net.dkahn.starter.domains.security.token.ProfileToken;
import net.dkahn.starter.domains.security.token.QProfileToken;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;
import org.springframework.stereotype.Repository;


@Repository
public class ProfileTokenRepository extends GenericRepositoryJpa<ProfileToken,String> implements IProfileTokenRepository {


    @Override
    public ProfileToken findByTokenAndId(String token, String id) {
        JPAQuery query = new JPAQuery(getEntityManager());

        QProfileToken profileToken = QProfileToken.profileToken;

        query.select(profileToken).from(profileToken).where(profileToken.token.eq(token).and(profileToken.id.eq(id)));

        return (ProfileToken) query.fetchOne();
    }

    @Override
    public Long removeUserTokens(String username) {
        QProfileToken profileToken = QProfileToken.profileToken;
        return new JPADeleteClause(getEntityManager(), profileToken)
                .where(profileToken.username.eq(username)).execute();
    }
}
