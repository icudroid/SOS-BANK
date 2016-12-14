package net.dkahn.starter.apps.webapps.modules.security.controller.permission.rest;


import net.dkahn.starter.apps.webapps.common.bean.WebResponseBean;
import net.dkahn.starter.apps.webapps.modules.security.bean.RoleBean;
import net.dkahn.starter.apps.webapps.modules.security.command.AddPermissionToRoleCommand;
import net.dkahn.starter.apps.webapps.modules.security.facade.PermissionFacade;
import net.dkahn.starter.apps.webapps.modules.security.controller.permission.IMetaDataController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * User: dimitri
 * Date: 12/01/15
 * Time: 15:04
 * Goal:
 */
@RestController
@RequestMapping(IMetaDataController.RoleRestController.PREFIX_PATH)
public class RoleRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionFacade permissionFacade;


    @Secured(
            {
                    "ROLE_ROLE_W"
            }
    )
    @RequestMapping(IMetaDataController.RoleRestController.DELETE_ROLE)
    public WebResponseBean<Void> deleteRoleById(@PathVariable Integer id){

        try {
            permissionFacade.deleteRoleById(id);
            return WebResponseBean.OK();
        } catch (Exception e) {
            logger.error("Une erreur lors de la suppression du role "+id,e);
            //Todo:
            return WebResponseBean.FAILED(1,e.getMessage());
        }
    }


    @Secured(
            {
                    "ROLE_ROLE_R"
            }
    )
    @RequestMapping(value = IMetaDataController.RoleRestController.EXIST_ROLE)
    public @ResponseBody
    WebResponseBean<Boolean> existProfile(@PathVariable Integer id,@PathVariable String newName){
        try {
            return WebResponseBean.OK_WITH_DATA(permissionFacade.existRole(id, newName));
        } catch (Exception e) {
            logger.error("Une erreur",e);
            //Todo:
            return WebResponseBean.FAILED(1,e.getMessage());
        }

    }



    @Secured("ROLE_ROLE_R")
    @RequestMapping(value = IMetaDataController.RoleRestController.SEARCH_BY_NAME)
    public @ResponseBody
    Page<RoleBean> searchByName(@RequestParam("q") String query, Pageable pageable){
        return permissionFacade.findRoleByName(query, pageable);
    }


    @Secured("ROLE_ROLE_W")
    @RequestMapping(IMetaDataController.RoleRestController.ADD_PERMISSION_TO_ROLE)
    public WebResponseBean<Void> addPermission(@RequestBody AddPermissionToRoleCommand command){

        try {
            permissionFacade.addPermissionToRole(command.getPermissionId(), command.getRoleId());
            return WebResponseBean.OK();
        } catch (Exception e) {
            logger.error("Une erreur lors de la l'ajout du roleId :"+command.getRoleId()+ "et la permissionId : "+ command.getPermissionId(),e);
            return WebResponseBean.FAILED(1,e.getMessage());
        }
    }



}
