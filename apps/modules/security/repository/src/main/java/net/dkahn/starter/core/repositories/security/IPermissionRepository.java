package net.dkahn.starter.core.repositories.security;

import net.dkahn.starter.core.repositories.security.filtering.PermissionFilter;
import net.dkahn.starter.domains.security.Permission;
import net.dkahn.starter.tools.repository.IGenericRepository;
import net.dkahn.starter.tools.repository.pageable.IPageable;

import java.util.List;
import java.util.Map;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:11
 * Goal:
 */
public interface IPermissionRepository extends IGenericRepository<Permission,Integer>, IPageable<Permission, PermissionFilter> {

    Map<String,List<Permission>> findPermissionsByRoleId(Integer roleId);

    Map<String,List<Permission>> findAllPermissions();

}
