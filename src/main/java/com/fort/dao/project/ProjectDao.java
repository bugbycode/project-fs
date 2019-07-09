package com.fort.dao.project;

import java.util.List;

import com.fort.module.project.Project;

public interface ProjectDao {
	
	public List<Project> query();
	
	public Project queryById(int id);
	
	public int insert(Project pro);
	
	public void update(Project pro);
	
	public void delete(int id);

	public List<Project> queryByParentId(int parentId);
}
