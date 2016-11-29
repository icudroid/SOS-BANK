package net.dkahn.starter.authentication.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class FailureDTO {
    private Integer attempts = 0;
    private Date lockedUntil = null;
    private String message = null;
    private boolean disable;
}
