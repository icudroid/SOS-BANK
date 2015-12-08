package net.dkahn.starter.apps.webapps.common.command.datatable.query;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataTableSearch {
    private String value;
    private Boolean regex;
}
