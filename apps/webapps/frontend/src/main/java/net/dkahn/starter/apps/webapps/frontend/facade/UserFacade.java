package net.dkahn.starter.apps.webapps.frontend.facade;

import net.dkahn.starter.core.repositories.security.impl.UserRepository;
import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.services.security.model.DefaultUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: dimitri
 * Date: 10/02/15
 * Time: 16:30
 * Goal:
 */
@Service
public class UserFacade {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User getCurrentUser() {
        Object principal = getAuthenticationUser().getPrincipal();
        if (!(principal instanceof User)) {
            throw new AssertionError("Please check configuration. Should be User in the principal.");
        }
        return userRepository.get(((DefaultUserDetails) principal).getId());
    }

    protected Authentication getAuthenticationUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public DefaultUserDetails getTop3UserDetails() {
        return (DefaultUserDetails) getAuthenticationUser().getPrincipal();
    }

}
