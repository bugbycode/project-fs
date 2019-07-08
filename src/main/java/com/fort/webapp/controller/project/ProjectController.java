package com.fort.webapp.controller.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fort.module.project.Project;
import com.fort.service.project.ProjectService;
import com.util.tree.ProjectTree;

@Validated
@Controller
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;

	@RequestMapping(value = "/query",method = {RequestMethod.GET})
	public String query() {
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
		projectService.delete(id);
		return "redirect:/project/query";
	}
	
	@RequestMapping(value = "/queryById",method = {RequestMethod.GET})
	public Project queryById(@RequestParam(name = "id",defaultValue="0") int id) {
		return projectService.queryById(id);
	}
}
