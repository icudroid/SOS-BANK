package net.dkahn.starter.apps.webapps.modules.security.bean.dismantler;


import net.dkahn.starter.apps.webapps.modules.security.bean.PermissionBean;
import net.dkahn.starter.domains.security.Permission;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 20/01/15
 * Time: 11:05
 * Goal:
 */
public class PermissionDismantler {
    public static List<Permission> buildPermissions(List<PermissionBean> permissionBeans) {
        return permissionBeans
                .stream()
                .map(PermissionDismantler::buildPermission)
                .collect(Collectors.toList());
    }


    public static Permission buildPermission(PermissionBean permission) {
        return  Permission.builder()
                .id(permission.getId())
                .name(permission.getName())
                .right(permission.getRight())
                .description(permission.getDescription())
                .build();
    }
}
