package com.fort.dao.project;

import java.util.List;
import java.util.Map;

import com.fort.module.project.ProjectMemo;

public interface ProjectMemoDao {

	public List<ProjectMemo> query(Map<String,Object> params);
	
	public int count(Map<String,Object> params);
	
	public void update(Map<String,Object> params);
	
	public void insert(ProjectMemo pm);
	
	public void delete(int id);

	public ProjectMemo queryById(int id);
}
