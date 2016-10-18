package com.ximelon.xmspace.dbbean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 组织架构
 */
@Entity 
@Table(name = "T_ORGANIZATION")
public class Organization implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -869096056306088683L;
	private Long id;// id主键
    private String orgName;// 组织名称
    private String orgAddress;// 组织地址
    private String orgRemark;// 备注
    private Organization parent;// 父组织
    
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="ORG_NAME")
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	@Column(name="ORG_ADDRESS")
	public String getOrgAddress() {
		return orgAddress;
	}
	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}
	
	@Column(name="ORG_REMARK")
	public String getOrgRemark() {
		return orgRemark;
	}
	public void setOrgRemark(String orgRemark) {
		this.orgRemark = orgRemark;
	}
	
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	public Organization getParent() {
		return parent;
	}
	public void setParent(Organization parent) {
		this.parent = parent;
	}
	
	/**
	 * 组织全路径
	 * @return
	 */
	@Transient
	public String getOrgPath() {
		String fullPath = "";
		return getFullPath(this, fullPath);
	}
	
	private String getFullPath(Organization org, String orgPath) {
		if (null != this) {
			orgPath = org.getOrgName() + "/" + orgPath;
		}
		while (null != org.getParent()) {
			return getFullPath(org.getParent(), orgPath);
		}
		return orgPath;
	}
	 
}







