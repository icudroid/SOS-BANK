package net.dkahn.starter.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Created by dev on 16/11/16.
 */
@Data
@Builder
public class PinpadDTO {
    private String pinpadId;
    private String imgUrl;
}
