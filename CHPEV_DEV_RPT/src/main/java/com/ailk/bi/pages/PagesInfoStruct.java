package com.ailk.bi.pages;

import com.ailk.bi.common.event.JBTableBase;

public class PagesInfoStruct extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2891347239226334906L;

	/**
	 * 当前页号
	 */
	public int iCurPage = -1;

	/**
	 * 每页行数
	 */
	public int iLinesPerPage = 20;

	/**
	 * 结果集的总行数
	 */
	public int iLines = 0;

	/**
	 * 总页数
	 */
	public int iPages = 0;

	/**
	 * 已经选中的值信息，可为多个用ID_DELI分隔
	 */
	public String checkIDs = "";

	/**
	 * 不显示的IDs
	 */
	public String[] blankIDs = null;

	/**
	 * 在前台html中的page信息的前缀，默认为page__
	 */
	public String pagePrefix = "page";

	/**
	 * 计算总共的页数
	 */
	public int calcPages() {
		iPages = (iLines - 1) / iLinesPerPage;
		return iPages;
	}

	/**
	 * 当前页的第一行的绝对行数
	 * 
	 * @return
	 */
	public int absRowNoCurPage() {
		return iCurPage * iLinesPerPage;
	}

	/**
	 * 判断一个ID是否在已选的IDs中
	 * 
	 * @param ID
	 * @return
	 */
	public boolean isInIDs(String ID) {
		if (checkIDs.indexOf(CommKeys.ID_DELI + ID + CommKeys.ID_DELI) > -1)
			return true;
		return false;
	}
}