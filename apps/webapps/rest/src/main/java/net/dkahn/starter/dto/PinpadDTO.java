package net.dkahn.starter.dto;

import lombok.Builder;
import lombok.Data;

/**
 * pinpad DTO
 */
@Data
@Builder
public class PinpadDTO {
    private String pinpadId;
    private String imgUrl;
}
