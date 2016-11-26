package net.dkahn.starter.services.security.impl;

import net.dkahn.starter.core.repositories.security.IHistoryUserAuthenticationAttemptsRepository;
import net.dkahn.starter.core.repositories.security.IUserAuthenticationAttemptsRepository;
import net.dkahn.starter.core.repositories.security.IUserRepository;
import net.dkahn.starter.domains.security.AuthenticationStatus;
import net.dkahn.starter.domains.security.HistoryUserAuthenticationAttempts;
import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.domains.security.UserAuthenticationAttempts;
import net.dkahn.starter.services.security.IUserService;
import net.dkahn.starter.services.security.exception.RestAuthenticationException;
import net.dkahn.starter.tools.service.impl.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * User service
 */
@Service
public class UserService extends GenericServiceImpl<User, Long> implements IUserService {

    private static final int MAX_ATTEMPTS = 3;

    private final long lockDuration;
    private final IUserAuthenticationAttemptsRepository attemptsRepository;
    private final IHistoryUserAuthenticationAttemptsRepository authenticationAttemptsRepository;

    public UserService(   IUserAuthenticationAttemptsRepository attemptsRepository
                        , IUserRepository repository
                        , IHistoryUserAuthenticationAttemptsRepository authenticationAttemptsRepository
                        , @Value("${user.lock.duration:30}") long lockDuration
    ) {
        this.attemptsRepository = attemptsRepository;
        this.lockDuration = lockDuration;
        this.authenticationAttemptsRepository = authenticationAttemptsRepository;
        this.repository = repository;
    }

    @Transactional
    @Override
    public void loginFailure(String username) {
        UserAuthenticationAttempts attempts = attemptsRepository.findByUsername(username);
        User user = ((IUserRepository) repository).findByLoginEnabled(username);
        if (attempts == null) {
            attempts = new UserAuthenticationAttempts();
            attempts.setUser(user);
            attempts.setAttempts(0);
            attempts = attemptsRepository.save(attempts);
        }
        attempts.increment();


        HistoryUserAuthenticationAttempts history = new HistoryUserAuthenticationAttempts();
        history.setUser(user);

        if (attempts.getAttempts() >= MAX_ATTEMPTS) {
            user.setBlocked(true);
            history.setStatus(AuthenticationStatus.BLOCKED);
        } else {
            history.setStatus(AuthenticationStatus.WRONG);
        }

        authenticationAttemptsRepository.save(history);

    }

    @Transactional
    @Override
    public void loginSuccess(String username) {
        UserAuthenticationAttempts attempts = attemptsRepository.findByUsername(username);
        User user = ((IUserRepository) repository).findByLoginEnabled(username);
        if (attempts == null) {
            attempts = new UserAuthenticationAttempts();
            attempts.setUser(user);
            attempts.setAttempts(0);
            attemptsRepository.save(attempts);
        }

        attempts.setAttempts(0);

        HistoryUserAuthenticationAttempts history = new HistoryUserAuthenticationAttempts();
        history.setUser(user);
        history.setStatus(AuthenticationStatus.OK);
        authenticationAttemptsRepository.save(history);
    }

    @Transactional
    @Override
    public boolean checkLockedTimeExpired(String username) throws RestAuthenticationException{
        UserAuthenticationAttempts attempts = attemptsRepository.findByUsername(username);
        if (attempts.getAttempts() >= MAX_ATTEMPTS) {
            LocalDateTime expiration = attempts.getCreationDate().plusMinutes(lockDuration);

            if (expiration.isBefore(LocalDateTime.now())) {
                attempts.setAttempts(0);
                return true;
            }

            throw new RestAuthenticationException("Account locked yet",attempts.getAttempts(),"account.locket.yet",expiration);

        }
        return true;
    }



}
