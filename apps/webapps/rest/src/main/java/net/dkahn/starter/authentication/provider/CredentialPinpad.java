package net.dkahn.starter.authentication.provider;

import lombok.Data;

@Data
public class CredentialPinpad {
    private String password;
    private String pindpadId;
}
