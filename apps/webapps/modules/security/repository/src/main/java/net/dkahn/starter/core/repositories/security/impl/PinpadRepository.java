package net.dkahn.starter.core.repositories.security.impl;

import net.dkahn.starter.core.repositories.security.IPinpadRepository;
import net.dkahn.starter.domains.security.Pinpad;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;
import org.springframework.stereotype.Repository;

@Repository
public class PinpadRepository extends GenericRepositoryJpa<Pinpad,String> implements IPinpadRepository{



}
