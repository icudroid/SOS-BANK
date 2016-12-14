package net.dkahn.starter.core.repositories.security;

import net.dkahn.starter.domains.security.Role;
import net.dkahn.starter.tools.repository.IGenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:11
 * Goal:
 */
public interface IRoleRepository extends IGenericRepository<Role,Integer> {

    List<Role> findAllInId(List<Integer> rolesId);

    boolean existsByName(Integer id, String name);

    Page<Role> findByNameLike(String query, Pageable pageable);
}
