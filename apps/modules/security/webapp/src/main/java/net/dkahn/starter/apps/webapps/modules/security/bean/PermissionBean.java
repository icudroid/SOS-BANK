package net.dkahn.starter.apps.webapps.modules.security.bean;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class PermissionBean {
    private Integer id;
    private String name;
    private String right;
    private String description;

    public PermissionBean(Integer id){
        this.id = id;
    }


}