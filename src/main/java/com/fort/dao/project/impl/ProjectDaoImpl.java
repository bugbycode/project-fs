package com.fort.dao.project.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fort.dao.base.BaseDao;
import com.fort.dao.project.ProjectDao;
import com.fort.module.project.Project;

@Repository("projectDao")
public class ProjectDaoImpl extends BaseDao implements ProjectDao {

	@Override
	public List<Project> query() {
		return getSqlSession().selectList("project.query");
	}

	@Override
	public Project queryById(int id) {
		return getSqlSession().selectOne("project.queryById", id);
	}

	@Override
	public int insert(Project pro) {
		return getSqlSession().insert("project.insert", pro);
	}

	@Override
	public void update(Project pro) {
		getSqlSession().update("project.update", pro);
	}

	@Override
	public void delete(int id) {
		getSqlSession().delete("project.delete", id);
	}

	@Override
	public List<Project> queryByParentId(int parentId) {
		return getSqlSession().selectList("project.queryByParentId", parentId);
	}

}
