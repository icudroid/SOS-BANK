package net.dkahn.starter.domains;

import lombok.Data;

import javax.persistence.Embeddable;


@Data
@Embeddable
public class Adresse {
    private String adresse;
    private String complement;
    private String codePostal;
    private String ville;
    private String pays;
    private Boolean valide;
}
