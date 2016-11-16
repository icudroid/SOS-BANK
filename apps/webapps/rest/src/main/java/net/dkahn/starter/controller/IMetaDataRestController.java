package net.dkahn.starter.controller;

/**
 * Created by dev on 17/11/16.
 */
public class IMetaDataRestController {

    interface PinpadRestController {
        String PREFIX_PATH = "/pinpad";
        String GENERATE = "";
        String DOWNLOAD_IMAGE = "/{id}/img";

    }
}
