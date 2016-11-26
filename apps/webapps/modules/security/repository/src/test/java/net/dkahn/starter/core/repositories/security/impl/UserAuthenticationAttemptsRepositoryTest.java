package net.dkahn.starter.core.repositories.security.impl;

import net.dkahn.starter.core.domains.business.user.Customer;
import net.dkahn.starter.core.repositories.security.IHistoryUserAuthenticationAttemptsRepository;
import net.dkahn.starter.core.repositories.security.IUserRepository;
import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.domains.security.UserAuthenticationAttempts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan(basePackages = {"net.dkahn.starter"})
@ComponentScan
public class UserAuthenticationAttemptsRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserAuthenticationAttemptsRepository attemptsRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;



    private UserAuthenticationAttempts createAttempts(User user) {
        UserAuthenticationAttempts res = new UserAuthenticationAttempts();
        res.setAttempts(0);
        res.setUser(user);
        res.setId(-1L);
        return res;
    }

    private Customer createCustomer() {
        Customer res = new Customer();
        res.setBlocked(false);
        res.setEnabled(true);
        res.setId(-1L);
        res.setLogin("100");
        res.setPassword(passwordEncoder.encode("000001"));
        return res;
    }

    @Test
    @Transactional
    public void findByUsername() throws Exception {

        User user = userRepository.save(createCustomer());
        attemptsRepository.save(createAttempts(user));

        String expectedUser = "100";
        UserAuthenticationAttempts userAuthenticationAttempts = attemptsRepository.findByUsername(expectedUser);
        assertThat(userAuthenticationAttempts.getUser().getLogin()).isEqualTo(expectedUser);
        assertThat(userAuthenticationAttempts.getAttempts()).isEqualTo(0);
    }

}