package net.dkahn.starter.apps.webapps.modules.security.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class RightDto implements Serializable {
    private Integer id;
    private Boolean selected;
    private String right;
    private String description;
}
