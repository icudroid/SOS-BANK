package net.dkahn.starter.controller;

/**
 * Metadata pour les controlleurs
 */
class IMetaDataRestController {

    interface PinpadRestController {
        String PREFIX_PATH              = "/pinpad";
        String GENERATE                 = "";
        String DOWNLOAD_IMAGE           = "/{id}/img";
    }

    interface UserRestController {
        String LOGOUT                   = "/logout";
    }
}
