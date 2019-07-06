package com.fort.webapp.controller.employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fort.module.employee.Employee;
import com.fort.module.role.Role;
import com.fort.service.employee.EmployeeService;
import com.fort.service.role.RoleService;
import com.util.StringUtil;
import com.util.page.SearchResult;

@Validated
@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
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
		SearchResult<Employee> sr = employeeService.query(paramQuery, offset, limit);
		model.put("list", sr.getList());
		model.put("page", sr.getPage());
		return "/pages/employee/list";
	}
	
	@RequestMapping(value = "/edit",method = {RequestMethod.GET})
	public String edit(@ModelAttribute("emp") Employee emp,
			@RequestParam(name = "id",defaultValue = "0")int id) {
		emp.setType(1);
		if(id > 0) {
			Employee e = employeeService.queryById(id);
			if(e != null) {
				emp.copy(e);
			}
		}
		return "pages/employee/edit";
	}
	
	@RequestMapping(value = "/queryRole",method = {RequestMethod.GET})
	@ResponseBody
	public List<Role> queryRole(String roleName) {
		return roleService.query(roleName);
	}
	
	@RequestMapping(value = "/insert",method = {RequestMethod.POST})
	public String insert(@Validated @ModelAttribute("emp") Employee emp) {
		Employee e = employeeService.queryByUserName(emp.getUsername());
		if(e != null) {
			throw new RuntimeException("该用户名已被使用");
		}
		int roleId = emp.getRoleId();
		if(roleId > 0) {
			Role r = roleService.queryById(roleId);
			if(r == null) {
				emp.setRoleId(0);
			}
		}
		employeeService.insert(emp);
		return "redirect:/employee/query";
	}
	
	@RequestMapping(value = "/update",method = {RequestMethod.POST})
	public String update(@Validated @ModelAttribute("emp") Employee emp) {
		Employee user = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int empId = emp.getId();
		if(empId == 0) {
			return "redirect:/employee/query";
		}
		Employee e = employeeService.queryById(empId);
		if(e == null) {
			return "redirect:/employee/query";
		}
		emp.setType(e.getType());
		if(e.getType() == 0 || empId == user.getId()) {
			emp.setStatus(e.getStatus());
			emp.setRoleId(e.getRoleId());
		}
		employeeService.update(emp);
		return "redirect:/employee/query";
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public String delete(@RequestParam(name="ids",defaultValue="")String ids) {
		Employee user = (Employee)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Integer> idList = new ArrayList<Integer>();
		if(StringUtil.isNotBlank(ids)) {
			Set<String> set = StringUtils.commaDelimitedListToSet(ids);
			for(String id : set) {
				idList.add(Integer.valueOf(id));
			}
		}
		Employee tmp;
		for(Integer id : idList) {
			tmp = employeeService.queryById(id);
			if(tmp == null || tmp.getType() == 0 || tmp.getId() == user.getId()) {
				continue;
			}
			employeeService.delete(id);
		}
		return "redirect:/employee/query";
	}
	
	@RequestMapping(value = "/checkUserName",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> checkUserName(String userName){
		Map<String,Object> result = new HashMap<String,Object>();
		Employee emp = employeeService.queryByUserName(userName);
		int empId = 0;
		if(emp != null) {
			empId = emp.getId();
		}
		result.put("empId", empId);
		return result;
	}
}
