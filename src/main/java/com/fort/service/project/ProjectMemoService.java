package com.fort.service.project;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fort.module.project.ProjectMemo;
import com.util.page.SearchResult;

public interface ProjectMemoService {

	public List<ProjectMemo> query(Map<String,Object> params);
	
	public SearchResult<ProjectMemo> search(int projectId,String grant,String keyword,int offset,int limit);
	
	public void update(int id,String description,Date updateTime);
	
	public void insert(ProjectMemo pm);
	
	public void delete(int id);
	
	public ProjectMemo queryById(int id);
	
	public List<ProjectMemo> queryByProjectId(int projectId);
}
