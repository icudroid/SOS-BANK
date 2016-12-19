package net.dkahn.starter.domains.information;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("SITUATION_PERSONNEL")
public class SituationPersonnelInformation extends InformationPersonnel {
    @Enumerated(EnumType.STRING)
    private SituationPersonnel situationPersonnel;
}
