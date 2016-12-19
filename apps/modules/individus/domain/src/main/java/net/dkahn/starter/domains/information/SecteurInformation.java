package net.dkahn.starter.domains.information;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("SECTEUR")
public class SecteurInformation extends InformationPersonnel {
    @Enumerated(EnumType.STRING)
    private Secteur secteur;
}
