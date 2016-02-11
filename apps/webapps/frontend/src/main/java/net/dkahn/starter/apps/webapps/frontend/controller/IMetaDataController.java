package net.dkahn.starter.apps.webapps.frontend.controller;


public interface IMetaDataController {


    interface HomeController {
        String PREFIX_PATH = "/";
        interface Views {
            interface Home {
                String PATH = "home";
                String VIEW = "home";


                String PATH_A = "/a";
                String VIEW_A = "a";

                String PATH_B = "/b";
                String VIEW_B = "b";

            }

            interface Root {
                String PATH = "";
                String VIEW = "index";
            }

        }
    }


}
