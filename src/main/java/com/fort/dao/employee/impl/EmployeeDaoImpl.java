package com.fort.dao.employee.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fort.dao.base.BaseDao;
import com.fort.dao.employee.EmployeeDao;
import com.fort.module.employee.Employee;

@Repository("employeeDao")
public class EmployeeDaoImpl extends BaseDao implements EmployeeDao {
	
	@Override
	public Employee queryByUserNameAndPassword(Map<String, Object> params) {
		return getSqlSession().selectOne("employee.queryByUserNameAndPassword", params);
	}

	@Override
	public List<Employee> query(Map<String, Object> params) {
		return getSqlSession().selectList("employee.query", params);
	}

	@Override
	public int count(Map<String, Object> params) {
		return getSqlSession().selectOne("employee.count", params);
	}

	@Override
	public Employee queryById(int empId) {
		return getSqlSession().selectOne("employee.queryById", empId);
	}

	@Override
	public Employee queryByUserName(String userName) {
		return getSqlSession().selectOne("employee.queryByUserName", userName);
	}

	@Override
	public int insert(Employee emp) {
		return getSqlSession().insert("employee.insert", emp);
	}

	@Override
	public void update(Employee emp) {
		getSqlSession().update("employee.update", emp);
	}

	@Override
	public void delete(int empId) {
		getSqlSession().delete("employee.delete", empId);
	}

}
