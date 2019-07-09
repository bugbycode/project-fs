package com.fort.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fort.authentication.ProjectAuthenticationFailureHandler;
import com.fort.authentication.ProjectAuthenticationSuccessHandler;
import com.fort.authentication.ProjectUsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final String defaultFailureUrl = "/login?error";
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/imgCode").permitAll()
		
		.antMatchers("/employee/query","/employee/edit","/employee/checkUserName",
				"/employee/queryRole")
		.hasAnyRole(RoleConfig.USER_QUERY,
				RoleConfig.USER_INSERT,RoleConfig.USER_UPDATE,RoleConfig.USER_DELETE) //主界面所有登录用户均可访问
		.antMatchers("/employee/insert").hasRole(RoleConfig.USER_INSERT)
		.antMatchers("/employee/update").hasRole(RoleConfig.USER_UPDATE)
		.antMatchers("/employee/delete").hasRole(RoleConfig.USER_DELETE)
		
		//角色管理
		.antMatchers("/role/query","/role/edit","/role/checkRoleName").hasAnyRole(RoleConfig.ROLE_QUERY,
				RoleConfig.ROLE_INSERT,RoleConfig.ROLE_UPDATE,RoleConfig.ROLE_DELETE)
		.antMatchers("/role/insert").hasRole(RoleConfig.ROLE_INSERT)
		.antMatchers("/role/update").hasRole(RoleConfig.ROLE_UPDATE)
		.antMatchers("/role/delete").hasRole(RoleConfig.ROLE_DELETE)
		
		//项目信息
		.antMatchers("/project/*").hasAnyRole(RoleConfig.USER_QUERY)
		
		
		.and().headers().frameOptions().disable()
		
		//用户登录页面 所有人均可访问
		.and().formLogin().loginPage("/login").permitAll()
		.and().logout().invalidateHttpSession(true)
		.and().addFilterBefore(getUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean("authenticationFailureHandler")
	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return new ProjectAuthenticationFailureHandler(defaultFailureUrl);
	}
	
	@Bean("authenticationSuccessHandler")
	public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
		return new ProjectAuthenticationSuccessHandler();
	}
	
	@Bean
	public UsernamePasswordAuthenticationFilter getUsernamePasswordAuthenticationFilter() {
		UsernamePasswordAuthenticationFilter authFilter = new ProjectUsernamePasswordAuthenticationFilter();
		authFilter.setAuthenticationManager(authenticationManager);
		authFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		authFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		return authFilter;
	}
}
