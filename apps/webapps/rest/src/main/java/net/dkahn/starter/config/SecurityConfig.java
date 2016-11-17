/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dkahn.starter.config;

import net.dkahn.starter.authentication.provider.PinpadAuthenticationProvider;
import net.dkahn.starter.authentication.provider.UsernamePinpadPasswordAuthenticationFilter;
import net.dkahn.starter.services.security.IPinpadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author Rob Winch
 * @author Vedran Pavic
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Resource
	private UserDetailsService authenticationUserService;

	@Resource
	private IPinpadService pinpadService;

	@Resource
	private DataSource dataSource;

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new StandardPasswordEncoder();
	}


	@Bean(name = "authenticationFilter")
	public UsernamePinpadPasswordAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager){
		UsernamePinpadPasswordAuthenticationFilter filter = new UsernamePinpadPasswordAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}





	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		PinpadAuthenticationProvider authenticationProvider = new PinpadAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(authenticationUserService);
		authenticationProvider.setPinpadService(pinpadService);
		auth.authenticationProvider(authenticationProvider);
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/login").permitAll()
				.antMatchers("/pinpad").permitAll()
				.antMatchers("/pinpad/*/img").permitAll()
				.anyRequest().authenticated()
			.and()
				.requestCache()
					.requestCache(new NullRequestCache())
			.and()
				.httpBasic()
			;
	}


}
