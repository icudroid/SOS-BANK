package net.dkahn.starter.domains.information;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("PATRIMOINE")
public class PatrimoineInformation extends InformationPersonnel {
    @Enumerated(EnumType.STRING)
    private Patrimoine patrimoine;
}
