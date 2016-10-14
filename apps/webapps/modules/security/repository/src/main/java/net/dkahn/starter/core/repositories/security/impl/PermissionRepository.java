package net.dkahn.starter.core.repositories.security.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import net.dkahn.starter.core.repositories.security.IPermissionRepository;
import net.dkahn.starter.core.repositories.security.filtering.PermissionFilter;
import net.dkahn.starter.domains.security.Permission;
import net.dkahn.starter.domains.security.QPermission;
import net.dkahn.starter.domains.security.QRole;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;
import net.dkahn.starter.tools.repository.utils.ExpressionHelp;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:12
 * Goal:
 */
@Repository
public class PermissionRepository extends GenericRepositoryJpa<Permission,Integer> implements IPermissionRepository {


    @Override
    public Map<String,List<Permission>> findPermissionsByRoleId(Integer roleId) {
        JPAQuery query = new JPAQuery(getEntityManager());

        QRole role = QRole.role;
        QPermission permission = QPermission.permission;

        query.select(permission.name, permission).from(role)
                .join(role.permissions,permission)
                .groupBy(permission,permission.name)
                .where(role.id.eq(roleId));

        return createMapPermissions(query.fetch());
    }

    @Override
    public Map<String, List<Permission>> findAllPermissions() {
        JPAQuery query = new JPAQuery(getEntityManager());

        QPermission permission = QPermission.permission;

        query.select(permission.name, permission).from(permission)
                .groupBy(permission, permission.name);

        return createMapPermissions(query.fetch());
    }



    private Map<String, List<Permission>> createMapPermissions(List<Tuple> list) {
        Map<String,List<Permission>> permissionMap = new HashMap<>();

        for (Tuple tuple : list) {
            String permissionName = tuple.get(QPermission.permission.name);
            Permission permissionObj = tuple.get(QPermission.permission);
            List<Permission> permissions = permissionMap.get(permissionName);
            if(permissions==null){
                permissions = new ArrayList<>();
            }
            permissions.add(permissionObj);
            permissionMap.put(permissionName,permissions);
        }
        return permissionMap;
    }

    @Override
    public Page<Permission> find(PermissionFilter filter, Sort sort, Pageable pageable) {
        QPermission permission = QPermission.permission;
        QRole role = QRole.role;

        JPAQuery query = createQuery();
        query.from(permission);


        ExpressionHelp expressionHelp = ExpressionHelp.instance();


        if (filter.getAttachedRole()!=null) {
            if (filter.getAttachedRole()) {
               expressionHelp.andExp(
                       JPAExpressions.selectFrom(role).where(role.permissions.contains(permission)).exists()
                );

            } else {
                expressionHelp.andExp(
                        JPAExpressions.selectFrom(role).where(role.permissions.contains(permission)).exists().not()
                );
            }
        }

        if (!StringUtils.isEmpty(filter.getDescription())){
            expressionHelp.andExp(permission.description.containsIgnoreCase(filter.getDescription()));
        }

        if (!StringUtils.isEmpty(filter.getPermission())){
            expressionHelp.andExp(permission.name.containsIgnoreCase(filter.getPermission()));
        }

        if (!StringUtils.isEmpty(filter.getRight())){
            expressionHelp.andExp(permission.right.containsIgnoreCase(filter.getRight()));
        }

        if (filter.getId()!=null){
            expressionHelp.andExp(permission.id.eq(filter.getId()));
        }



        if(expressionHelp.hasExp()){
            query.where(expressionHelp.getComputeExp());
        }


        long count = query.fetchCount();


        applyPagination(pageable, query);

        List<Permission> list = query.select(permission).fetch();

        return new PageImpl<>(list,pageable,count);

    }
}
