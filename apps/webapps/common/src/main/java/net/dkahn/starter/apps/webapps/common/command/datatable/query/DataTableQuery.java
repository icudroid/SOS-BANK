package net.dkahn.starter.apps.webapps.common.command.datatable.query;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataTableQuery<T> implements Serializable {
    private Integer draw;
    private Integer start;
    private Integer length;

    private T additionalForm;
    private DataTableSearch search;
    private List<DataTableOrder> order;


}
