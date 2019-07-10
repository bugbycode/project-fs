package com.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class RoleUtil {

	public static boolean chekcRole(String role,Collection<? extends GrantedAuthority> authorities) {
		role = "ROLE_" + role;
		for(GrantedAuthority grant : authorities) {
			if(role.equals(grant.getAuthority())) {
				return true;
			}
		}
		return false;
	}
}
