package net.dkahn.starter.domains.information;


import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue("FONCTION")
public class FonctionInformation extends InformationPersonnel {
    private String fonction;
}
