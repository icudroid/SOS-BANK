package net.dkahn.starter.services.security;

import net.dkahn.starter.domains.security.token.ProfileToken;
import net.dkahn.starter.tools.service.GenericService;

import javax.transaction.Transactional;

public interface IProfileTokenService extends GenericService<ProfileToken, String> {
    ProfileToken generateToken(String username);
    ProfileToken updateToken(ProfileToken profileToken);
    ProfileToken findByTokenAndId(String token, String id);

    Long removeUserTokens(String username);
}
