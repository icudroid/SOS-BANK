package net.dkahn.starter.apps.webapps.frontend;

import net.dkahn.starter.apps.webapps.common.filter.StoreIpFilter;
import net.dkahn.starter.core.repositories.security.IProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 8)
public class MVCSecurityConfig extends WebSecurityConfigurerAdapter {


    @Resource
    private UserDetailsService authenticationUserService;

    @Autowired
    private IProfileRepository profileRepository;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new StandardPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(authenticationUserService);
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/**").authenticated();

        http.formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .permitAll();

        http.logout()
                .deleteCookies("remove")
                .invalidateHttpSession(false)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");


        http.csrf().disable();

        http.addFilterBefore(storeIpFilter(), ChannelProcessingFilter.class);

        http.sessionManagement()
                            .maximumSessions(1)
                            .expiredUrl("/login?expired");

        http.anonymous()
                .authenticationFilter(anonymousAuthFilter());

    }

    @Bean
    public Filter storeIpFilter() {
        return new StoreIpFilter();
    }



    //@Bean
    public AnonymousAuthenticationFilter anonymousAuthFilter(){
        return new AnonymousAuthenticationFilter("anonymous"){

            private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource
                    = new WebAuthenticationDetailsSource();


            @Override
            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
                //if (applyAnonymousForThisRequest((HttpServletRequest) req)) {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        addAttributesToSession((HttpServletRequest)req);
                        List<GrantedAuthority> grantedAuthorities = profileRepository.loadGrantedAnonymousAuthorities();
                        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken("anonymous",  "anonymousUser", grantedAuthorities);
                        auth.setDetails(authenticationDetailsSource.buildDetails((HttpServletRequest) req));

                        SecurityContextHolder.getContext().setAuthentication(auth);

                        if (logger.isDebugEnabled()) {
                            logger.debug("Populated SecurityContextHolder with anonymous token: '"
                                    + SecurityContextHolder.getContext().getAuthentication() + "'");
                        }
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("SecurityContextHolder not populated with anonymous token, as it already contained: '"
                                    + SecurityContextHolder.getContext().getAuthentication() + "'");
                        }
                    }
                //}

                chain.doFilter(req, res);
            }

            private void addAttributesToSession(HttpServletRequest req) {
                String test = (String) req.getSession().getAttribute("test");
                logger.debug(test);
                if(test ==null) {
                    req.getSession().setAttribute("test", "test");
                }


            }
        };
    }

}
