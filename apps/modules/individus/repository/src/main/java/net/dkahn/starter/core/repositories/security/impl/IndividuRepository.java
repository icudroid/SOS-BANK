package net.dkahn.starter.core.repositories.security.impl;

import net.dkahn.starter.core.repositories.security.IIndividuRepository;
import net.dkahn.starter.domains.Individu;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;
import org.springframework.stereotype.Repository;

@Repository
public class IndividuRepository extends GenericRepositoryJpa<Individu,String> implements IIndividuRepository {


}
