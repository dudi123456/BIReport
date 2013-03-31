package com.ailk.bi.sigma.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HelloAction extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Configuration configuration;

	public void init() throws ServletException {
		configuration = new Configuration();

		configuration.setServletContextForTemplateLoading(getServletContext(),
				"/jsp/ftl");
		configuration.setEncoding(Locale.CHINA, "UTF-8");
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Map map = new HashMap();
		map.put("me", "小敏2");
		map.put("WEBROOT_PATH", request.getContextPath());

		Template template = configuration.getTemplate("test.ftl");

		response.setContentType("text/html; charset=" + template.getEncoding());

		Writer out = response.getWriter();

		try {
			template.process(map, out);
		} catch (TemplateException e) {
			e.printStackTrace();
		}

		// request.getRequestDispatcher("/jsp/ftl/test.ftl").forward(request,
		// response);

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doGet(req, resp);
	}

	public void destroy() {
		super.destroy();
		if (configuration != null) {
			configuration = null;
		}
	}
}
