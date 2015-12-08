package net.dkahn.starter.apps.webapps.modules.security.bean.builder;


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
public class ProfileBeanBuilder {



    public static List<ProfileBean> build(List<Profile> profiles){
        return profiles
                .stream()
                .map(ProfileBeanBuilder::build)
                .collect(Collectors.toList());
    }


    public static  ProfileBean build(Profile profile){
        return
                ProfileBean.builder()
                        .id(profile.getId())
                        .name(profile.getName())
                        .description(profile.getDescription())
                        .roles(RoleBeanBuilder.build(profile.getRoles()))
                        .build();
    }



}
