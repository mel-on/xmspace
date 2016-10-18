package com.ximelon.xmspace.web.cache;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局缓存内容的更新器
 * 
 * @author chenjunwen
 * @since 2015-08-23
 */
public class GlobalCacheUpdateThread implements Runnable {

	private Logger logger = LoggerFactory.getLogger(GlobalCacheUpdateThread.class);

	private Timers userTime = new Timers();

	private static final long BASE_TIME = 60 * 1000; // 1分钟,基准时间

	protected GlobalCacheService service;
	protected TimesConfig timesConfig;

	public GlobalCacheUpdateThread(GlobalCacheService service, TimesConfig config) {
		this.service = service;
		userTime.setTimes(config.getUserTimes());
	}

	@Override
	public void run() {
		updateUser();
		a: while (true) {
			try {
				Thread.sleep(BASE_TIME);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("通知规则数据更新线程休眠出错,线程结束!" + e.getMessage());
				break a;
			}
			if (isTimeArrive(userTime))
				updateUser();
		}

	}
	
	/**
	 * 更新USER对象
	 */
	private void updateUser() {
		service.initUsersCache();
	}

	/*
	 * 时间计数器 ,以一分钟为一个最小单位
	 */
	class Timers {
		private int times; // 计时次数

		private int currentTime; // 当前计时次数

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}

		public int getCurrentTime() {
			return currentTime;
		}

		public void setCurrentTime(int currentTime) {
			this.currentTime = currentTime;
		}
	}
	
	private boolean isTimeArrive(Timers timers) {
		if (timers.getCurrentTime() + 1 == timers.getTimes()) {
			timers.setCurrentTime(0);
			return true;
		} else {
			timers.setCurrentTime(timers.getCurrentTime() + 1);
			return false;
		}
	}

}
