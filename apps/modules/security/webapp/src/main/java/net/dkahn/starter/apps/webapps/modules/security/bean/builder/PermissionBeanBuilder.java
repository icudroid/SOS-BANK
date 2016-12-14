package net.dkahn.starter.apps.webapps.modules.security.bean.builder;


import net.dkahn.starter.apps.webapps.modules.security.bean.PermissionBean;
import net.dkahn.starter.domains.security.Permission;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 20/01/15
 * Time: 14:11
 * Goal:
 */
public class PermissionBeanBuilder {


    public static List<PermissionBean> build(List<Permission> permissions){
        return  permissions
                    .stream()
                    .map(PermissionBeanBuilder::build)
                    .collect(Collectors.toList());
    }

    public static PermissionBean build(Permission permission){
        return
                PermissionBean.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .right(permission.getRight())
                        .description(permission.getDescription())
                        .build();
    }

}
