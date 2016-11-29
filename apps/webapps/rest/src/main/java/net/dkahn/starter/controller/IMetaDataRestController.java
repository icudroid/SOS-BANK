package net.dkahn.starter.controller;

/**
 * Metadata pour les controlleurs
 */
public interface IMetaDataRestController {

    interface PinpadRestController {
        String PREFIX_PATH              = "/pinpad";
        String GENERATE                 = "";
        String DOWNLOAD_IMAGE           = "/{id}/img";
    }

    interface UserRestController {
        String LOGOUT                   = "/logout";
        String PROFILE                  = "/profile";
        String INVALIDATE_PROFILE       = "/profile/invalidate";
    }
}
