package net.dkahn.starter.authentication;

import net.dkahn.starter.core.repositories.security.IProfileRepository;
import net.dkahn.starter.core.repositories.security.IUserRepository;
import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;


@Repository
public class AuthenticationUserService extends GenericRepositoryJpa<User, Long> implements UserDetailsService {

    @Autowired
    private IProfileRepository profileRepository;


    @Autowired
    private IUserRepository userRepository;


    @javax.transaction.Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(username)){
            throw  new IllegalArgumentException("Username must be not null");
        }

        User user = userRepository.findByLoginEnabled(username);

        if(user==null){
            throw new UsernameNotFoundException("User not found : "+username);
        }else {
            if(!user.getProfiles().contains(profileRepository.findByName(IProfileRepository.CUSTOMER_PROFILE))){
                throw new UsernameNotFoundException("user '" + username + "' not found...");
            }
        }

        return new RestUser(user,profileRepository.loadGrantedAuthorities(user));
    }




}

