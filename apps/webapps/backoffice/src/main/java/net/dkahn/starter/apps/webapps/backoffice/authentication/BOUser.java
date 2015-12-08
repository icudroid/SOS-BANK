package net.dkahn.starter.apps.webapps.backoffice.authentication;

import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.services.security.model.DefaultUserDetails;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;


@Getter
@Setter
public class BOUser extends DefaultUserDetails {

    public BOUser(User user, List<GrantedAuthority> grantedAuthorities) {
        super(user, grantedAuthorities);
    }
}
