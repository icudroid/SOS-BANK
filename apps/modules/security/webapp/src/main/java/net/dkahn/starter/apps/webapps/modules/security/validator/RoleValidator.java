package net.dkahn.starter.apps.webapps.modules.security.validator;

import net.dkahn.starter.apps.webapps.modules.security.bean.RoleBean;
import net.dkahn.starter.core.repositories.security.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * User: dimitri
 * Date: 16/01/15
 * Time: 13:05
 * Goal:
 */
@Component
public class RoleValidator implements Validator {

    @Autowired
    private IRoleRepository roleRepository;



    @Override
    public boolean supports(Class<?> clazz) {
        return RoleBean.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoleBean role = (RoleBean) target;

        if(StringUtils.isEmpty(role.getName())){
            errors.rejectValue("name","required");
        }else if(roleRepository.existsByName(role.getId(), role.getName())){
            errors.rejectValue("name","exist");
        }

        if(StringUtils.isEmpty(role.getDescription())){
            errors.rejectValue("description","required");
        }

    }
}
