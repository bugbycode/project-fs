package com.fort.dao.role;

import java.util.List;
import java.util.Map;

import com.fort.module.role.Role;

public interface RoleDao {

	public Role queryById(int roleId);
	
	public Role queryByName(String roleName);
	
	public List<Role> query(Map<String,Object> params);
	
	public int count(Map<String,Object> params);

	public int insert(Role r);

	public void update(Role r);

	public void delete(int roleId);
}
