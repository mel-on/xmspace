package com.ximelon.xmspace.util;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ximelon.xmspace.web.security.LoginWrongRecord;


/**
 * 用户登录错误记录器
 * 
 * @author chenjunwen
 * @since 2015-08-18
 */
public class LoginRecordCache {
	
	private static LoginRecordCache instance;
	
	private final Map<String, LoginWrongRecord> map = new ConcurrentHashMap<String, LoginWrongRecord>();
	
	public static synchronized LoginRecordCache getInstance() {
		if (null == instance)
			instance = new LoginRecordCache();
		return instance;
	}

	public Map<String, LoginWrongRecord> getMap() {
		return map;
	}

}
