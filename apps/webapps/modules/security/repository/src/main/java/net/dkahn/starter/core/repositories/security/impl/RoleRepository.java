package net.dkahn.starter.core.repositories.security.impl;


import net.dkahn.starter.core.repositories.security.IRoleRepository;
import net.dkahn.starter.domains.security.QRole;
import net.dkahn.starter.domains.security.Role;
import net.dkahn.starter.tools.repository.jpa.GenericRepositoryJpa;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:12
 * Goal:
 */
@Repository
public class RoleRepository extends GenericRepositoryJpa<Role, Integer> implements IRoleRepository {
    @Override
    public List<Role> findAllInId(List<Integer> rolesId) {
        JPAQuery query = new JPAQuery(getEntityManager());
        QRole role = QRole.role;
        query.from(role).where(role.id.in(rolesId));
        return query.list(role);
    }

    @Override
    public boolean existsByName(Integer id, String newName) {
        JPAQuery query = new JPAQuery(getEntityManager());

        QRole role = QRole.role;


        BooleanExpression exp = role.name.equalsIgnoreCase(newName);
        if(id != null){
            exp = exp.and(role.id.ne(id));
        }

        query.from(role).where(exp);

        return query.exists();
    }

    @Override
    public Page<Role> findByNameLike(String q, Pageable pageable) {
        JPAQuery query = new JPAQuery(getEntityManager());
        QRole role = QRole.role;
        query.from(role).where(role.name.containsIgnoreCase(q));

        long count = query.count();

        applyPagination(pageable,query);

        List<Role> list = query.list(role);
        return new PageImpl<>(list,pageable,count);
    }


}
