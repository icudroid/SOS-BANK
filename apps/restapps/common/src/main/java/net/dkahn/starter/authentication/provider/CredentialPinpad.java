package net.dkahn.starter.authentication.provider;

import lombok.Data;

import java.time.LocalDate;

@Data
class CredentialPinpad {
    private String password;
    private String pindpadId;
    private LocalDate birthdate;
}
