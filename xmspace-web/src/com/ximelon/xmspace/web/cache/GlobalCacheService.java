package com.ximelon.xmspace.web.cache;



import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ximelon.xmspace.dao.IGenericDao;
import com.ximelon.xmspace.dbbean.User;

public class GlobalCacheService {
	
	private static Logger logger = LoggerFactory.getLogger(GlobalCacheService.class);
	
	@Autowired
	@Qualifier("timesConfig")
	private TimesConfig config;
	
	@Autowired
	private IGenericDao genericDao;
	
	public void init() {
		logger.info("开始初始化全局缓存信息，更新配置:{}", config.toString());
		GlobalCacheUpdateThread t = new GlobalCacheUpdateThread(this, config);
		Thread updateThread = new Thread(t);
		updateThread.start();
	}
	
	public void initUsersCache() {
		try {
			logger.info("开始更新所有USER对象到内存");
			List<User> list = genericDao.getAllObject(User.class);
			Map<String, User> userMap = GlobalCache.getInstance().getUserMap();
			for (User u : list) {
				userMap.put(u.getId(), u);
			}
			logger.info("更新所有USER到内存成功,个数=" + list.size());
		} catch (Exception e) {
			logger.error("更新所有USER到内存时出错!" + e.getMessage());
			e.printStackTrace();
		}
	}

	public TimesConfig getConfig() {
		return config;
	}

	public void setConfig(TimesConfig config) {
		this.config = config;
	}
	
	

}
