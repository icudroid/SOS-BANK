package net.dkahn.starter.apps.webapps.common.command.datatable.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabelError {
    private String name;
    private String error;
    private String errorKey;
}
