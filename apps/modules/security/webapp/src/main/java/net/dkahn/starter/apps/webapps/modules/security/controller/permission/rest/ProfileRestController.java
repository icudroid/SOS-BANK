package net.dkahn.starter.apps.webapps.modules.security.controller.permission.rest;

import net.dkahn.starter.apps.webapps.common.bean.WebResponseBean;
import net.dkahn.starter.apps.webapps.modules.security.bean.ProfileBean;
import net.dkahn.starter.apps.webapps.modules.security.facade.PermissionFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static net.dkahn.starter.apps.webapps.modules.security.controller.permission.IMetaDataController.ProfileRestController.*;

/**
 * User: dimitri
 * Date: 14/01/15
 * Time: 10:25
 * Goal:
 */
@RestController
@RequestMapping(PREFIX_PATH)
public class ProfileRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionFacade permissionFacade;


    @Secured({"ROLE_PROFILE_W"})
    @RequestMapping(value = SAVE_PROFILE,method = RequestMethod.POST)
    @ResponseBody
    public WebResponseBean updateProfileByField(@RequestBody ProfileBean profile){
        logger.info("request de mise à jour du profile : {}",profile);
        try {
            permissionFacade.saveProfile(profile);
            return WebResponseBean.OK();
        } catch (Exception e) {
            logger.error("Une erreur lors de la mise à jour du profile "+profile,e);
            //Todo:
            return WebResponseBean.FAILED(1,e.getMessage());
        }

    }



    @Secured({"ROLE_PROFILE_W"})
    @RequestMapping(value = DELETE_PROFILE)
    @ResponseBody
    public WebResponseBean deleteProfile(@PathVariable Integer id){
        logger.info("suppression du profile avec l'identifiant : {}",id);
        try {
            permissionFacade.deleteProfile(id);
            return WebResponseBean.OK();
        } catch (Exception e) {
            logger.error("Une erreur lors de la suppression du profile : "+id,e);
            //Todo:
            return WebResponseBean.FAILED(1,e.getMessage());
        }

    }



    @Secured({"ROLE_PROFILE_R"})
    @RequestMapping(value = EXIST_PROFILE)
    @ResponseBody
    public WebResponseBean existProfile(@PathVariable Integer id, @PathVariable String newName){
        try {
            return WebResponseBean.OK_WITH_DATA(permissionFacade.existProfile(id,newName));
        } catch (Exception e) {
            logger.error("Une erreur",e);
            //Todo:
            return WebResponseBean.FAILED(1,e.getMessage());
        }

    }


}
