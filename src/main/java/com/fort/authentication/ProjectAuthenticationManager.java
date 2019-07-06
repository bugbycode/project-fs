package com.fort.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.fort.module.employee.Employee;
import com.fort.module.role.Role;
import com.fort.service.employee.EmployeeService;
import com.fort.service.role.RoleService;
import com.util.AESUtil;

@Service("authenticationManager")
public class ProjectAuthenticationManager implements AuthenticationManager {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private RoleService roleService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		password = AESUtil.encrypt(password);
		Employee emp = employeeService.login(username, password);
		if(emp == null) {
			throw new BadCredentialsException("用户名密码错误");
		}
		if(emp.getStatus() == 0) {
			throw new LockedException("该账号已被锁定");
		}
		if(emp.getRoleId() == 0) {
			throw new BadCredentialsException("该账号未授权登录系统");
		}
		Role role = roleService.queryById(emp.getRoleId());
		if(role == null) {
			throw new BadCredentialsException("该账号未授权登录系统");
		}
		emp.setRole(role);
		return new UsernamePasswordAuthenticationToken(emp, password,emp.getAuthorities());
	}

}
