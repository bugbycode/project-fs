package com.fort.module.project;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Project implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8378469209474039943L;

	private int id;
	
	@NotEmpty(message="项目名称不能为空")
	@Size(min=2,max=20,message="项目名称长度不能少于两个字符且不能超过二十个字符")
	//@Pattern(regexp="^[A-Za-z0-9\\u4e00-\\u9fa5]*$",message="项目名称不能包含英文特殊字符")
	private String name;
	
	private int type;
	
	private int parentId;

	private Date createTime;
	
	private Date updateTime;
	
	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
