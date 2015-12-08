package net.dkahn.starter.apps.webapps.modules.security.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class PermissionRightDto implements Serializable{
    private String permission;
    private List<RightDto> rights;
}
