package com.ailk.bi.common.app;

import java.util.*;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 通用的分页计算类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes" })
public class AppForum {
	/** 当前是第几页 */
	public int currentPage = 0;

	/** 一共有多少页 */
	public int totalPage = 0;

	/** 一共有多少行 */
	public int totalCount = 0;

	/** 每页有多少行 */
	public int countPerPage = 10;

	/** 当前页多少行 */
	public int countCurrentPage = 0;

	/** 装载每页的数据 */
	public Vector vData;

	/** 起始行 */
	public int startRow = 1;

	/** 结束行 */
	public int endRow = 1;

	/**
	 * 构造数据
	 * 
	 * @param vNewData
	 *            要显示的数据内容 intNewCurPage 当前页 intMaxRowCount 总行数
	 */
	public AppForum(Vector vNewData, int intNewCurPage, int intMaxRowCount) {
		vData = vNewData;
		currentPage = intNewCurPage;
		totalCount = intMaxRowCount;
		setCurrentPage(totalCount, countPerPage, currentPage);
		// countMaxPage();
		startRow = (currentPage - 1) * 10 + 1;
		endRow = startRow + countCurrentPage;
	}

	/**
	 * 构造数据
	 * 
	 * @param totalCount
	 *            总行数
	 * @param countPerPage
	 *            每页行数
	 */
	public AppForum(int totalCount, int countPerPage) {
		this.totalCount = totalCount;
		this.countPerPage = countPerPage;

		int m = this.totalCount / this.countPerPage;
		if (this.totalCount > this.countPerPage * m) {
			m++;
		}
		this.totalPage = m;
	}

	/**
	 * 设置显示内容
	 */
	public void setData(Vector vNewData) {
		vData = vNewData;
	}

	/**
	 * 根据总行数计算总页数
	 */
	public void countMaxPage() {
		if (this.totalCount % this.countCurrentPage == 0) {
			this.totalPage = this.totalCount / this.countCurrentPage;
		} else {
			this.totalPage = this.totalCount / this.countCurrentPage + 1;
		}
	}

	/**
	 * 设置（翻到）当前页
	 */
	public boolean setCurrentPage(int totalCount, int countPerPage,
			int currentPage) {
		this.totalCount = totalCount;
		this.countPerPage = countPerPage;

		int m = this.totalCount / this.countPerPage;
		if (this.totalCount > this.countPerPage * m) {
			m++;
		}
		this.totalPage = m;

		boolean b = false;
		if (currentPage == totalPage) {
			this.countCurrentPage = this.totalCount - this.countPerPage
					* (this.totalPage - 1);
		} else {
			this.countCurrentPage = this.countPerPage;
		}// if
		if (currentPage >= 1 && currentPage <= totalPage) {
			this.currentPage = currentPage;
			b = true;
		} else {
			b = false;
		}
		return b;
	}

	/**
	 * 设置（翻到）当前页
	 */
	public boolean setCurrentPage(int currentPage) {
		boolean b = false;
		if (currentPage == totalPage) {
			this.countCurrentPage = this.totalCount - this.countPerPage
					* (this.totalPage - 1);
		} else {
			this.countCurrentPage = this.countPerPage;
		}// if
		if (currentPage >= 1 && currentPage <= totalPage) {
			this.currentPage = currentPage;
			b = true;
		} else {
			b = false;
		}

		return b;
	}

	/**
	 * 得到总行数
	 */
	public int getTotalCount() {
		return this.totalCount;
	}

	/**
	 * 得到每页行数
	 */
	public int getCountPerPage() {
		return this.countPerPage;
	}

	/**
	 * 得到当前页数
	 */
	public int getCurrentPage() {
		return this.currentPage;
	}

	/**
	 * 得到总页数
	 */
	public int getTotalPage() {
		return this.totalPage;
	}

	/**
	 * 得到当前页行数
	 */
	public int getCountCurrentPage() {
		return this.countCurrentPage;
	}

	/**
	 * 可显第一页
	 */
	public boolean isFirstTrue() {
		boolean b = false;
		if (totalPage >= 2 && currentPage > 1) {
			b = true;
		}
		return b;
	}

	/**
	 * 可显最后页
	 */
	public boolean isLastTrue() {
		boolean b = false;
		if (totalPage >= 2 && currentPage < totalPage) {
			b = true;
		}
		return b;
	}

	/**
	 * 可显上一页
	 */
	public boolean isPreviousTrue() {
		boolean b = false;
		if (totalPage >= 2 && currentPage > 1) {
			b = true;
		}
		return b;
	}

	/**
	 * 可显下一页
	 */
	public boolean isNextTrue() {
		boolean b = false;
		if (totalPage >= 2 && currentPage < totalPage) {
			b = true;
		}
		return b;
	}

	/**
	 * 显示翻页信息
	 * 
	 * @param formName
	 *            显示页面的窗体名字
	 * @return 翻页信息的HTML片断
	 */
	public String printComInfo(String formName) {
		String out = "";
		int cuPage = getCurrentPage();
		int toPage = getTotalPage();
		out = out + "第<font color=green>" + cuPage
				+ "</font>页&nbsp	共<font color=green>" + toPage
				+ "</font>页&nbsp";
		out = out + "共<font color=green>" + getTotalCount()
				+ "</font>条记录&nbsp&nbsp&nbsp";
		if (isFirstTrue()) {
			out = out + "<a href='javascript:document." + formName
					+ ".page.value=1;document." + formName
					+ ".submit()'>首页</a>&nbsp";
		} else {
			out = out + "首页&nbsp";
		}
		if (isPreviousTrue()) {
			int previousPage = this.currentPage - 1;
			out = out + "<a href='javascript:document." + formName
					+ ".page.value=" + previousPage + ";document." + formName
					+ ".submit()'>前一页</a>&nbsp";
		} else {
			out = out + "前一页&nbsp";
		}
		if (isNextTrue()) {
			int nextPage = currentPage + 1;
			out = out + "<a href='javascript:document." + formName
					+ ".page.value=" + nextPage + ";document." + formName
					+ ".submit()'>后一页</a>&nbsp";
		} else {
			out = out + "后一页&nbsp";
		}
		if (isLastTrue()) {
			out = out + "<a href='javascript:document." + formName
					+ ".page.value=" + totalPage + ";document." + formName
					+ ".submit()'>末页</a>&nbsp";
		} else {
			out = out + "末页&nbsp&nbsp";
		}
		out = out + currentPage + "/" + totalPage + "页&nbsp";
		// com.asiabi.common.app.Debug.print(out);
		out = out
				+ "显示第<input type='input' name='goToPage' size='2' maxlenth='3'>页&nbsp<a href='javascript:chkSubmit("
				+ totalPage + ");'>GO</a>";
		return out;
	}
}