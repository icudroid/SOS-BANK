package net.dkahn.starter.core.repositories.security.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import net.dkahn.starter.core.repositories.security.IProfileRepository;
import net.dkahn.starter.domains.security.*;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:12
 * Goal:
 */
@Repository
public class ProfileRepository extends GenericRepositoryJpa<Profile,Integer> implements IProfileRepository {

    @Override
    public List<GrantedAuthority> loadGrantedAuthorities(User user) {
        JPAQuery query = new JPAQuery(getEntityManager());

        QProfile profile = QProfile.profile;
        QRole role = QRole.role;
        QPermission permission = QPermission.permission;

        query.select(permission).from(profile) .join(profile.roles, role)
                            .join(role.permissions,permission)
                .where(profile.in(user.getProfiles()));

        return  getAuthorities(query.fetch());
    }


    @Override
    public  List<GrantedAuthority> loadGrantedAnonymousAuthorities(){
        JPAQuery query = new JPAQuery(getEntityManager());

        QProfile profile = QProfile.profile;
        QRole role = QRole.role;
        QPermission permission = QPermission.permission;

        query.select(permission).from(profile) .join(profile.roles, role)
                .join(role.permissions,permission)
                .where(profile.name.eq(IProfileRepository.ANONYMOUS));

        return  getAuthorities(query.fetch());
    }

    private List<GrantedAuthority> getAuthorities(List<Permission> list) {
        List<GrantedAuthority> permissionsResult = new ArrayList<>();

        for (Permission p_action : list) {
            StringBuilder perm = new StringBuilder();
            perm.append("ROLE_").append(p_action.getName());
            if(p_action.getRight()!=null){
                perm.append("_").append(p_action.getRight());
            }

                permissionsResult.add(new SimpleGrantedAuthority(perm.toString()));
        }
        return permissionsResult;
    }

    @Override
    public Boolean existsByName(Integer id,String newName) {
        JPAQuery<Profile> query = new JPAQuery(getEntityManager());

        QProfile profile = QProfile.profile;


        BooleanExpression exp = profile.name.equalsIgnoreCase(newName);
        if(id != null){
            exp = exp.and(profile.id.ne(id));
        }

        query.from(profile).where(exp);

        return query.fetchCount() >=1  ;

    }

    @Override
    public List<Profile> findByIds(List<Integer> profileIds) {

        JPAQuery<Profile> query = createQuery();

        QProfile profile = QProfile.profile;

        query.select(profile).from(profile).where(profile.id.in(profileIds));

        return query.fetch();
    }

    @Override
    public Profile findByName(String profileName) {
        JPAQuery<Profile> query = createQuery();
        QProfile profile = QProfile.profile;
        query.select(profile).from(profile).where(profile.name.in(profileName));
        return  query.fetchOne();
    }



}
