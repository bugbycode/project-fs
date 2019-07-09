package com.fort.service.project.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fort.dao.project.ProjectMemoDao;
import com.fort.module.project.ProjectMemo;
import com.fort.service.project.ProjectMemoService;

@Service("projectMemoService")
public class ProjectMemoServiceImpl implements ProjectMemoService {

	@Value("${spring.server.projectFilePath}")
	private String basePath;
	
	@Autowired
	private ProjectMemoDao projectMemoDao;
	
	@Override
	public List<ProjectMemo> query(Map<String, Object> params) {
		return projectMemoDao.query(params);
	}

	@Override
	public void update(int id, String description, Date updateTime) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("id", id);
		params.put("description", description);
		params.put("updateTime", updateTime);
		projectMemoDao.update(params);
	}

	@Override
	public void insert(ProjectMemo pm) {
		projectMemoDao.insert(pm);
	}

	@Override
	public void delete(int id) {
		ProjectMemo pm = queryById(id);
		if(pm == null) {
			return;
		}
		
		projectMemoDao.delete(id);
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("md5Sign", pm.getMd5Sign());
		List<ProjectMemo> list = projectMemoDao.query(params);
		if(list.isEmpty()) {
			if(!basePath.endsWith("/")) {
				basePath += "/";
			}
			
			File file = new File(basePath + pm.getMd5Sign());
			if(file.exists()) {
				file.delete();
			}
		}
	}

	@Override
	public ProjectMemo queryById(int id) {
		return projectMemoDao.queryById(id);
	}

	@Override
	public List<ProjectMemo> queryByProjectId(int projectId) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("projectId", projectId);
		return projectMemoDao.query(params);
	}

}
