package net.dkahn.starter.apps.webapps.bankingstub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static net.dkahn.starter.apps.webapps.bankingstub.controller.IMetaDataController.HomeController.PREFIX_PATH;
import static net.dkahn.starter.apps.webapps.bankingstub.controller.IMetaDataController.HomeController.Views.Root;


@Controller
@RequestMapping(PREFIX_PATH)
public class HomeController {

    @RequestMapping(Root.PATH)
    public String root(){
        return Root.VIEW;
    }

}
