package com.ailk.bi.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于Excel工具的表头行对象 对于最后一行，即使有跨行单元格，也应在最后一列加上此单元格。
 */
public class TableHeadTr implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5825036000584010322L;

	/**
	 * 行对象列表
	 */
	private List<TableHeadTd> trSet = null;

	/**
	 * 是否是表头最后一行
	 */
	private boolean lastRow = false;

	/**
	 * 获取表头行对象
	 */
	public List<TableHeadTd> getTrSet() {
		return trSet;
	}

	/**
	 * 设置表头行对象
	 * 
	 * @param trSet
	 *            表头行对象列表
	 */
	public void setTrSet(List<TableHeadTd> trSet) {
		this.trSet = trSet;
	}

	/**
	 * 向表头行中添加一个单元格
	 * 
	 * @param thtd
	 */
	public void addTabeHeadTd(TableHeadTd thtd) {
		if (null == trSet)
			trSet = new ArrayList<TableHeadTd>();
		trSet.add(thtd);
	}

	/**
	 * 判断是否最后一行
	 * 
	 * @return
	 */
	public boolean isLastRow() {
		return lastRow;
	}

	/**
	 * 设置是否表头最后一行
	 * 
	 * @param lastRow
	 */
	public void setLastRow(boolean lastRow) {
		this.lastRow = lastRow;
	}
}
