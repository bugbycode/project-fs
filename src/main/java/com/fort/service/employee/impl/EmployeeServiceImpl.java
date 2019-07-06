package com.fort.service.employee.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fort.dao.employee.EmployeeDao;
import com.fort.module.employee.Employee;
import com.fort.service.employee.EmployeeService;
import com.util.AESUtil;
import com.util.StringUtil;
import com.util.page.Page;
import com.util.page.SearchResult;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public Employee login(String username, String password) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("username", username);
		params.put("password", password);
		return employeeDao.queryByUserNameAndPassword(params);
	}

	@Override
	public SearchResult<Employee> query(String paramQuery, int offset, int limit) {
		SearchResult<Employee> sr = new SearchResult<Employee>();
		Page page = new Page(limit, offset);
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotEmpty(paramQuery)) {
			params.put("paramQuery", paramQuery);
		}
		params.put("offset", offset);
		params.put("limit", limit);
		int totalCount = employeeDao.count(params);
		if(totalCount > 0) {
			page.setTotalCount(totalCount);
			sr.setList(employeeDao.query(params));
		}
		sr.setPage(page);
		return sr;
	}

	@Override
	public Employee queryById(int empId) {
		return employeeDao.queryById(empId);
	}

	@Override
	public Employee queryByUserName(String userName) {
		return employeeDao.queryByUserName(userName);
	}

	@Override
	public int insert(Employee emp) {
		emp.setCreateTime(new Date());
		emp.setType(1);
		String password = emp.getPassword();
		if(StringUtil.isEmpty(password)) {
			throw new RuntimeException("用户密码不能为空");
		}
		password = AESUtil.encrypt(password);
		emp.setPassword(password);
		return employeeDao.insert(emp);
	}

	@Override
	public void update(Employee emp) {
		emp.setUpdateTime(new Date());
		String password = emp.getPassword();
		if(StringUtil.isNotEmpty(password)) {
			password = AESUtil.encrypt(password);
			emp.setPassword(password);
		}
		employeeDao.update(emp);
	}

	@Override
	public void delete(int empId) {
		employeeDao.delete(empId);
	}

}
