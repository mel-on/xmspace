package com.ximelon.xmspace.dbbean;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="t_menu")
public class Menu implements Serializable {
	
	private Long id;
	private String name;
	private String url;
	private Integer menuLevel;
	private Long pid;
	private Boolean enabled;


	private String icon;
//	private String operDefine;
	private Integer orderNum;
	private String menuType;
	private String menuTypeInfo;
	


	private Set<Role> roles;
	

	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Column(name="menu_level")
	public Integer getMenuLevel() {
		return menuLevel;
	}
	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	
//	@Transient
//	@Deprecated
//	public String getOperDefine() {
//		return operDefine;
//	}
//	@Deprecated
//	public void setOperDefine(String operDefine) {
//		this.operDefine = operDefine;
//	}
	
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	
	@Column(name="order_num")
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name="menu_type")
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	
	@Column(name="menu_type_info")
	public String getMenuTypeInfo() {
		return menuTypeInfo;
	}
	public void setMenuTypeInfo(String menuTypeInfo) {
		this.menuTypeInfo = menuTypeInfo;
	}
	
	
	@OneToMany(cascade=CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name="T_ROLE_MENU",
	   joinColumns={@JoinColumn(name="MENU_ID",referencedColumnName="ID")},
	   inverseJoinColumns={@JoinColumn(name="ROLE_ID",referencedColumnName="ID")})
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}
