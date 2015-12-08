package net.dkahn.starter.apps.webapps.backoffice.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static net.dkahn.starter.apps.webapps.backoffice.controller.IMetaDataController.HomeController.PREFIX_PATH;
import static net.dkahn.starter.apps.webapps.backoffice.controller.IMetaDataController.HomeController.Views.Home;

/**
 * Created by dkahn on 01/06/15.
 */
@Controller

@RequestMapping(PREFIX_PATH)
public class HomeController {

    @Secured("ROLE_HOME")
    @RequestMapping(Home.PATH)
    public String home(){
        return Home.VIEW;
    }

}
