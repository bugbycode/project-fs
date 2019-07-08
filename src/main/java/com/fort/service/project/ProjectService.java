package com.fort.service.project;

import java.util.List;

import com.fort.module.project.Project;

public interface ProjectService {
	
	public List<Project> query();
	
	public Project queryById(int id);
	
	public int insert(Project pro);
	
	public void update(Project pro);
	
	public void delete(int id);
}
