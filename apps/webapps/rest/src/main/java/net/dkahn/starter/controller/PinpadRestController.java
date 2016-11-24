package net.dkahn.starter.controller;

import net.dkahn.starter.domains.security.Pinpad;
import net.dkahn.starter.dto.PinpadDTO;
import net.dkahn.starter.services.security.IPinpadService;
import net.dkahn.starter.services.security.impl.PinpadExpiredException;
import net.dkahn.starter.tools.logger.Log;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.MessageFormat;

import static net.dkahn.starter.controller.IMetaDataRestController.PinpadRestController.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(PREFIX_PATH)
public class PinpadRestController {

    private static final String CONTENT_TYPE_IMG = "image/png";
    public static final String PINPAD_DOWNLAD_URL = "pinpad/{0}/img";

    @Log
    private Logger LOGGER;

    @Autowired
    private IPinpadService pinpadService;

    @RequestMapping(GENERATE)
    public PinpadDTO generatePinpad(HttpServletRequest request) {
        Pinpad generate = pinpadService.generate();
        return PinpadDTO.builder()
                .pinpadId(generate.getId())
                .imgUrl(MessageFormat.format("http://localhost:8080/"+PINPAD_DOWNLAD_URL,generate.getId()))
                .build();
    }


    @RequestMapping(value = DOWNLOAD_IMAGE ,method = RequestMethod.GET)
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
