package com.fort.service.role;

import java.util.List;

import com.fort.module.role.Role;
import com.util.page.SearchResult;

public interface RoleService {

	public Role queryById(int roleId);
	
	public Role queryByName(String roleName);
	
	public SearchResult<Role> query(String paramQuery,int offset,int limit);
	
	public List<Role> query(String roleName);

	public int insert(Role r);

	public void update(Role r);

	public void delete(Integer roleId);
}
