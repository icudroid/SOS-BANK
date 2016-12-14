package net.dkahn.starter.apps.webapps.modules.security.bean.builder;

import net.dkahn.starter.apps.webapps.modules.security.bean.PermissionDTO;
import net.dkahn.starter.apps.webapps.common.utils.datatable.IBeanBuilder;
import net.dkahn.starter.domains.security.Permission;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionDTOBuilder implements IBeanBuilder<Permission,PermissionDTO> {


    public List<PermissionDTO> build(List<Permission> permissions){
        return  permissions
                    .stream()
                    .map(this::build)
                    .collect(Collectors.toList());
    }

    public PermissionDTO build(Permission permission){
        return
                PermissionDTO.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .right(permission.getRight())
                        .description(permission.getDescription())
                        .roles(RoleBeanBuilder.build(permission.getRoles()))
                        .build();
    }

}
