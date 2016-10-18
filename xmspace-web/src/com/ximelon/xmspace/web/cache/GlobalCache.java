package com.ximelon.xmspace.web.cache;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ximelon.xmspace.dbbean.User;

/**
 * 全局缓存器
 *    此部分信息都放到机器的内存中，随应用启动加载
 *    
 * @author chenjunwen
 * @since 2015-08-23
 */
public class GlobalCache {

	private static GlobalCache instance;

	/* 用户信息缓存 */
	private Map<String, User> userMap = new ConcurrentHashMap<String, User>();

	public static GlobalCache getInstance() {
		if (instance == null)
			instance = new GlobalCache();
		return instance;
	}

	public Map<String, User> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, User> userMap) {
		this.userMap = userMap;
	}

}
