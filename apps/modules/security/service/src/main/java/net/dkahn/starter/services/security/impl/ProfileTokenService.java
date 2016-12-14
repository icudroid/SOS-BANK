package net.dkahn.starter.services.security.impl;

import net.dkahn.starter.core.repositories.security.IProfileTokenRepository;
import net.dkahn.starter.domains.security.token.ProfileToken;
import net.dkahn.starter.services.security.IProfileTokenService;
import net.dkahn.starter.tools.logger.Log;
import net.dkahn.starter.tools.service.impl.GenericServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;

@Service
public class ProfileTokenService extends GenericServiceImpl<ProfileToken, String> implements IProfileTokenService {

    @Log
    private Logger log = null;

    private SecureRandom random;

    private int tokenLength;
    private int seriesLength;

    public ProfileTokenService(IProfileTokenRepository repository
            , @Value("${security.token.profile.tokenLength:16}") int tokenLength
            , @Value("${security.token.profile.seriesLength:16}") int seriesLength
   ){
        this.repository = repository;
        random = new SecureRandom();
        this.tokenLength = tokenLength;
        this.seriesLength = seriesLength;
    }

    @Transactional
    @Override
    public ProfileToken generateToken(String username){
        ProfileToken buildToken = ProfileToken.builder()
                .id(generateSeriesData())
                .token(generateTokenData())
                .username(username)
                .build();
        log.debug("Generation du token pour username : {}",username);
        return repository.save(buildToken);
    }

    @Transactional
    @Override
    public ProfileToken updateToken(ProfileToken profileToken){
        log.debug("Update du token pour username : {}",profileToken.getUsername());
        profileToken.setToken(generateTokenData());
        return repository.save(profileToken);
    }


    @Transactional
    @Override
    public ProfileToken findByTokenAndId(String token, String id){
        log.debug("Find du token pour token {} et id {}",token,id);
        return ((IProfileTokenRepository)repository).findByTokenAndId(token,id);
    }

    @Transactional
    @Override
    public Long removeUserTokens(String username) {
        return ((IProfileTokenRepository)repository).removeUserTokens(username);
    }


    private String generateSeriesData() {
        byte[] newSeries = new byte[seriesLength];
        random.nextBytes(newSeries);
        return new String(Base64.encode(newSeries));
    }

    private String generateTokenData() {
        byte[] newToken = new byte[tokenLength];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }

}
