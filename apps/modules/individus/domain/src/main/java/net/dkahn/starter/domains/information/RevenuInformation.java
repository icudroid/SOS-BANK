package net.dkahn.starter.domains.information;


import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue("REVENU")
public class RevenuInformation extends InformationPersonnel {
    private Integer revenu;
}
