package net.dkahn.starter.apps.webapps.modules.security.command;

import net.dkahn.starter.apps.webapps.common.command.datatable.IFilteringCommand;
import net.dkahn.starter.core.repositories.security.filtering.PermissionFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionCommand implements IFilteringCommand<PermissionFilter> {

    private Integer id;
    private String right;
    private String permission;
    private String description;
    private Boolean attachedRole;

    @Override
    public PermissionFilter toFilter() {
        return PermissionFilter.builder()
                .id(id)
                .description(description)
                .permission(permission)
                .right(right)
                .attachedRole(attachedRole)
                .build();
    }
}

