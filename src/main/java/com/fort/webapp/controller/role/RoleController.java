package com.fort.webapp.controller.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fort.module.role.Role;
import com.fort.service.role.RoleService;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Validated
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
	
	@RequestMapping(value = "/checkRoleName",method = {RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> checkRoleName(String roleName){
		Map<String,Object> result = new HashMap<String,Object>();
		Role r = roleService.queryByName(roleName);
		int roleId = 0;
		if(r != null) {
			roleId = r.getId();
		}
		result.put("roleId", roleId);
		return result;
	}
	
	@RequestMapping(value = "/insert",method = {RequestMethod.POST})
	public String insert(@Validated @ModelAttribute("r") Role r) {
		Role role = roleService.queryByName(r.getName());
		if(role != null) {
			throw new RuntimeException("该角色名称已存在");
		}
		roleService.insert(r);
		return "redirect:/role/query";
	}
	
	@RequestMapping(value = "/update",method = {RequestMethod.POST})
	public String update(@Validated @ModelAttribute("r") Role r) {
		Role role = roleService.queryByName(r.getName());
		if(!(role == null || role.getId() == r.getId())) {
			throw new RuntimeException("该角色名称已存在");
		}
		roleService.update(r);
		return "redirect:/role/query";
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public String delete(@RequestParam(name="ids",defaultValue="")String ids) {
		List<Integer> idList = new ArrayList<Integer>();
		if(StringUtil.isNotBlank(ids)) {
			Set<String> set = StringUtils.commaDelimitedListToSet(ids);
			for(String id : set) {
				idList.add(Integer.valueOf(id));
			}
		}
		Role r;
		for(Integer id : idList) {
			r = roleService.queryById(id);
			if(r == null || r.getType() == 0) {
				continue;
			}
			roleService.delete(id);
		}
		return "redirect:/role/query";
	}
}
