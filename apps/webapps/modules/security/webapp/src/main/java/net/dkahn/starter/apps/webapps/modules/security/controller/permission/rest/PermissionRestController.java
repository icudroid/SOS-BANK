package net.dkahn.starter.apps.webapps.modules.security.controller.permission.rest;


import net.dkahn.starter.apps.webapps.common.command.datatable.query.DataTableQuery;
import net.dkahn.starter.apps.webapps.common.command.datatable.response.DataTableResponse;
import net.dkahn.starter.apps.webapps.modules.security.bean.PermissionDTO;
import net.dkahn.starter.apps.webapps.modules.security.bean.PermissionRightDto;
import net.dkahn.starter.apps.webapps.modules.security.command.PermissionCommand;
import net.dkahn.starter.apps.webapps.modules.security.facade.PermissionFacade;
import net.dkahn.starter.apps.webapps.modules.security.controller.permission.IMetaDataController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * User: dimitri
 * Date: 12/01/15
 * Time: 15:04
 * Goal:
 */
@RestController
@RequestMapping(IMetaDataController.PermissionRestController.PREFIX_PATH)
public class PermissionRestController {


    private final static List<String> PERMISSION_CONFIG = Arrays.asList("name", "right", "description");

    @Autowired
    private PermissionFacade permissionFacade;


    @Secured({"ROLE_PERMISSION_R"})
    @RequestMapping(IMetaDataController.PermissionRestController.FIND_PERMISSION_BY_ROLE_ID)
    public List<PermissionRightDto> findPermissionsByRoleId(@PathVariable Integer roleId) {
        return permissionFacade.findPermissionsByRoleId(roleId);
    }


    @Secured({"ROLE_PERMISSION_R"})
    @RequestMapping(IMetaDataController.PermissionRestController.FIND_ALL_PERMISSION)
    public List<PermissionRightDto> findAllPermissions() {
        return permissionFacade.findAllPermissions();
    }


    @Secured("ROLE_PERMISSION_R")
    @RequestMapping(value = IMetaDataController.PermissionRestController.FIND_PERMISSION)
    public DataTableResponse<PermissionDTO> listNew(@RequestBody DataTableQuery<PermissionCommand> query) throws Exception {
        return permissionFacade.findByQuery(query, PERMISSION_CONFIG);
    }


}
