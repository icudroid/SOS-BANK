package net.dkahn.starter.core.domains.business.user;

import net.dkahn.starter.domains.security.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

}
