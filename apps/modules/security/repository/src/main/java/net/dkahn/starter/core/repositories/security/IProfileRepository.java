package net.dkahn.starter.core.repositories.security;

import net.dkahn.starter.domains.security.Profile;
import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.tools.repository.IGenericRepository;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:11
 * Goal:
 */
public interface IProfileRepository extends IGenericRepository<Profile,Integer> {

    String ADMIN_PROFILE    = "PROFILE_ADMIN";
    String FRONT_PROFILE    = "PROFILE_FRONT";
    String CUSTOMER_PROFILE    = "PROFILE_CUSTOMER";
    String ANONYMOUS        = "PROFILE_ANONYMOUS";


    List<GrantedAuthority> loadGrantedAuthorities(User user);

    List<GrantedAuthority> loadGrantedAnonymousAuthorities();

    Boolean existsByName(Integer id, String newName);

    List<Profile> findByIds(List<Integer> profileIds);

    Profile findByName(String profileName);
}
