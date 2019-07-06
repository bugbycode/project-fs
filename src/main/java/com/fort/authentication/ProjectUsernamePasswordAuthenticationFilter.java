package com.fort.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.util.StringUtil;

public class ProjectUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(StringUtil.isEmpty(username)) {
			throw new BadCredentialsException("用户名密码错误");
		}
		if(StringUtil.isEmpty(password)) {
			throw new BadCredentialsException("用户名密码错误");
		}
		return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}

}
