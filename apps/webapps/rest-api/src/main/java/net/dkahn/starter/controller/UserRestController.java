package net.dkahn.starter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dev on 03/11/16.
 */
@RestController
public class UserRestController {

    @RequestMapping(value = "/", produces = "application/json")
    public Map<String, String> login(Principal principal) {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("username", principal.getName());
        return result;
    }

    @RequestMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
