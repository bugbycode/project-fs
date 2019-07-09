package com.fort.webapp.controller.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fort.module.project.Project;
import com.fort.module.project.ProjectMemo;
import com.fort.service.project.ProjectService;
import com.util.tree.ProjectTree;

@Validated
@Controller
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;

	@RequestMapping(value = "/query",method = {RequestMethod.GET})
	public String query(@ModelAttribute("p")Project pro) {
		return "pages/project/manager";
	}
	
	@RequestMapping(value = "/projectTree",method = {RequestMethod.GET})
	@ResponseBody
	public String projectTree(){
		List<Project> list = projectService.query();
		return new ProjectTree(list).data().toString();
	}
	
	@RequestMapping(value = "/insert",method = {RequestMethod.POST})
	public String insert(@Validated @ModelAttribute("p")Project pro) {
		projectService.insert(pro);
		return "redirect:/project/query";
	}
	
	@RequestMapping(value = "/update",method = {RequestMethod.POST})
	public String update(@Validated @ModelAttribute("p")Project pro) {
		projectService.update(pro);
		return "redirect:/project/query";
	}
	
	@RequestMapping(value = "/delete",method = {RequestMethod.POST})
	public String delete(@RequestParam(name = "id",defaultValue="0") int id) {
		if(id > 0) {
			Project pro = projectService.queryById(id);
			if(!(pro == null || pro.getType() == 0)) {
				List<Project> list = projectService.queryByParentId(pro.getId());
				if(CollectionUtils.isEmpty(list)) {
					projectService.delete(id);
				}
			}
		}
		return "redirect:/project/query";
	}
	
	@RequestMapping(value = "/queryById",method = {RequestMethod.GET})
	public Project queryById(@RequestParam(name = "id",defaultValue="0") int id) {
		return projectService.queryById(id);
	}
	
	@RequestMapping(value = "/checkDel",method = {RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> checkDel(@RequestParam(name = "id",defaultValue="0") int id){
		Map<String,Object> result = new HashMap<String,Object>();
		int code = 0;
		if(id == 0) {
			code = 1;
		}
		Project pro = projectService.queryById(id);
		if(pro != null) {
			List<Project> list = projectService.queryByParentId(pro.getId());
			if(!CollectionUtils.isEmpty(list)) {
				code = 2;
			}
		}
		result.put("code", code);
		return result;
	}
}
