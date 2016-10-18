package com.ximelon.xmspace.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ximelon.xmspace.dao.IGenericDao;
import com.ximelon.xmspace.dao.IOrganizationDao;
import com.ximelon.xmspace.dao.IRoleDao;
import com.ximelon.xmspace.dao.IUserDao;
import com.ximelon.xmspace.dbbean.Organization;
import com.ximelon.xmspace.dbbean.Role;
import com.ximelon.xmspace.dbbean.User;
import com.ximelon.xmspace.web.bean.TreeNode;
import com.ximelon.xmspace.web.bean.UserCondition;

/**
 * 用户管理service
 * @author zhuwenxin
 *
 */
@Service("userManagementService")
public class UserManagementService {
	
	@Autowired
	private IUserDao userDao; 
	@Autowired
	private IRoleDao roleDao;
	@Autowired
	private IGenericDao genericDao;
	@Autowired
	private IOrganizationDao organizationDao;
	
	
	/**
	 * 根据条件查询用户
	 * @param condition 用户查询条件
	 * @return 用户List集合
	 */
	public List<User> getUserListByCondition(UserCondition condition,int userPorperty){
		String name = condition.getNameQuery();// 用户名
		String orgId = condition.getOrgIdQuery();// 组织id
		
		String hql = "";
		Map<String,Object> params = new HashMap<String,Object>();
		
		if(userPorperty == 1){
			 hql = "from User u where u.isDel<>1";
		}else{
			 hql = "from User u where u.isDel<>1 and u.userPorperty =:userPorperty ";
			 params.put("userPorperty", userPorperty);
		}
		
		if(name != null && !"".equals(name.trim())){
			hql += " and u.name like '%"+name+"%'";
		}
		if(orgId != null && !"".equals(orgId.trim())){
			Set<Long> orgIdSet = getAllChildOrgId(orgId);
			orgIdSet.add(Long.valueOf(orgId));
			hql += " and u.organization.id in (:orgIds)";
			params.put("orgIds", orgIdSet);
		}
		hql += " order by convert_gbk(u.organization.orgName),convert_gbk(u.name)";
		
		List<User> result = userDao.find(hql,params);
		return result;
	}
	
	/**
	 * 递归，获取用户组id下所有的子用户组id
	 * @param orgId
	 * @return
	 */
	private Set<Long> getAllChildOrgId(String orgId){
		Set<Long> orgIdSet = new HashSet<Long>();
		//查找该组织的子组织
		List<Organization> childOrg = getOrgByPid(orgId); 
		
		for(Organization org : childOrg){
			orgIdSet.add(org.getId());
			Set<Long> childSet = this.getAllChildOrgId(org.getId().toString());
			orgIdSet.addAll(childSet);
		}
		return orgIdSet;
	}
	
	/**
	 * 新增或更新一个用户
	 * @param user 用户实体
	 */
	public void saveOrUpdateUser(User user){
		if(user.getId()!=null) user.setId( user.getId().trim() );
		if(user.getName()!=null) user.setName( user.getName().trim() );
		if(user.getMobilePhone()!=null) user.setMobilePhone( user.getMobilePhone().trim() );
		if(user.getTelephone()!=null) user.setTelephone( user.getTelephone().trim() );
		userDao.saveOrUpdate(user);
	}
	
	/**
	 * 删除用户
	 * @param 逗号隔开的id字符串
	 */
	public void deleteUser(String userIds){
		String sql = "delete from T_USER where id in ('"+userIds+"')";
		userDao.executeSql(sql);
	}
	
	/**
	 * 根据主键id取得用户
	 * @param userId 用户id
	 * @return 匹配该id的用户
	 */
	public User getUserById(String userId){
		return userDao.get(userId);
	}
	
	/**
	 * 根据userid取得用户
	 * @param userId 用户id
	 * @return 匹配该id的用户
	 */
	public User getUserByUserId(String userId){
		return userDao.findOneByHql("from User u where u.userId = ?", User.class, userId);
	}
	
	
	/**
	 * 根据组织id查找用户
	 * @param orgId 组织id
	 * @return 用户list
	 */
	public List<User> getUsersByOrgId(String orgId){
		String hql = "from User u where u.organization.id="+orgId;
		return userDao.find(hql);
	}
	
	/**
	 * 删除用户
	 * @param userId
	 */
	public void removeUser(String userId) {
//		User u = userDao.get(userId);
//		userDao.delete(u);
		String sql = "UPDATE t_user t set t.IS_DEL=1 where t.ID=?";
		userDao.executeSql(sql, userId);
	}
	
	/**
	 * 查询所有的用户角色
	 * @return 用户角色List集合
	 */
	public List<Role> getAllRoleList(){
		return roleDao.getAll();
	}
	
	/**
	 * 查询某用户创建的角色
	 * @param userId 用户id
	 * @return 角色集合
	 */
	public List<Role> getRolesByUser(String userId){
		String hql = "from Role where creator.id='"+userId+"'";
		return roleDao.find(hql);
	}
	
	/**
	 * 根据主键id取得组织
	 * @param orgId 主键
	 * @return 组织
	 */
	public Organization getOrgById(long orgId){
		return organizationDao.get(orgId);
	}
	
	/**
	 * 删除组织
	 * @param org 组织
	 */
	public void delOrg(Organization org){
		organizationDao.delete(org);
	}
	
	/**
	 * 新增或更新组织
	 * @param org 组织
	 */
	public void saveOrUpdateOrg(Organization org){
		if(org.getOrgName()!=null) org.setOrgName( org.getOrgName().trim() );
		organizationDao.saveOrUpdate(org);
	}
	
	/**
	 * 根据父级id和组织名称查找组织
	 * @param pId 父级id
	 * @param orgName 组织名称
	 * @return 组织list
	 */
	public List<Organization> getOrgByPidAndName(String pId, String orgName){
		String hql = "from Organization o where o.parent.id="+pId+" and o.orgName='"+orgName+"'";
		return organizationDao.find(hql);
	} 
	
	/**
	 * 根据父级id查找下级节点
	 * @param pid 父级id
	 * @return 子节点list
	 */
	public List<Organization> getOrgByPid(String pid){
		String hql = "from Organization o where o.parent.id="+pid;
		return organizationDao.find(hql);
	}
	
	/**
	 * 根据父级id取得组织架构的树形数据
	 * @param pId 父级id
	 * @return 组织架构的树形数据
	 */
	public TreeNode getOrgTreeData(String pId){
		TreeNode result = new TreeNode();
		
		Organization parentOrg = organizationDao.get( Long.parseLong(pId) );
		result.setId(parentOrg.getId().toString()); 
		result.setName(parentOrg.getOrgName());
		
		//子树组织
		List<Organization> childList = organizationDao.find( "select o from Organization o where o.parent.id="+pId+" order by convert_gbk(o.orgName)" );
		int size = childList.size();
		if( size>0 ){//存在子节点
			result.setIsParent(true);
			result.setIconSkin("pIcon01");
		}
		
		for(Organization org : childList){
			String id = org.getId().toString();
			//System.out.println("***********"+id);
			result.addChildren( getOrgTreeData(id) );//递归增加当前节点下的子节点
		}
		
		return result;
	}
	
	public TreeNode getEditOrgTree(String orgId,String pId){
		TreeNode result = new TreeNode();
		
		Organization parentOrg = organizationDao.get( Long.parseLong(pId) );
		result.setId(parentOrg.getId().toString()); 
		result.setName(parentOrg.getOrgName());
		
		//子树组织
		List<Organization> childList = organizationDao.find( "select o from Organization o where o.parent.id="+pId );
		int size = childList.size();
		if( size>0 ){//存在子节点
			result.setIsParent(true);
			result.setIconSkin("pIcon01");
		}
		
		for(Organization org : childList){
			String id = org.getId().toString();			
			TreeNode node = getEditOrgTree(orgId,id);
			if (orgId.equals(node.getId())) {
				continue;
			}
			result.addChildren( node );//递归增加当前节点下的子节点
		}
		
		return result;
	}
	
	
	/**
	 * 返回用户树
	 * @param pId
	 * @return
	 */
	public TreeNode getUserTreeData(Long pId) {
		TreeNode result = new TreeNode();
		
		Organization parentOrg = organizationDao.get(pId);
		result.setId(parentOrg.getId().toString()); 
		result.setName(parentOrg.getOrgName());
		//取当前组织下的用户
		List<User> users = getUsersByOrgId(pId.toString());
		List<TreeNode> userNodes = new ArrayList<TreeNode>();
		if(users.size() > 0) {
			for(User u: users) {
				TreeNode node = new TreeNode();
				node.setId(u.getId());
				node.setName(u.getName());
				node.setIsParent(false);
				node.setNocheck(false);
				userNodes.add(node);
			}
			result.setChildren(userNodes);
		}
		result.setNocheck(true);
		
		//子树组织
		List<Organization> childList = organizationDao.find( "select o from Organization o where o.parent.id=" + pId);
		int size = childList.size();
		if(size > 0) {//存在子节点
			result.setIsParent(true);
		}
		
		for(Organization org: childList) {
			String id = org.getId().toString();
			//System.out.println("***********"+id);
			result.addChildren(getUserTreeData(Long.parseLong(id)));//递归增加当前节点下的子节点
		}
		
		return result; 
	}
	
	public List<User> getEffectiveUser(){
		return this.userDao.find(" from User where enabled=true ");
	}
	
	
}









