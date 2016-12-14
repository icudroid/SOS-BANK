package net.dkahn.starter.apps.webapps.modules.security.bean.dismantler;


import net.dkahn.starter.apps.webapps.modules.security.bean.RoleBean;
import net.dkahn.starter.domains.security.Role;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 20/01/15
 * Time: 11:06
 * Goal:
 */
public class RoleDismantler {

    public static Role buildRole(RoleBean role) {
        return Role.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(PermissionDismantler.buildPermissions(role.getPermissions()))
                .build();
    }



    public static List<Role> buildRoles(List<RoleBean> roles) {
        return roles
                .stream()
                .map(RoleDismantler::buildRole)
                .collect(Collectors.toList());
    }
}
