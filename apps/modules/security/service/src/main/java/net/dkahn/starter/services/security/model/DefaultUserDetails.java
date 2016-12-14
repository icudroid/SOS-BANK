package net.dkahn.starter.services.security.model;

import net.dkahn.starter.domains.security.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public abstract class DefaultUserDetails implements UserDetails {

    protected User user;

    protected  List<GrantedAuthority> grantedAuthorities;

    public DefaultUserDetails(User user, List<GrantedAuthority> grantedAuthorities) {
        if(user==null ) throw new IllegalArgumentException("user object must be not null");
        this.user=user;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public Long getId(){
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        LocalDateTime now= LocalDateTime.now();
        return user.getExpirationDate()==null || user.getExpirationDate().isAfter(now);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getBlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getEnabled();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }


}
