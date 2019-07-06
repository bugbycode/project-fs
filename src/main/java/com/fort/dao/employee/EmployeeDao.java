package com.fort.dao.employee;

import java.util.List;
import java.util.Map;

import com.fort.module.employee.Employee;

public interface EmployeeDao {
	
	public Employee queryByUserNameAndPassword(Map<String,Object> params);
	
	public List<Employee> query(Map<String,Object> params);
	
	public int count(Map<String,Object> params);
	
	public Employee queryById(int empId);
	
	public Employee queryByUserName(String userName);
	
	public int insert(Employee emp);
	
	public void update(Employee emp);
	
	public void delete(int empId);
}
