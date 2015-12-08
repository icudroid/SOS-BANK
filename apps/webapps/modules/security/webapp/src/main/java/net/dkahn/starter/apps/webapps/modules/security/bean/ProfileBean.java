package net.dkahn.starter.apps.webapps.modules.security.bean;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class ProfileBean{
    private Integer id;
    private String name;
    private String description;
    private List<RoleBean> roles;


    public ProfileBean(){
        roles = new ArrayList<>();
    }


}
