package com.ximelon.xmspace.web.security;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.ximelon.xmspace.dbbean.User;
import com.ximelon.xmspace.util.LoginRecordCache;
import com.ximelon.xmspace.util.SpringApplicationContextHelper;
import com.ximelon.xmspace.web.WebConst;
import com.ximelon.xmspace.web.service.impl.UserManagementService;

public class UsernamePasswordAuthenticationFilter
		extends
		org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter {
	
	private Logger logger = LoggerFactory.getLogger(UsernamePasswordAuthenticationFilter.class);
	
	// 登录失败次数限制
	private static final int tryLoginTimes = 5;
	// 登录失败次数超过限制后的账号锁定时间（单位：分钟）
	private static final int lockTime = 60;

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		// 验证用户是否被锁
		boolean isLock = this
				.checkLoginLock(request.getParameter("j_username"));
		if (isLock) {
			String key = request.getParameter("j_username") + "wrongCount";
			LoginWrongRecord l = LoginRecordCache.getInstance().getMap().get(key);
			long leftTime = lockTime - ((System.currentTimeMillis() - l.getLockTime()) / (60 * 1000));
			leftTime = leftTime == 0l ? 1l : leftTime;
			throw new CredentialsExpiredException("账号已被锁定，请于" + leftTime + "分后再尝试登录");
		}
		
		// 不管是否登录成功，都直接清除验证码
		request.getSession().removeAttribute("RANDOMVALIDATECODEKEY");
		
		//登录成功记录用户名
		Cookie cook = null;
		Cookie cook2 = new Cookie("rememberPsw","N");
		if("Y".equals(request.getParameter("rememberPsw"))){
			cook = new Cookie("namepsw",request.getParameter("j_username")+"-"+request.getParameter("j_password"));
			cook2 = new Cookie("rememberPsw","Y");
		}else{
			cook = new Cookie("namepsw",request.getParameter("j_username")+"-");
		}
		cook.setMaxAge(60*60*24*14);
		response.addCookie(cook);
		
		cook2.setMaxAge(60*60*24*14);
		response.addCookie(cook2);
		
		return super.attemptAuthentication(request, response);
	}

	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException, ServletException {
	
		super.successfulAuthentication(request, response, authResult);
		// 添加登录用户ip信息
		WebApplicationContext webContext=WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		SecurityMetadataSource sms=webContext.getBean(SecurityMetadataSource.class);
		sms.asyncLoadMenu();
		logger.info("{}成功登陆系统",authResult.getName());
		
		//登陆成功之后判断用户选择的是哪一种语言（中文、English）
		Locale locale = (Locale) request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if(locale==null){
			locale = request.getLocale();
			request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
			request.getSession().setAttribute(WebConst.LOCALE,locale.toString());
		}

		UserManagementService userService = SpringApplicationContextHelper.getContext().getBean(UserManagementService.class);
		User user = userService.getUserByUserId(authResult.getName());
		request.getSession().setAttribute(WebConst.USER, user);
		request.getSession().setAttribute(WebConst.USER_ID, user.getId());
		this.cleanLoginWrongRecord(user.getId());
	}
	
	protected boolean checkValidateCode(HttpServletRequest request) {   
        HttpSession session = request.getSession();  
          
        String sessionValidateCode = (String)session.getAttribute("RANDOMVALIDATECODEKEY");  
        //让上一次的验证码失效  
      
        String validateCodeParameter = request.getParameter("checkcode");  
    
        if (StringUtils.isEmpty(validateCodeParameter) || !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {    
          //  throw new AuthenticationServiceException("验证码错误！");    
        	return false;
        }    
        return true;
    } 
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		String errMsg = failed.getMessage();
		if (failed instanceof BadCredentialsException) {
			String user = failed.getAuthentication().getName();
			LoginWrongRecord l = this.setLoginWrongRecord(user);
    		int leftCount = tryLoginTimes-l.getWrongCount().intValue();
    		if (leftCount == 0) {
    			errMsg = "登录尝试次数超过" + tryLoginTimes + "次，用户账号已被锁定";
    		} else {
    			errMsg = "密码不正确，还有" + leftCount + "次登录尝试";
    		}
    		failed = new BadCredentialsException(errMsg);
		} else if (failed instanceof UsernameNotFoundException) {
			errMsg = "账号不存在";
			failed = new UsernameNotFoundException(errMsg);
		} 
		super.unsuccessfulAuthentication(request, response, failed);
	} 
	
	   /**
     * 登录失败后设定登录尝试次数
     * @param user
     * @return
     */
    private LoginWrongRecord setLoginWrongRecord(String user) {
    	String key = user + "wrongCount";
    	LoginWrongRecord l = LoginRecordCache.getInstance().getMap().get(key);
		if (null == l) {
			l = new LoginWrongRecord();
			l.setUserId(user);
			LoginRecordCache.getInstance().getMap().put(key, l);
		} else {
			logger.info("用户登录用户名密码错误次数:" + l.getWrongCount().getAndIncrement());
		}
		return l;
    }
    
    /**
     * 检查用户是否被锁定
     * @param user
     * @return
     */
    private boolean checkLoginLock(String user) {
    	String key = user + "wrongCount";
    	LoginWrongRecord l = LoginRecordCache.getInstance().getMap().get(key);
		if (null == l) return false;
		if (l.isLock()) {
			// 锁定解除
			if ((System.currentTimeMillis() - l.getLockTime()) > lockTime * 60 * 1000) {
				l.setLock(false);
				l.setWrongCount(new AtomicInteger(0));
				return false;
			} else {
				return true;
			}
		} else {
			if (l.getWrongCount().intValue() == tryLoginTimes) {
				l.setLock(true);
				l.setLockTime(System.currentTimeMillis());
				return true;
			}
		}
		return false;
    }
    
    public void cleanLoginWrongRecord(String user) {
    	String key = user + "wrongCount";
    	LoginWrongRecord l = LoginRecordCache.getInstance().getMap().get(key);
    	if (null != l) {
    		l.getWrongCount().set(0);
    		l.setLock(false);
    		l.setLockTime(0l);
    	}
    }
	
}
