package net.dkahn.starter.apps.webapps.modules.security.facade;


import net.dkahn.starter.apps.webapps.common.command.datatable.query.DataTableQuery;
import net.dkahn.starter.apps.webapps.common.command.datatable.response.DataTableResponse;
import net.dkahn.starter.apps.webapps.common.utils.datatable.DatatableQueryUtils;
import net.dkahn.starter.apps.webapps.modules.security.bean.*;
import net.dkahn.starter.apps.webapps.modules.security.bean.builder.PermissionDTOBuilder;
import net.dkahn.starter.apps.webapps.modules.security.bean.builder.ProfileBeanBuilder;
import net.dkahn.starter.apps.webapps.modules.security.bean.builder.RoleBeanBuilder;
import net.dkahn.starter.apps.webapps.modules.security.bean.dismantler.ProfileDismantler;
import net.dkahn.starter.apps.webapps.modules.security.bean.dismantler.RoleDismantler;
import net.dkahn.starter.apps.webapps.modules.security.command.PermissionCommand;
import net.dkahn.starter.core.repositories.security.IPermissionRepository;
import net.dkahn.starter.core.repositories.security.IProfileRepository;
import net.dkahn.starter.core.repositories.security.IRoleRepository;
import net.dkahn.starter.core.repositories.security.IUserRepository;
import net.dkahn.starter.tools.logger.Log;
import net.dkahn.starter.domains.security.Permission;
import net.dkahn.starter.domains.security.Profile;
import net.dkahn.starter.domains.security.Role;
import net.dkahn.starter.domains.security.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 12/01/15
 * Time: 15:50
 * Goal:
 */
@Service
public class PermissionFacade {

    @Log
    private Logger logger;

    @Autowired
    private IProfileRepository profileRepository;

    @Autowired
    private IRoleRepository roleRepository;


    @Autowired
    private IPermissionRepository permissionRepository;


    @Autowired
    private IUserRepository userRepository;


    @Autowired
    private PermissionDTOBuilder permissionDTOBuilder;


    @Transactional
    public List<ProfileBean> findAllProfiles() {
        return ProfileBeanBuilder.build(profileRepository.getAll());
    }

    @Transactional
    public ProfileBean getProfile(Integer id) {
        return ProfileBeanBuilder.build(profileRepository.get(id));
    }


    public List<PermissionRightDto> findAllPermissions() {
        List<PermissionRightDto> res = new ArrayList<>();
        Map<String,List<Permission>> permissions = permissionRepository.findAllPermissions();


        for (Map.Entry<String, List<Permission>> entry : permissions.entrySet()) {
            PermissionRightDto.PermissionRightDtoBuilder builder = PermissionRightDto.builder();
            List<RightDto> rights = entry.getValue().stream().map(permission -> RightDto.builder()
                    .id(permission.getId())
                    .right(permission.getRight())
                    .description(permission.getDescription())
                    .build()).collect(Collectors.toList());

            builder.permission(entry.getKey()).rights(rights);
            res.add(builder.build());

        }

        return res;

    }

    @Transactional
    public List<PermissionRightDto> findPermissionsByRoleId(Integer roleId) {
        Map<String,List<Permission>> permissions = permissionRepository.findPermissionsByRoleId(roleId);
        return getPermissionRightDtos(permissions);
    }


    public Boolean existProfile(Integer id, String newName) {
        return profileRepository.existsByName(id, newName);
    }


    public Boolean existRole(Integer id, String newName) {
        return roleRepository.existsByName(id,newName);
    }

    @Transactional
    public void saveProfile(ProfileBean profileBean) throws JAXBException{
        Profile profile = ProfileDismantler.buildProfile(profileBean);
        profileRepository.save(profile);
    }


    public List<RoleBean> getAllRole() {
        return RoleBeanBuilder.build(roleRepository.getAll());
    }


    @Transactional
    public void deleteProfile(Integer idProfile) {
        Profile profile = profileRepository.get(idProfile);
        List<User> users =userRepository.findUsersWithProfile(idProfile);
        for (User user : users) {
            user.getProfiles().remove(profile);
        }
        profileRepository.remove(profile);
    }

    @Transactional
    public void createProfile(ProfileBean profile) {
        List<RoleBean> roles = profile.getRoles()
                                            .stream()
                                            .filter(roleBean -> roleBean.getId() != null)
                                            //.map(roleBean -> roleBean.getId())
                                            .collect(Collectors.toList());

        profile.setRoles(roles);
        profileRepository.save(ProfileDismantler.buildProfile(profile));

    }

    @Transactional
    public void deleteRoleById(Integer id) {
        roleRepository.remove(id);
    }


    @Transactional
    public RoleBean getRole(Integer id) {
        return RoleBeanBuilder.build(roleRepository.get(id));
    }

    @Transactional
    public List<PermissionRightDto> getAllPermissions() {
        Map<String,List<Permission>> permissions = permissionRepository.findAllPermissions();
        return getPermissionRightDtos(permissions);
    }

    private List<PermissionRightDto> getPermissionRightDtos(Map<String, List<Permission>> permissions) {
        List<PermissionRightDto> res = new ArrayList<>();
        for (Map.Entry<String, List<Permission>> entry : permissions.entrySet()) {
            PermissionRightDto.PermissionRightDtoBuilder builder = PermissionRightDto.builder();
            List<RightDto> rights = entry.getValue().stream().map(permission -> RightDto.builder()
                    .id(permission.getId())
                    .right(permission.getRight())
                    .description(permission.getDescription())
                    .build()).collect(Collectors.toList());

            builder.permission(entry.getKey()).rights(rights);
            res.add(builder.build());

        }
        return res;
    }

    @Transactional
    public void saveRole(RoleBean role) {
        List<PermissionBean> permissions = role.getPermissions()
                .stream()
                .filter(roleBean -> roleBean.getId() != null)
                .collect(Collectors.toList());

        role.setPermissions(permissions);
        roleRepository.save(RoleDismantler.buildRole(role));
    }

    public DataTableResponse<PermissionDTO> findByQuery(DataTableQuery<PermissionCommand> query, List<String> relationObject) throws Exception {
        return DatatableQueryUtils.query(query, relationObject, permissionDTOBuilder, permissionRepository);
    }


    @Transactional
    public Page<RoleBean> findRoleByName(String query, Pageable pageable) {
        Page<Role> roles = roleRepository.findByNameLike(query, pageable);

        List<RoleBean> content = RoleBeanBuilder.build(roles.getContent());

        return new PageImpl<>(content, pageable, roles.getTotalElements());
    }


    @Transactional
    public void addPermissionToRole(Integer permissionId, Integer roleId) {
        if(permissionId==null){
            throw new IllegalArgumentException("PermissionId null");
        }

        if(roleId==null){
            throw new IllegalArgumentException("roleId null");
        }

        Permission permission = permissionRepository.get(permissionId);
        if(permission == null){
            throw new IllegalArgumentException("La Permission n'existe pas");
        }

        Role role = roleRepository.get(roleId);
        if(role == null){
            throw new IllegalArgumentException("le Role n'existe pas");
        }

        role.getPermissions().add(permission);

    }
}
