package net.dkahn.starter.domains.information;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FONCTION")
public class FonctionInformation extends InformationPersonnel {
    private String fonction;
}
