package com.ximelon.xmspace.dao.impl;


import org.springframework.stereotype.Repository;

import com.ximelon.xmspace.dao.IMenuDao;
import com.ximelon.xmspace.dbbean.Menu;

@Repository
public class MenuDao extends HibernateDao<Menu,Long> implements IMenuDao {

}
