package net.dkahn.starter.apps.webapps.modules.security.bean;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class PermissionDTO {
    private Integer id;
    private String name;
    private String right;
    private String description;

    private List<RoleBean> roles;

    public PermissionDTO(Integer id){
        this.id = id;
    }


}