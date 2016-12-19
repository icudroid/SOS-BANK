package net.dkahn.starter.domains.information;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("REVENU")
public class RevenuInformation extends InformationPersonnel {
    private Integer revenu;
}
