package net.dkahn.starter.domains;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PROSPET")
public class Prospet extends Individu{
}
