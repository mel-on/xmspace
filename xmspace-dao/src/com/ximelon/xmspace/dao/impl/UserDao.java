package com.ximelon.xmspace.dao.impl;

import org.springframework.stereotype.Repository;

import com.ximelon.xmspace.dao.IUserDao;
import com.ximelon.xmspace.dbbean.User;

@Repository("userDao")
public class UserDao extends HibernateDao<User, String> implements IUserDao {

}
