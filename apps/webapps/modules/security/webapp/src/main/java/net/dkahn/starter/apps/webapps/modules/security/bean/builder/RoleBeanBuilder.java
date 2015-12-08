package net.dkahn.starter.apps.webapps.modules.security.bean.builder;


import net.dkahn.starter.apps.webapps.modules.security.bean.RoleBean;
import net.dkahn.starter.domains.security.Role;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 20/01/15
 * Time: 11:03
 * Goal:
 */
public class RoleBeanBuilder {

    public static List<RoleBean> build(List<Role> roles) {
        return  roles
                    .stream()
                    .map(RoleBeanBuilder::build)
                    .collect(Collectors.toList());
    }

    public static RoleBean build(Role role) {
        return
                RoleBean.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .permissions(PermissionBeanBuilder.build(role.getPermissions()))
                        .build();
    }
}
