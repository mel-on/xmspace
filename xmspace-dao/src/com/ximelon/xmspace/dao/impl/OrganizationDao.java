package com.ximelon.xmspace.dao.impl;

import org.springframework.stereotype.Repository;

import com.ximelon.xmspace.dao.IOrganizationDao;
import com.ximelon.xmspace.dbbean.Organization;

/**
 * 组织架构dao
 *
 */
@Repository("organizationDao")
public class OrganizationDao extends HibernateDao<Organization, Long> implements IOrganizationDao {
    
}
