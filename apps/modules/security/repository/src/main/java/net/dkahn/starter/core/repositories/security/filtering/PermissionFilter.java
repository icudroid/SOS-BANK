package net.dkahn.starter.core.repositories.security.filtering;

import net.dkahn.starter.tools.repository.filtering.IFiltering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionFilter implements IFiltering {

    private Integer id;
    private String right;
    private String permission;
    private String description;
    private Boolean attachedRole;


    @Override
    public PermissionFilter defaultFilter(String value) {
        return PermissionFilter.builder().permission(value).build();
    }
}
