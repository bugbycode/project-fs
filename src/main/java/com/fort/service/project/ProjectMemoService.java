package com.fort.service.project;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fort.module.project.ProjectMemo;

public interface ProjectMemoService {

	public List<ProjectMemo> query(Map<String,Object> params);
	
	public void update(int id,String description,Date updateTime);
	
	public void insert(ProjectMemo pm);
	
	public void delete(int id);
	
	public ProjectMemo queryById(int id);
	
	public List<ProjectMemo> queryByProjectId(int projectId);
}
