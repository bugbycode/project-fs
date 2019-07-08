package com.fort.service.project.impl;

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
		return projectDao.insert(pro);
	}

	@Override
	public void update(Project pro) {
		projectDao.update(pro);
	}

	@Override
	public void delete(int id) {
		projectDao.delete(id);
	}

}
