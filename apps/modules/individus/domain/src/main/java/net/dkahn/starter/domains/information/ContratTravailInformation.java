package net.dkahn.starter.domains.information;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("CONTRAT_TRAVAIL")
public class ContratTravailInformation extends InformationPersonnel {
    @Enumerated(EnumType.STRING)
    private ContractTravail contractTravail;
}
