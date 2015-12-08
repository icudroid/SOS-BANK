package net.dkahn.starter.core.repositories.security;


import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.tools.repository.IGenericRepository;

import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:11
 * Goal:
 */
public interface IUserRepository extends IGenericRepository<User,Long> {

    User findByLoginEnabled(String username);

    List<User> findUsersWithProfile(Integer idProfile);

    User findByLogin(String username);
}
