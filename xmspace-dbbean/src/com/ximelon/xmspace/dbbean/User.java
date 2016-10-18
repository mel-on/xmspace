package com.ximelon.xmspace.dbbean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity 
@Table(name="t_user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;// 主键
	private String userId;//登录ID
	private String name;// 用户名 
	private String password;// 密码
	private String email;// 邮箱地址
	private String telephone;// 固定电话
	private String mobilePhone;// 手机
	private boolean enabled;// 用户状态   
	private int isAdmin;// 是否管理员 0：不是    1：是
	private Organization organization;// 所属组织
	private Set<Role> roles = new HashSet<Role>();// 所属角色
	private String formatOrgName;// 层级关系的组织名字串
	private int userPorperty;
	private int isDel;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@Column(name="MOBILE_PHONE")
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Column(name="IS_ADMIN")
	public int getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	@ManyToOne
	@JoinColumn(name = "ORG_ID")
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	@ManyToMany(cascade={CascadeType.PERSIST,CascadeType.REFRESH},fetch=FetchType.EAGER)
	@JoinTable(name="T_USER_ROLE",
		   joinColumns={@JoinColumn(name="USER_ID",referencedColumnName="ID")},
		   inverseJoinColumns={@JoinColumn(name="ROLE_ID",referencedColumnName="ID")})
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role){
		if(!this.roles.contains(role)){
			this.roles.add(role);
		}
	}
	
	public void removeRole(Role role){
		this.roles.remove(role);
	}
	
	@Transient
	public String getFormatOrgName() {
		return formatOrgName;
	}
	public void setFormatOrgName(String formatOrgName) {
		this.formatOrgName = formatOrgName;
	}

	@Column(name="USER_PROPERTY")
	public int getUserPorperty() {
		return userPorperty;
	}
	public void setUserPorperty(int userPorperty) {
		this.userPorperty = userPorperty;
	}
	
	@Column(name = "IS_DEL")
	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

}






