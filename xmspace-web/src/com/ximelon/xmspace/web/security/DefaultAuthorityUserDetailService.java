package com.ximelon.xmspace.web.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import com.ximelon.xmspace.web.WebConst;

/**
 * 为用户添加默认权限ItmpUser,该权限能访问"/**"
 *
 */
public class DefaultAuthorityUserDetailService extends JdbcDaoImpl {

	protected List<GrantedAuthority> loadUserAuthorities(String username) {
		List<GrantedAuthority> orgList = super.loadUserAuthorities(username);
		orgList.add(new GrantedAuthorityImpl(WebConst.USER_DEFAULT_AUTHORITY));
		return orgList;
	}

}
