package net.dkahn.starter.controller;

import net.dkahn.starter.authentication.dto.ProfileInfoDTO;
import net.dkahn.starter.authentication.provider.service.IProfileRememberService;
import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.services.security.IUserService;
import net.dkahn.starter.tools.logger.Log;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static net.dkahn.starter.controller.IMetaDataRestController.UserRestController.*;

@RestController
public class UserRestController {

    @Log
    private Logger LOGGER = null;

    @Autowired
    private IProfileRememberService profileRememberService;

    @Autowired
    private IUserService userService;

    @SuppressWarnings("unused")
    @PostMapping(LOGOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        LOGGER.debug("Logout user and invalidate his session");
        session.invalidate();
    }


    @SuppressWarnings("unused")
    @GetMapping(PROFILE)
    public ProfileInfoDTO profile(HttpServletRequest request) {
        String username= profileRememberService.getUsername(request);

        if(username==null){
            return ProfileInfoDTO.builder().build();
        }else{
            User user = userService.findByUsername(username);
            return ProfileInfoDTO.builder().firstname(user.getFirstname()).lastname(user.getLastname()).build();
        }
    }


    @SuppressWarnings("unused")
    @GetMapping(INVALIDATE_PROFILE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unvalidateToken(HttpServletRequest request, HttpServletResponse response) {
        profileRememberService.unvalidateToken(request,response);
    }





}
