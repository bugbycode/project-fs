package com.fort.module.employee;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.fort.module.role.Role;

public class Employee implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2928409438698851832L;

	private int id;			//用户ID
	
	@NotEmpty(message="姓名不能为空")
	@Size(min=2,max=20,message="姓名长度不能少于两个字符且不能超过二十个字符")
	@Pattern(regexp="^[A-Za-z0-9\\u4e00-\\u9fa5]*$",message="姓名不能包含英文特殊字符")
	private String name;        //姓名
	
	@NotEmpty(message="用户名不能为空")
	@Size(min=2,max=20,message="用户名长度不能少于两个字符且不能超过二十个字符")
	@Pattern(regexp="^[A-Za-z0-9_-]*$",message="用户名不能包含除大小写字母、数字、下划线以及横杠以外的字符")
	private String username;    //用户名或手机号码
	
	@Size(min=0,max=16,message="密码长度不能超过十六个字符")
	private String password;    //密码
	
	@Size(min=0,max=30,message="Email长度不能超过三十个字符")
	@Email(message="Email格式不正确")
	private String email;		//电子邮件
	
	@Max(value=1,message="用户状态必须是0或者1（1：激活，0：锁定）")
	@Min(value=0,message="用户状态必须是0或者1（1：激活，0：锁定）")
	private int status;			//状态 0:锁定，1:激活
	
	private int type;			
	
	private Date createTime;	//创建时间
	
	private Date updateTime;    //更新时间
	
	private int roleId;
	
	private Role role;
	
	private String roleName;
	
	private String projectGrant;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authoritie = new HashSet<GrantedAuthority>();
		Set<String> roleSet = (this.role == null || this.role.getGrantedAuthority() == null) 
				? new HashSet<String>() : StringUtils.commaDelimitedListToSet(this.role.getGrantedAuthority());
		for(String grant : roleSet) {
			authoritie.add(new SimpleGrantedAuthority("ROLE_" + grant));
		}
		return authoritie;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getProjectGrant() {
		return projectGrant;
	}

	public void setProjectGrant(String projectGrant) {
		this.projectGrant = projectGrant;
	}

	public void copy(Employee e) {
		this.setId(e.getId());
		this.setName(e.getName());
		this.setUsername(e.getUsername());
		this.setPassword(e.getPassword());
		this.setStatus(e.getStatus());
		this.setType(e.getType());
		this.setEmail(e.getEmail());
		this.setCreateTime(e.getCreateTime());
		this.setUpdateTime(e.getUpdateTime());
		this.setRoleId(e.getRoleId());
		this.setRoleName(e.getRoleName());
	}
}
