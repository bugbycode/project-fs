package com.fort.service.employee;

import com.fort.module.employee.Employee;
import com.util.page.SearchResult;

public interface EmployeeService{
	
	public Employee login(String username,String password);
	
	public SearchResult<Employee> query(String paramQuery,int offset,int limit);
	
	public Employee queryById(int empId);
	
	public Employee queryByUserName(String userName);
	
	public int insert(Employee emp);
	
	public void update(Employee emp);
	
	public void delete(int empId);
}
