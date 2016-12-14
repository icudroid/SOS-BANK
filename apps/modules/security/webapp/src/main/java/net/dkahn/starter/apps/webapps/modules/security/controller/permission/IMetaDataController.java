package net.dkahn.starter.apps.webapps.modules.security.controller.permission;


public interface IMetaDataController {


    interface PermissionRestController {
        String PREFIX_PATH = "/admin/rest/permission";
        String FIND_PERMISSION_BY_ROLE_ID = "/byRole/{roleId}";
        String FIND_ALL_PERMISSION = "/all";
        String FIND_PERMISSION = "/query";

    }

    interface RoleRestController {
        String PREFIX_PATH = "/admin/rest/role";
        String DELETE_ROLE = "/delete/{id}";
        String EXIST_ROLE = "/exist/{id}/{newName}";
        String SEARCH_BY_NAME = "/searchByName";
        String ADD_PERMISSION_TO_ROLE = "/addPermission";

    }



    interface ProfileRestController {
        String PREFIX_PATH   = "/admin/rest/profile";
        String SAVE_PROFILE  = "/save";
        String EXIST_PROFILE = "/exist/{id}/{newName}";
        String DELETE_PROFILE = "/delete/{id}";

    }


    interface PermissionController {
        String PREFIX_PATH = "/admin/profile";
        interface Views {

            interface ListProfiles {
                String PATH = "";
                String VIEW = "admin/permission/profiles";
            }


            interface EditProfile {
                String PATH = "/edit/{id}";
                String VIEW = "admin/permission/profile_edit_modal";
            }


            interface EditRole {
                String PATH = "/role/edit/{id}";
                String VIEW = "admin/permission/role_edit_modal";
                String SAVE = "/role/save";
                String SAVED = "admin/permission/role_saved";
            }


            interface CreateRole {
                String PATH = "/role/create";
                String VIEW = "admin/permission/role_edit_modal";
            }

            interface AddRoleToProfile {
                String PATH = "/addRole";
                String VIEW = "admin/permission/profile_add_role_modal";
            }

            interface CreateProfile {
                String PATH = "/create";
                String VIEW = "admin/permission/profile_create_modal";
                String CREATED = "admin/permission/profile_created";
            }
        }
    }


}
