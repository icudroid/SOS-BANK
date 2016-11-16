package net.dkahn.starter.services.security;

import net.dkahn.starter.domains.security.Pinpad;
import net.dkahn.starter.tools.service.GenericService;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created by dev on 16/11/16.
 */
public interface IPinpadService extends GenericService<Pinpad, String> {


    /**
     *
     * @return pinpad
     */
    Pinpad generate();


    /**
     *
     * @param id
     * @param encodePassword
     * @return
     */
    String decodePassword(String id,String encodePassword);

    @Transactional
    String findPathImage(String id, Integer position);

    @Transactional
    byte[] generateImage(String id) throws IOException;
}
