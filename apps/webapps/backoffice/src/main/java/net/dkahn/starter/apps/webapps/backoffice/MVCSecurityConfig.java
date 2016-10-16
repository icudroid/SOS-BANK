package net.dkahn.starter.apps.webapps.backoffice;

import net.dkahn.starter.apps.webapps.common.filter.StoreIpFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.sql.DataSource;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 8)
public class MVCSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserDetailsService authenticationUserService;

    @Resource
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new StandardPasswordEncoder();
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
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
                .antMatchers("/").authenticated()
                .antMatchers("/login").permitAll()
                .antMatchers("/static/**").permitAll()
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


        http.rememberMe()
                .userDetailsService(authenticationUserService)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(1209600);
    }

    @Bean
    public Filter storeIpFilter() {
        return new StoreIpFilter();
    }



    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler
    savedRequestAwareAuthenticationSuccessHandler() {

        SavedRequestAwareAuthenticationSuccessHandler auth
                = new SavedRequestAwareAuthenticationSuccessHandler();
        auth.setTargetUrlParameter("targetUrl");
        return auth;
    }

}
