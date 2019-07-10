package com.util.tree;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fort.module.project.Project;
import com.util.StringUtil;

public class ProjectGrantTree {

	private List<Project> list;
	
	private Project root;
	
	public String grant;
	
	public ProjectGrantTree(List<Project> list,String grant) {
		this.list = list;
		this.grant = grant;
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
		JSONObject state = new JSONObject();
		state.put("disabled", true);
		obj.put("state", state);
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
			if(StringUtil.isNotEmpty(this.grant)) {
				String idStr = String.valueOf(pro.getId());
				if(this.grant.startsWith(idStr + ",") || this.grant.endsWith("," + idStr)
						|| this.grant.contains("," + idStr + ",") || this.grant.equals(idStr)) {
					JSONObject state = new JSONObject();
					state.put("selected", true);
					obj.put("state", state);
				}
			}
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
