package com.ximelon.xmspace.web.filter;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;


/**
 * 安全过滤
 *     针对session，cookie作设置，提高安全性
 * @author chenjunwen
 * @since 201-09-06
 */
public class SpringApplicationContextHelperFilter extends GenericFilterBean {

	private static Logger logger = LoggerFactory.getLogger(SpringApplicationContextHelperFilter.class);
	
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        /**
         * 在Cookie中设置HttpOnly属性为true，兼容浏览器接收到HttpOnly cookie，
         * 那么客户端通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这将有助于缓解跨站点脚本威胁。
         */
		response.setHeader("Set-Cookie", "name=value; HttpOnly");
		
		/**
		 * 设置 X-Frame-Options 阻止站点内的页面被其他页面嵌入从而防止点击劫持。
		 * 1、DENY：不能被嵌入到任何iframe或者frame中。 
		 * 2、SAMEORIGIN:页面只能被本站页面嵌入到iframe或者frame中。 
		 * 3、ALLOW-FROM uri：只能被嵌入到指定域名的框架中。
		 */
		response.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
		
		/* JSESSIONID设置 */
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (null != c && "JSESSIONID".equals(c.getName())) {
					response.setHeader("Set-Cookie", "JSESSIONID=" + c.getValue() + "; Secure; HttpOnly");
				}
			}
		}
		
        logger.debug(request.getRequestURI());
        chain.doFilter(request, response);
    }
}
