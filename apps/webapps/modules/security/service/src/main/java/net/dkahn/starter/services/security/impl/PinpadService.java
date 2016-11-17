package net.dkahn.starter.services.security.impl;

import net.dkahn.starter.core.repositories.security.IPinpadRepository;
import net.dkahn.starter.domains.security.Pinpad;
import net.dkahn.starter.services.security.IPinpadService;
import net.dkahn.starter.tools.logger.Log;
import net.dkahn.starter.tools.service.impl.GenericServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * Created by dev on 16/11/16.
 */
@Service
public class PinpadService extends GenericServiceImpl<Pinpad, String> implements IPinpadService {

    public static final int PINPAD_LENGTH = 10;
    public static final int IMG_WIDTH = 46;
    public static final int IMG_HEIGHT = 46;
    public static final int IMG_HEIGHT_GLOBAL = IMG_WIDTH * PINPAD_LENGTH;

    @Value("${pinpad.path.base}")
    private String base;

    @Value("${pinpad.duration:120}")
    private Integer pinpadLive;

    @Log
    private Logger log;

    private static final String IMAGE_EXTENTION = ".png";

    public PinpadService(IPinpadRepository pinpadRepository){
        repository = pinpadRepository;
    }

    @Override
    @Transactional
    public Pinpad generate(){
        Pinpad res = new Pinpad();

        List<Integer> numbers = new ArrayList<>(PINPAD_LENGTH);
        for (int i = 0; i < PINPAD_LENGTH; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        log.debug("Generation du pinpad aléatoire : {}",numbers);

        res.setCorrespondance(numbers);

        return repository.save(res);
    }

    @Override
    @Transactional
    public String decodePassword(String id, String encodePassword) throws PinpadExpiredException{

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
        LocalDateTime expiration = pinpad.getCreationDate().plusSeconds(pinpadLive);
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

        StringBuilder path = new StringBuilder(base);
        path.append(image.toString());
        path.append(IMAGE_EXTENTION);

        String imgPath = path.toString();
        log.debug("Path image mire pour pinpad {} => {}",pinpad.getId(), imgPath);

        return imgPath;
    }


    @Override
    @Transactional
    public byte[] generateImage(String id) throws IOException, PinpadExpiredException {

        Pinpad pinpad = repository.get(id);
        isValid(pinpad);

        BufferedImage img = new BufferedImage(IMG_WIDTH, IMG_HEIGHT_GLOBAL,BufferedImage.TYPE_INT_RGB);
        Graphics g = img.createGraphics();

        int offset = 0;
        for (int i = 0; i < pinpad.getCorrespondance().size(); i++) {
            String imagePath = findPathImage(pinpad,i);
            BufferedImage image = ImageIO.read(new FileInputStream(imagePath));

            g.drawImage(image, 0, offset, null);
            offset+=IMG_HEIGHT;
        }

        g.dispose();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            ImageIO.write( img, "PNG", baos );
            baos.flush();
            return  baos.toByteArray();
        }
    }


}
