package com.fort.service.project.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fort.dao.project.ProjectDao;
import com.fort.module.project.Project;
import com.fort.service.project.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectDao projectDao;
	
	@Override
	public List<Project> query() {
		return projectDao.query();
	}

	@Override
	public Project queryById(int id) {
		return projectDao.queryById(id);
	}

	@Override
	public int insert(Project pro) {
		pro.setCreateTime(new Date());
		pro.setType(1);
		return projectDao.insert(pro);
	}

	@Override
	public void update(Project pro) {
		pro.setUpdateTime(new Date());
		projectDao.update(pro);
	}

	@Override
	public void delete(int id) {
		projectDao.delete(id);
	}

	@Override
	public List<Project> queryByParentId(int parentId) {
		return projectDao.queryByParentId(parentId);
	}

}
