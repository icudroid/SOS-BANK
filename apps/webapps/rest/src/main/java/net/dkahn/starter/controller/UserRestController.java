package net.dkahn.starter.controller;

import net.dkahn.starter.tools.logger.Log;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static net.dkahn.starter.controller.IMetaDataRestController.UserRestController.LOGOUT;

@RestController
public class UserRestController {

    @Log
    private Logger LOGGER = null;

    @SuppressWarnings("unused")
    @PostMapping(LOGOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        LOGGER.debug("Logout user and invalidate his session");
        session.invalidate();
    }




}
