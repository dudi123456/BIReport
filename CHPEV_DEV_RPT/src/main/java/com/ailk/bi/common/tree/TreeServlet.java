package com.ailk.bi.common.tree;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.ailk.bi.common.tree.*;

@SuppressWarnings("serial")
public class TreeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer pid = null;
		response.setContentType("text/html;charset=GBK");

		PrintWriter p = response.getWriter();
		String id = request.getParameter("parentId");
		if (id.equals("-1")) {
			pid = null;

		} else {
			pid = Integer.parseInt(id);
		}

		TreeService treeSer = new TreeService();
		// System.out.println(treeSer.getNodesInfo(pid, request));
		p.print(treeSer.getNodesInfo(pid, request));

	}

}
