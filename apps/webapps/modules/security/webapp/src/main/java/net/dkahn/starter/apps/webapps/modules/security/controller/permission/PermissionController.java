package net.dkahn.starter.apps.webapps.modules.security.controller.permission;


import net.dkahn.starter.apps.webapps.modules.security.bean.ProfileBean;
import net.dkahn.starter.apps.webapps.modules.security.bean.RoleBean;
import net.dkahn.starter.apps.webapps.modules.security.facade.PermissionFacade;
import net.dkahn.starter.apps.webapps.modules.security.validator.ProfileValidator;
import net.dkahn.starter.apps.webapps.modules.security.validator.RoleValidator;
import net.dkahn.starter.core.repositories.security.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


import static net.dkahn.starter.apps.webapps.modules.security.controller.permission.IMetaDataController.PermissionController.*;
import static net.dkahn.starter.apps.webapps.modules.security.controller.permission.IMetaDataController.PermissionController.Views.*;

/**
 * User: dimitri
 * Date: 12/01/15
 * Time: 15:04
 * Goal:
 */
@Controller

@RequestMapping(PREFIX_PATH)
public class PermissionController {

    @Autowired
    private PermissionFacade permissionFacade;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private ProfileValidator profileValidator;

    @Autowired
    private RoleValidator roleValidator;


    @InitBinder("profile")
    protected void initBinderProfile(WebDataBinder binder) {
        binder.setValidator(profileValidator);
    }


    @InitBinder("role")
    protected void initBinderRole(WebDataBinder binder) {
        binder.setValidator(roleValidator);
    }



    @Secured(
            {
                    "ROLE_PROFILE_LIST"
            }
    )
    @RequestMapping(ListProfiles.PATH)
    public String listProfiles(ModelMap map){
        map.addAttribute("profiles", permissionFacade.findAllProfiles());
        map.addAttribute("roles", permissionFacade.getAllRole());
        return ListProfiles.VIEW;
    }



    @Secured(
            {
                    "ROLE_PROFILE_R"
            }
    )
    @RequestMapping(EditProfile.PATH)
    public String editProfile(@PathVariable Integer id ,ModelMap map){
        map.addAttribute("profile", permissionFacade.getProfile(id));
        map.addAttribute("roles", permissionFacade.getAllRole());
        return EditProfile.VIEW;
    }





    @Secured(
            {
                    "ROLE_PROFILE_W"
            }
    )
    @RequestMapping(CreateProfile.PATH)
    public String createProfile(ModelMap map){
        map.addAttribute("profile", new ProfileBean());
        map.addAttribute("roles", permissionFacade.getAllRole());
        return CreateProfile.VIEW;
    }



    @Secured(
            {
                    "ROLE_PROFILE_W"
            }
    )
    @RequestMapping(value = CreateProfile.PATH,method = RequestMethod.POST)
    public String createProfile(@Valid @ModelAttribute("profile") ProfileBean profile,BindingResult bindingResult,ModelMap map){
        map.addAttribute("roles", permissionFacade.getAllRole());

        if(bindingResult.hasErrors()){
            return CreateProfile.VIEW;
        }else{
            permissionFacade.createProfile(profile);
            return CreateProfile.CREATED;
        }

    }




    @Secured(
            {
                    "ROLE_ROLE_R"
            }
    )
    @RequestMapping(EditRole.PATH)
    public String editRole(@PathVariable Integer id ,ModelMap map){
        map.addAttribute("role", permissionFacade.getRole(id));
        map.addAttribute("permissionsRights", permissionFacade.getAllPermissions());
        return EditRole.VIEW;
    }



    @Secured(
            {
                    "ROLE_ROLE_W"
            }
    )
    @RequestMapping(CreateRole.PATH)
    public String createRole(@ModelAttribute("role")RoleBean role ,ModelMap map){
        map.addAttribute("permissionsRights", permissionFacade.getAllPermissions());
        return CreateRole.VIEW;
    }


    @Secured(
            {
                    "ROLE_ROLE_W"
            }
    )
    @RequestMapping(value = EditRole.SAVE,method = RequestMethod.POST)
    public String saveRole(@Valid @ModelAttribute("role")RoleBean role,BindingResult bindingResult,ModelMap map){
        if(bindingResult.hasErrors()){
            map.addAttribute("permissionsRights", permissionFacade.getAllPermissions());

            return EditRole.VIEW;
        }else{
            permissionFacade.saveRole(role);
            return EditRole.SAVED;
        }

    }




    @Secured(
            {
                    "ROLE_PROFILE_W"
            }
    )
    @RequestMapping(AddRoleToProfile.PATH)
    public String addRoleModal(){
        return AddRoleToProfile.VIEW;
    }


}
