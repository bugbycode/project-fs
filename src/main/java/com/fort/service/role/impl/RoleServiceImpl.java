package com.fort.service.role.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fort.dao.role.RoleDao;
import com.fort.module.role.Role;
import com.fort.service.role.RoleService;
import com.util.page.Page;
import com.util.page.SearchResult;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public Role queryById(int roleId) {
		return roleDao.queryById(roleId);
	}

	@Override
	public Role queryByName(String roleName) {
		return roleDao.queryByName(roleName);
	}

	@Override
	public SearchResult<Role> query(String paramQuery,int offset,int limit) {
		SearchResult<Role> sr = new SearchResult<Role>();
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("paramQuery", paramQuery);
		params.put("limit", limit);
		params.put("offset", offset);
		Page page = new Page(limit, offset);
		int totalCount = roleDao.count(params);
		if(totalCount > 0) {
			page.setTotalCount(totalCount);
			sr.setList(roleDao.query(params));
		}
		sr.setPage(page);
		return sr;
	}

	@Override
	public List<Role> query(String roleName) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("roleName", roleName);
		params.put("limit", 10);
		params.put("offset", 0);
		return roleDao.query(params);
	}

}
