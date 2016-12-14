package net.dkahn.starter.apps.webapps.modules.security.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPermissionToRoleCommand {
    private Integer permissionId;
    private Integer roleId;
}
