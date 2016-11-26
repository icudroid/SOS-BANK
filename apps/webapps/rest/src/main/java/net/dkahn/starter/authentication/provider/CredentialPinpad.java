package net.dkahn.starter.authentication.provider;

import lombok.Data;

@Data
class CredentialPinpad {
    private String password;
    private String pindpadId;
}
