package net.dkahn.starter.apps.webapps.common.command.datatable.query;

import lombok.*;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataTableOrder {
    private Integer column;
    private String dir;

    public Sort.Direction toDirection(){
        return Sort.Direction.valueOf(dir.toUpperCase());
    }
}
