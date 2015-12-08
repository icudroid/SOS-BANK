package net.dkahn.starter.apps.webapps.common.command.datatable.response;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: dimitri
 * Date: 22/01/15
 * Time: 14:06
 * Goal:
 */
@Data
public  class DataTableResponse<T> implements Serializable {

    private Integer draw;
    private Long recordsTotal;
    private Long recordsFiltered;
    private List<T> data = new ArrayList<>();
    private String error;

    private List<LabelError> errors;

    public static DataTableResponse withErrors(List<LabelError> errors, Integer draw,Long count) {
        DataTableResponse response = new DataTableResponse();
        response.setErrors(errors);
        response.setDraw(draw);
        response.setRecordsFiltered(count);
        response.setRecordsTotal(count);
        return response;
    }
}
