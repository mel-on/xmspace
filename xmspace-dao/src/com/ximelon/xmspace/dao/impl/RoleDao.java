package com.ximelon.xmspace.dao.impl;

import org.springframework.stereotype.Repository;

import com.ximelon.xmspace.dao.IRoleDao;
import com.ximelon.xmspace.dbbean.Role;

/**
 * 用户角色dao
 * 
 */
@Repository("roleDao")
public class RoleDao extends HibernateDao<Role, Long> implements IRoleDao {

}
