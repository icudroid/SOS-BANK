package net.dkahn.starter.domains.information;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("REVENU_FOYER")
public class RevenuFoyerInformation extends InformationPersonnel {
    @Enumerated(EnumType.STRING)
    private RevenuFoyer revenuFoyer;
}
