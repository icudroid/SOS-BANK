package net.dkahn.starter.services.security.impl;

import net.dkahn.starter.domains.security.token.ProfileToken;
import net.dkahn.starter.services.security.IProfileTokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan(basePackages = {"net.dkahn.starter"})
@ComponentScan
public class ProfileTokenServiceTest {
    @Autowired
    private IProfileTokenService profileTokenService;

    @Test
    public void generateToken() throws Exception {
        ProfileToken profileToken = profileTokenService.generateToken("100");
        ProfileToken profileTokenDB = profileTokenService.get(profileToken.getId());
        assertThat(profileToken.getId()).isEqualTo(profileTokenDB.getId());
        assertThat(profileToken.getUsername()).isEqualTo(profileTokenDB.getUsername());
        assertThat(profileToken.getToken()).isEqualTo(profileTokenDB.getToken());
        assertThat(profileToken.getCreationDate()).isNotNull().isEqualTo(profileTokenDB.getCreationDate());
        assertThat(profileToken.getUpdateDate()).isNull();
    }

    @Test
    public void updateToken() throws Exception {
        ProfileToken profileToken = profileTokenService.generateToken("100");
        String token = profileToken.getToken();
        ProfileToken profileTokenDB = profileTokenService.updateToken(profileToken);

        assertThat(profileToken.getId()).isEqualTo(profileTokenDB.getId());
        assertThat(profileToken.getUsername()).isEqualTo(profileTokenDB.getUsername());
        assertThat(token).isNotEqualTo(profileTokenDB.getToken());
        assertThat(profileToken.getCreationDate()).isNotNull().isEqualTo(profileTokenDB.getCreationDate());
        assertThat(profileTokenDB.getUpdateDate()).isNotNull();
    }

    @Test
    public void findByTokenAndId() throws Exception {
        ProfileToken profileToken = profileTokenService.generateToken("100");
        ProfileToken profileTokenDB = profileTokenService.findByTokenAndId(profileToken.getToken(),profileToken.getId());
        assertThat(profileToken.getId()).isEqualTo(profileTokenDB.getId());
        assertThat(profileToken.getUsername()).isEqualTo(profileTokenDB.getUsername());
        assertThat(profileToken.getToken()).isEqualTo(profileTokenDB.getToken());
        assertThat(profileToken.getCreationDate()).isNotNull().isEqualTo(profileTokenDB.getCreationDate());
        assertThat(profileToken.getUpdateDate()).isNull();
    }

    @Test
    public void deleteAllUserToken(){
        ProfileToken profileToken = profileTokenService.generateToken("100");
        profileTokenService.removeUserTokens("100");

        assertThat(profileTokenService.get(profileToken.getId())).isNull();
    }

}