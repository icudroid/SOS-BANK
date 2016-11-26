package net.dkahn.starter.services.security.impl;

import net.dkahn.starter.core.repositories.security.IPinpadRepository;
import net.dkahn.starter.domains.security.Pinpad;
import net.dkahn.starter.services.security.IPinpadService;
import net.dkahn.starter.services.security.PinpadConfigProperties;
import net.dkahn.starter.services.security.exception.PinpadExpiredException;
import net.dkahn.starter.tools.logger.Log;
import net.dkahn.starter.tools.service.impl.GenericServiceImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pinpad service
 */
@Service
public class PinpadService extends GenericServiceImpl<Pinpad, String> implements IPinpadService {

    private final PinpadConfigProperties pinpadConfigProperties;

    @Log
    private Logger log = null;

    public PinpadService(IPinpadRepository pinpadRepository, PinpadConfigProperties pinpadConfigProperties){
        this.pinpadConfigProperties = pinpadConfigProperties;
        repository = pinpadRepository;
    }

    @Override
    @Transactional
    public Pinpad generate(){
        Pinpad res = new Pinpad();

        List<Integer> numbers = new ArrayList<>(pinpadConfigProperties.getLength());
        for (int i = 0; i < pinpadConfigProperties.getLength(); i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        log.debug("Generation du pinpad aléatoire : {}",numbers);

        res.setCorrespondance(numbers);

        return repository.save(res);
    }

    @Override
    @Transactional
    public String decodePassword(String id, String encodePassword) throws PinpadExpiredException {

        Pinpad pinpad = repository.get(id);

        isValid(pinpad);

        StringBuilder decoded = new StringBuilder();

        for (int i = 0; i < encodePassword.length(); i++) {
            Integer pos = new Integer(String.valueOf(encodePassword.charAt(i)));
            decoded.append(pinpad.getCorrespondance().get(pos));

        }
        String decodePassword = decoded.toString();
        log.trace("decode le mot de passe : {} => {}",encodePassword, decodePassword);

        return decodePassword;
    }

    private boolean isValid(Pinpad pinpad) throws PinpadExpiredException {
        LocalDateTime expiration = pinpad.getCreationDate().plusSeconds(pinpadConfigProperties.getDuration());
        if(expiration.isAfter(LocalDateTime.now()))
            throw new PinpadExpiredException();
        return true;
    }


    @Override
    @Transactional
    public String findPathImage(String id, Integer position) {
        return findPathImage(repository.get(id),position);
    }


    private String findPathImage(Pinpad pinpad, Integer position) {
        Integer image = pinpad.getCorrespondance().get(position);

        String imgPath = pinpadConfigProperties.getBase() + image.toString() +
                pinpadConfigProperties.getImageExtention();

        log.debug("Path image mire pour pinpad {} => {}",pinpad.getId(), imgPath);

        return imgPath;
    }


    @Override
    @Transactional
    public byte[] generateImage(String id) throws IOException, PinpadExpiredException {

        Pinpad pinpad = repository.get(id);
        pinpad.setProvided(true);
        isValid(pinpad);

        BufferedImage img = new BufferedImage(pinpadConfigProperties.getImageWidth(), pinpadConfigProperties.getImageHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics g = img.createGraphics();

        int offset = 0;
        for (int i = 0; i < pinpad.getCorrespondance().size(); i++) {
            String imagePath = findPathImage(pinpad,i);
            BufferedImage image = ImageIO.read(new FileInputStream(imagePath));

            g.drawImage(image, 0, offset, null);
            offset+=pinpadConfigProperties.getImageHeight();
        }

        g.dispose();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write( img, "PNG", baos );
            baos.flush();
            return  baos.toByteArray();
        }
    }


}
