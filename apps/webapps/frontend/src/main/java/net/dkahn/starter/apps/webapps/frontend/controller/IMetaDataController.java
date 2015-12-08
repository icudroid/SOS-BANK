package net.dkahn.starter.apps.webapps.frontend.controller;


public interface IMetaDataController {


    interface HomeController {
        String PREFIX_PATH = "/";
        interface Views {
            interface Home {
                String PATH = "home";
                String VIEW = "home";
            }

            interface Root {
                String PATH = "";
                String VIEW = "index";
            }

        }
    }


}
