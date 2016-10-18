/**
 * ==========================================================
 * Sequence     Author       Date          Description
 *    1          hsq 	    2008-2-19        ����
 * ========================================================== 
 */

package com.ximelon.xmspace.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author hsq
 * @version 1.0
 */

public class SpringContextLoaderListener extends ContextLoaderListener {

	/**
	 * @see org.springframework.web.context.ContextLoaderListener#contextInitialized(javax.servlet.ServletContextEvent)
	 *
	 *      Description:
	 * @param
	 * @return
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ServletContext servletContext = event.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		SpringApplicationContextHelper.setContext(ctx);
	}

	/**
	 * @see org.springframework.web.context.ContextLoaderListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 *
	 *      Description:
	 * @param
	 * @return
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}
}
