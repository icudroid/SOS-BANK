package net.dkahn.starter.apps.webapps.frontend.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static net.dkahn.starter.apps.webapps.frontend.controller.IMetaDataController.HomeController.*;
import static net.dkahn.starter.apps.webapps.frontend.controller.IMetaDataController.HomeController.Views.*;


@Controller
@RequestMapping(PREFIX_PATH)
public class HomeController {

    @RequestMapping(Root.PATH)
    public String root(){
        return Root.VIEW;
    }


    @Secured("ROLE_HOME")
    @RequestMapping(Home.PATH)
    public String home(){
        return Home.VIEW;
    }



    @Secured("ROLE_A")
    @RequestMapping(Home.PATH_A)
    public String homeA(){
        return Home.VIEW_A;
    }


    @Secured("ROLE_B")
    @RequestMapping(Home.PATH_B)
    public String homeB(){
        return Home.VIEW_B;
    }

}
