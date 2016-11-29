package net.dkahn.starter.core.repositories.security;


import net.dkahn.starter.domains.security.token.ProfileToken;
import net.dkahn.starter.tools.repository.IGenericRepository;

public interface IProfileTokenRepository extends IGenericRepository<ProfileToken,String> {

    ProfileToken findByTokenAndId(String token, String id);

    Long removeUserTokens(String username);
}
