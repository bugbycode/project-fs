package com.fort.dao.project.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fort.dao.base.BaseDao;
import com.fort.dao.project.ProjectMemoDao;
import com.fort.module.project.ProjectMemo;

@Repository("projectMemoDao")
public class ProjectMemoDaoImpl extends BaseDao implements ProjectMemoDao {
	
	@Override
	public List<ProjectMemo> query(Map<String, Object> params) {
		return getSqlSession().selectList("pm.query", params);
	}

	@Override
	public void update(Map<String, Object> params) {
		getSqlSession().update("pm.update", params);
	}

	@Override
	public void insert(ProjectMemo pm) {
		getSqlSession().insert("pm.insert", pm);
	}

	@Override
	public void delete(int id) {
		getSqlSession().delete("pm.delete", id);
	}

	@Override
	public ProjectMemo queryById(int id) {
		return getSqlSession().selectOne("pm.queryById", id);
	}

}
