package net.dkahn.starter.controller;

import net.dkahn.starter.domains.security.Pinpad;
import net.dkahn.starter.dto.PinpadDTO;
import net.dkahn.starter.services.security.IPinpadService;
import net.dkahn.starter.services.security.exception.PinpadExpiredException;
import net.dkahn.starter.tools.logger.Log;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.MessageFormat;

import static net.dkahn.starter.controller.IMetaDataRestController.PinpadRestController.*;

@RestController
@RequestMapping(PREFIX_PATH)
public class PinpadRestController {

    private static final String CONTENT_TYPE_IMG = "image/png";
    private static final String PINPAD_DOWNLAD_URL = "pinpad/{0}/img";


    private final String base;

    @Log
    private Logger logger = null;

    private final IPinpadService pinpadService;

    public PinpadRestController(@Value("${env.baseUrl}") String base, IPinpadService pinpadService) {
        this.base = base;
        this.pinpadService = pinpadService;
    }


    @SuppressWarnings("unused")
    @RequestMapping(GENERATE)
    public PinpadDTO generatePinpad() {
        Pinpad generate = pinpadService.generate();
        return PinpadDTO.builder()
                .pinpadId(generate.getId())
                .imgUrl(MessageFormat.format(base+PINPAD_DOWNLAD_URL,generate.getId()))
                .build();
    }


    @SuppressWarnings("unused")
    @RequestMapping(value = DOWNLOAD_IMAGE)
    public ResponseEntity<byte[]> pinpadImage(@PathVariable String id) throws PinpadExpiredException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_IMG);
            return new ResponseEntity<>(pinpadService.generateImage(id), headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
