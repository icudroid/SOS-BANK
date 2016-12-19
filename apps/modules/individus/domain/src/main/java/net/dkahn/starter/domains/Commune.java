package net.dkahn.starter.domains;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Commune {
    private String codeDepartement;
    private String codeGeographique;
    private String codePostal;
    private String commune;
}
