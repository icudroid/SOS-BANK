package net.dkahn.starter.apps.webapps.modules.security.validator;

import net.dkahn.starter.apps.webapps.modules.security.bean.ProfileBean;
import net.dkahn.starter.core.repositories.security.IProfileRepository;
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
public class ProfileValidator implements Validator {

    @Autowired
    private IProfileRepository profileRepository;



    @Override
    public boolean supports(Class<?> clazz) {
        return ProfileBean.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileBean profile = (ProfileBean) target;

        if(StringUtils.isEmpty(profile.getName())){
            errors.rejectValue("name","required");
        }else if (profileRepository.existsByName(profile.getId(),profile.getName())){
            errors.rejectValue("name","exist");
        }

        if(StringUtils.isEmpty(profile.getDescription())){
            errors.rejectValue("description","required");
        }

    }
}
