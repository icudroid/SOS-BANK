package net.dkahn.starter.services.individu.impl;

import net.dkahn.starter.core.repositories.security.IIndividuRepository;
import net.dkahn.starter.domains.Individu;
import net.dkahn.starter.services.individu.IClientService;
import net.dkahn.starter.tools.logger.Log;
import net.dkahn.starter.tools.service.impl.GenericServiceImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ClientService extends GenericServiceImpl<Individu, String> implements IClientService {

    @Log
    private Logger log = null;


    public ClientService(IIndividuRepository repository) {
        this.repository = repository;
    }
}
