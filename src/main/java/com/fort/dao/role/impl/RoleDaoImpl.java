package com.fort.dao.role.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fort.dao.base.BaseDao;
import com.fort.dao.role.RoleDao;
import com.fort.module.role.Role;

@Repository("roleDao")
public class RoleDaoImpl extends BaseDao implements RoleDao {

	@Override
	public Role queryById(int roleId) {
		return getSqlSession().selectOne("role.queryById", roleId);
	}

	@Override
	public Role queryByName(String roleName) {
		return getSqlSession().selectOne("role.queryByName", roleName);
	}

	@Override
	public List<Role> query(Map<String, Object> params) {
		return getSqlSession().selectList("role.query", params);
	}

	@Override
	public int count(Map<String, Object> params) {
		return getSqlSession().selectOne("role.count", params);
	}

	@Override
	public int insert(Role r) {
		return getSqlSession().insert("role.insert", r);
	}

	@Override
	public void update(Role r) {
		getSqlSession().update("role.update", r);
	}

	@Override
	public void delete(int roleId) {
		getSqlSession().delete("role.delete", roleId);
	}

}
