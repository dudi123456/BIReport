package com.ailk.bi.sigma.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fins.gt.server.GridServerHandler;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "unused", "rawtypes" })
public class SigmaGridDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3151526074367816330L;
	private PrintWriter out;

	public PrintWriter getOut(HttpServletResponse response) {
		if (out == null) {
			try {
				setOut(response.getWriter());
			} catch (IOException e) {
				e.printStackTrace();
				// LogHandler.error(this,e);
			}
		}
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		GridServerHandler gridServerHandler = new GridServerHandler(request,
				response);

		// 取得总记录数
		int totalRowNum = gridServerHandler.getTotalRowNum();
		// 如果总数不存在, 那么调用"相应的方法" 来取得总数
		if (out == null) {
			try {
				setOut(response.getWriter());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String sigmaId = request.getParameter("sigmaId");
		String sql = "select SQL_DEFINE,SQL_CONDITION,SQL_ORDERBY,REMOTE_PAGE from ui_sigma_define_base where SIGMA_ID="
				+ sigmaId;
		System.out.println(sql);

		try {
			String res[][] = WebDBUtil.execQryArray(sql, "");
			if (res != null && res.length > 0) {
				String strSelect = res[0][0] + " " + res[0][1] + " "
						+ res[0][2];
				// System.out.println(strSelect);
				String sqlCnt = " Select count(*) from (" + strSelect + ")";
				if (totalRowNum < 1) {
					totalRowNum = getRecordCount(sqlCnt);
					// 将取得的总数赋给gridServerHandler
					gridServerHandler.setTotalRowNum(totalRowNum);
				}

				// List list =
				// WebDBUtil.getBeans_withSortSupport_limit_general(gridServerHandler.getStartRowNum(),gridServerHandler.getPageSize(),
				// strSelect);
				List list = null;
				String remotePage = StringB.NulltoBlank(res[0][3]);

				if (remotePage.equalsIgnoreCase("true")) {
					list = WebDBUtil.getBeans_withSortSupport_limit_general(
							gridServerHandler.getStartRowNum(),
							gridServerHandler.getPageSize(), strSelect);
				} else {
					list = WebDBUtil
							.getBeans_withSortSupport_limit_general(strSelect);
				}

				// List list =
				// WebDBUtil.getBeans_withSortSupport_limit_general(strSelect);
				gridServerHandler.setData(list);
				// print(gridServerHandler.getLoadResponseText(),response);
				// System.out.println(gridServerHandler.getLoadResponseText());
				out.print(gridServerHandler.getLoadResponseText());

			}

		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doGet(req, resp);
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public SigmaGridDataServlet() {
		super();
	}

	public void init() throws ServletException {
		// Put your code here
	}

	private int getRecordCount(String sql) {
		try {
			String res[][] = WebDBUtil.execQryArray(sql, "");
			if (res != null && res.length > 0) {
				return Integer.parseInt(res[0][0]);
			} else {
				return 0;
			}
		} catch (AppException e) {

			e.printStackTrace();
			return 0;
		}

	}

	private void print(Object text, HttpServletResponse resp) {

		getOut(resp).print(String.valueOf(text));
	}
}
