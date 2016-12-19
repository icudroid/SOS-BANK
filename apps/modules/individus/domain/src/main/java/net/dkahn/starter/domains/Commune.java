package net.dkahn.starter.domains;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class Commune {
    private String codeDepartement;
    private String codeGeographique;
    @Column(name = "commune_code_postal")
    private String codePostal;
    private String commune;
}
