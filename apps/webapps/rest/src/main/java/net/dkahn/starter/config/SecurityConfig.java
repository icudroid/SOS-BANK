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

import net.dkahn.starter.authentication.provider.FailureAjaxHandler;
import net.dkahn.starter.authentication.provider.PinpadAuthenticationProvider;
import net.dkahn.starter.authentication.provider.SuccessAjaxHandler;
import net.dkahn.starter.authentication.provider.UsernamePinpadPasswordAuthenticationFilter;
import net.dkahn.starter.services.security.IPinpadService;
import net.dkahn.starter.services.security.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* Security Config
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Resource
	private UserDetailsService authenticationUserService;

	@Resource
	private IPinpadService pinpadService;

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new StandardPasswordEncoder();
	}

	@Resource
	private FailureAjaxHandler failureAjaxHandler;

	@Resource
	private SuccessAjaxHandler successAjaxHandler;

	@Resource
	private IUserService userService;


	@Bean
	public UsernamePinpadPasswordAuthenticationFilter authenticationFilter() throws Exception {
		UsernamePinpadPasswordAuthenticationFilter filter = new UsernamePinpadPasswordAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(successAjaxHandler);
		filter.setAuthenticationFailureHandler(failureAjaxHandler);
		return filter;
	}




	@Bean
	public LogoutSuccessHandler logoutHandler() {
		return new HttpStatusReturningLogoutSuccessHandler();
	}



	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		PinpadAuthenticationProvider authenticationProvider = new PinpadAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(authenticationUserService);
		authenticationProvider.setPinpadService(pinpadService);
		authenticationProvider.setUserService(userService);
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
				.addFilterBefore(authenticationFilter(),UsernamePasswordAuthenticationFilter.class)
				.formLogin().loginPage("/login")
			.and().logout().logoutSuccessHandler(logoutHandler())
			.and()
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
			.and()
				.httpBasic()
		;
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint(){

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		};
	}


}
