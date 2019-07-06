package com.fort.webapp.controller.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fort.module.role.Role;
import com.fort.service.role.RoleService;
import com.util.page.SearchResult;

@Controller
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/query",method = {RequestMethod.GET})
	public String query(ModelMap model,
			@RequestParam(name = "paramQuery",defaultValue="")
			String paramQuery,
			@RequestParam(name = "startIndex",defaultValue="0")
			int offset,
			@RequestParam(name = "pageSize",defaultValue="10")
			int limit) {
		SearchResult<Role> sr = roleService.query(paramQuery, offset, limit);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		return "pages/role/list";
	}
	
	@RequestMapping(value = "/edit",method = {RequestMethod.GET})
	public String edit(@ModelAttribute("r") Role r,@RequestParam(name = "id",defaultValue = "0")int id) {
		r.setType(1);
		if(id > 0) {
			Role role = roleService.queryById(id);
			if(role != null) {
				r.copy(role);
			}
		}
		return "pages/role/edit";
	}
}
