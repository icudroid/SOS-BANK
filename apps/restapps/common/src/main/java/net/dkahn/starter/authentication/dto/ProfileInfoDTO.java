package net.dkahn.starter.authentication.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Created by dev on 29/11/16.
 */
@Data
@Builder
public class ProfileInfoDTO {
    private String firstname;
    private String lastname;
}
