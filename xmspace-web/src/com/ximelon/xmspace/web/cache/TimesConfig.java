package com.ximelon.xmspace.web.cache;



/**
 * 时间更新配置（单位：分钟）
 * 
 * @author chenjunwen
 * @since 2015-08-23
 */
public class TimesConfig {

	/* 用户信息(默认3小时) */
	private int userTimes = 60 * 3;

	public int getUserTimes() {
		return userTimes;
	}

	public void setUserTimes(int userTimes) {
		this.userTimes = userTimes;
	}

	private static TimesConfig instance;

	public TimesConfig() {
		instance = this;
	}

	public static TimesConfig getInstance() {
		return instance;
	}

	@Override
	public String toString() {
		return "TimesConfig [userTimes=" + userTimes + "]";
	}

}
