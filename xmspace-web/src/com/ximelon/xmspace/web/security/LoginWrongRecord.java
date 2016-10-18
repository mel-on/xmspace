package com.ximelon.xmspace.web.security;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户锁定实体类
 * 
 * @author chenjunwen
 * @since 2015-08-18
 */
public class LoginWrongRecord {

	private String userId;										// 用户ID
	private AtomicInteger wrongCount = new AtomicInteger(1);	// 登录失败次数
	private boolean lock;										// 是否被锁
	private long lockTime;										// 锁定时间

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public AtomicInteger getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(AtomicInteger wrongCount) {
		this.wrongCount = wrongCount;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public long getLockTime() {
		return lockTime;
	}

	public void setLockTime(long lockTime) {
		this.lockTime = lockTime;
	}

}
