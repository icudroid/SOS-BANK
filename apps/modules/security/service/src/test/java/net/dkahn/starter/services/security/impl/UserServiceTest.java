package net.dkahn.starter.services.security.impl;

import net.dkahn.starter.domains.security.user.Customer;
import net.dkahn.starter.core.repositories.security.IHistoryUserAuthenticationAttemptsRepository;
import net.dkahn.starter.core.repositories.security.IUserAuthenticationAttemptsRepository;
import net.dkahn.starter.core.repositories.security.IUserRepository;
import net.dkahn.starter.domains.security.AuthenticationStatus;
import net.dkahn.starter.domains.security.HistoryUserAuthenticationAttempts;
import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.domains.security.UserAuthenticationAttempts;
import net.dkahn.starter.services.security.IUserService;
import net.dkahn.starter.services.security.exception.RestAuthenticationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan(basePackages = {"net.dkahn.starter"})
@ComponentScan
public class UserServiceTest {
    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserAuthenticationAttemptsRepository attemptsRepository;


    @Autowired
    private IHistoryUserAuthenticationAttemptsRepository authenticationAttemptsRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;


    private Customer createCustomer() {
        Customer res = new Customer();
        res.setBlocked(false);
        res.setEnabled(true);
        res.setId(-1L);
        res.setLogin("100");
        res.setPassword(passwordEncoder.encode("000001"));
        return res;
    }


    @Transactional
    @Test
    public void loginFailureNoAttemps() throws Exception {
        User user = userRepository.save(createCustomer());

        userService.loginFailure(user.getLogin());
        UserAuthenticationAttempts attempts = attemptsRepository.findByUsername(user.getLogin());

        assertThat(attempts.getAttempts()).isEqualTo(1);

        List<HistoryUserAuthenticationAttempts> history = authenticationAttemptsRepository.findByUsername(user.getLogin());

        assertThat(history).size().isEqualTo(1);

        assertThat(history.get(0).getStatus()).isEqualTo(AuthenticationStatus.WRONG);
    }


    @Transactional
    @Test
    public void loginFailureLockUser() throws Exception {
        User user = userRepository.save(createCustomer());

        userService.loginFailure(user.getLogin());
        userService.loginFailure(user.getLogin());
        assertThat(userRepository.get(user.getId()).getBlocked()).isEqualTo(false);

        userService.loginFailure(user.getLogin());

        UserAuthenticationAttempts attempts = attemptsRepository.findByUsername(user.getLogin());

        assertThat(attempts.getAttempts()).isEqualTo(3);

        List<HistoryUserAuthenticationAttempts> history = authenticationAttemptsRepository.findByUsername(user.getLogin());

        assertThat(history).size().isEqualTo(3);

        assertThat(history.get(0).getStatus()).isEqualTo(AuthenticationStatus.WRONG);

        assertThat(userRepository.get(user.getId()).getBlocked()).isEqualTo(true);
    }


    @Transactional
    @Test
    public void loginFailureAndSuccess() throws Exception {
        User user = userRepository.save(createCustomer());

        userService.loginFailure(user.getLogin());
        userService.loginSuccess(user.getLogin());
        UserAuthenticationAttempts attempts = attemptsRepository.findByUsername(user.getLogin());
        assertThat(attempts.getAttempts()).isEqualTo(0);

        List<HistoryUserAuthenticationAttempts> history = authenticationAttemptsRepository.findByUsername(user.getLogin());
        assertThat(history.get(1).getStatus()).isEqualTo(AuthenticationStatus.OK);
    }


    @Transactional
    @Test
    public void loginSuccess() throws Exception {
        User user = userRepository.save(createCustomer());

        userService.loginSuccess(user.getLogin());
        UserAuthenticationAttempts attempts = attemptsRepository.findByUsername(user.getLogin());
        assertThat(attempts.getAttempts()).isEqualTo(0);

        List<HistoryUserAuthenticationAttempts> history = authenticationAttemptsRepository.findByUsername(user.getLogin());
        assertThat(history.get(0).getStatus()).isEqualTo(AuthenticationStatus.OK);
    }


    @Transactional
    @Test
    public void checkLockedTimeExpiredNot() throws Exception {
        User user = userRepository.save(createCustomer());
        userService.loginFailure(user.getLogin());
        userService.loginFailure(user.getLogin());
        userService.loginFailure(user.getLogin());

        try {
            userService.checkLockedTimeExpired(user.getLogin());
        }catch (RestAuthenticationException e){
            return;
        }

        throw new Exception("Le Compte n'est pas bloquer");
    }


    @Transactional
    @Test
    public void checkLockedTimeExpired() throws Exception {
        User user = userRepository.save(createCustomer());
        userService.loginFailure(user.getLogin());
        userService.loginFailure(user.getLogin());
        userService.loginSuccess(user.getLogin());

         assertThat(userService.checkLockedTimeExpired(user.getLogin())).isEqualTo(true);


    }

}
