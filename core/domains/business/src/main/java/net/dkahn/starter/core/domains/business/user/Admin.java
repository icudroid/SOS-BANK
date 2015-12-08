package net.dkahn.starter.core.domains.business.user;

import net.dkahn.starter.domains.security.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("OPERATOR")
public class Admin extends User {

}
