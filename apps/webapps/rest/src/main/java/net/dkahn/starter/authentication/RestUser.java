package net.dkahn.starter.authentication;

import lombok.Getter;
import lombok.Setter;
import net.dkahn.starter.domains.security.User;
import net.dkahn.starter.services.security.model.DefaultUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;


@Getter
@Setter
public class RestUser extends DefaultUserDetails {

    RestUser(User user, List<GrantedAuthority> grantedAuthorities) {
        super(user, grantedAuthorities);
    }
}
