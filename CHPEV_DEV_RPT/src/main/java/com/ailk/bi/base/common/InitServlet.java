package com.ailk.bi.base.common;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class InitServlet {
	public  static void init(ServletConfig servletConfig,Object object,String beanName) {
		try {
			    ServletContext servletContext = servletConfig.getServletContext();
			    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			    AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
			    autowireCapableBeanFactory.configureBean(object,beanName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
