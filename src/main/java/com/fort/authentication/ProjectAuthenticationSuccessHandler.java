package com.fort.authentication;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fort.config.RoleConfig;
import com.fort.module.employee.Employee;
import com.util.RoleUtil;

public class ProjectAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String forwardUrl = "";
		Employee emp = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		@SuppressWarnings("unchecked")
		Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) emp.getAuthorities();
		if(RoleUtil.chekcRole(RoleConfig.USER_QUERY, authorities)
				|| RoleUtil.chekcRole(RoleConfig.USER_INSERT, authorities)
				|| RoleUtil.chekcRole(RoleConfig.USER_UPDATE, authorities)
				|| RoleUtil.chekcRole(RoleConfig.USER_DELETE, authorities)
				) {
			forwardUrl = "/employee/query";
		}else if(RoleUtil.chekcRole(RoleConfig.ROLE_QUERY, authorities)
				|| RoleUtil.chekcRole(RoleConfig.ROLE_INSERT, authorities)
				|| RoleUtil.chekcRole(RoleConfig.ROLE_UPDATE, authorities)
				|| RoleUtil.chekcRole(RoleConfig.ROLE_DELETE, authorities)
				) {
			forwardUrl = "/role/query";
		}else if(RoleUtil.chekcRole(RoleConfig.PROJECT_QUERY, authorities)
				|| RoleUtil.chekcRole(RoleConfig.PROJECT_INSERT, authorities)
				|| RoleUtil.chekcRole(RoleConfig.PROJECT_UPDATE, authorities)
				|| RoleUtil.chekcRole(RoleConfig.PROJECT_DELETE, authorities)
				|| RoleUtil.chekcRole(RoleConfig.PROJECT_FILE_QUERY, authorities)
				|| RoleUtil.chekcRole(RoleConfig.PROJECT_FILE_DELETE, authorities)
				|| RoleUtil.chekcRole(RoleConfig.PROJECT_FILE_UPLOAD, authorities)
				|| RoleUtil.chekcRole(RoleConfig.PROJECT_FILE_DOWNLOAD, authorities)
				) {
			forwardUrl = "/project/query";
		}
		request.getSession().setAttribute("forwardUrl", forwardUrl.substring(1));
		redirectStrategy.sendRedirect(request, response, forwardUrl);
	}

}
