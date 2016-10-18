package com.ximelon.xmspace.web.bean;

/**
 * 用户查询条件
 * @author zhuwenxin
 *
 */
public class UserCondition {
	private String nameQuery;// 用户名
	private String orgIdQuery;// 组织id
	private String enabledQuery;// 用户状态   0：禁用   1：可用

	public String getNameQuery() {
		return nameQuery;
	}
	public void setNameQuery(String nameQuery) {
		this.nameQuery = nameQuery;
	}
	public String getOrgIdQuery() {
		return orgIdQuery;
	}
	public void setOrgIdQuery(String orgIdQuery) {
		this.orgIdQuery = orgIdQuery;
	}
	public String getEnabledQuery() {
		return enabledQuery;
	}
	public void setEnabledQuery(String enabledQuery) {
		this.enabledQuery = enabledQuery;
	}
	 
}





