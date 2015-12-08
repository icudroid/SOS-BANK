package net.dkahn.starter.apps.webapps.modules.security.bean.dismantler;


import net.dkahn.starter.apps.webapps.modules.security.bean.ProfileBean;
import net.dkahn.starter.domains.security.Profile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 15/01/15
 * Time: 10:31
 * Goal:
 */
public class ProfileDismantler {



    public static List<Profile> buildProfiles(List<ProfileBean> profiles){
        return  profiles
                .stream()
                .map(ProfileDismantler::buildProfile)
                .collect(Collectors.toList());
    }


    public static  Profile buildProfile(ProfileBean profile){
        return
                Profile.builder()
                        .id(profile.getId())
                        .name(profile.getName())
                        .description(profile.getDescription())
                        .roles(RoleDismantler.buildRoles(profile.getRoles()))
                        .build();
    }








}
