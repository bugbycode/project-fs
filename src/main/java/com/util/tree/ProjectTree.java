package com.util.tree;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fort.module.project.Project;

public class ProjectTree {
	
	private List<Project> list;
	
	private Project root;
	
	public ProjectTree(List<Project> list) {
		this.list = list;
		this.root = new Project();
		this.root.setName("项目信息");
		this.root.setId(0);
	}
	
	public JSONArray data() {
		JSONArray result = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("id", root.getId());
		obj.put("text", root.getName());
		obj.put("children", getChileJsonArray(root));
		result.add(obj);
		return result;
	}
	
	
	public JSONArray getChileJsonArray(Project parent) {
		JSONArray result = new JSONArray();
		List<Project> childList = findChildren(parent.getId());
		for(Project pro : childList) {
			JSONObject obj = new JSONObject();
			obj.put("id", pro.getId());
			obj.put("text", pro.getName());
			
			JSONArray children = getChileJsonArray(pro);
			if(!children.isEmpty()) {
				obj.put("children", children);
			}
			result.add(obj);
		}
		return result;
	}
	
	public List<Project> findChildren(int parentId){
		List<Project> children = new ArrayList<Project>();
		if(!list.isEmpty()) {
			for(Project pro : list) {
				if(pro.getParentId() == parentId) {
					children.add(pro);
				}
			}
		}
		return children;
	}
	
}
