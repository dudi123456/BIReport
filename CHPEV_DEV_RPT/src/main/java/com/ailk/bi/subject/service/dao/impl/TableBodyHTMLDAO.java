package com.ailk.bi.subject.service.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableRowStruct;
import com.ailk.bi.subject.service.dao.ITableBodyHTMLDAO;
import com.ailk.bi.subject.util.SubjectConst;
import com.ailk.bi.subject.util.SubjectStringUtil;

/**
 * @author xdou 表格表体实现类
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableBodyHTMLDAO implements ITableBodyHTMLDAO {
	public volatile List export = new ArrayList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableBodyHTMLDAO#getTableBodyHTML(com
	 * .asiabi.base.table.SubjectCommTabDef,
	 * com.asiabi.subject.domain.TableCurFunc, java.util.List)
	 */
	public List getTableBodyHTML(SubjectCommTabDef subTable,
			TableCurFunc curFunc, List rows) throws SubjectException {
		if (null == subTable)
			throw new SubjectException("生成表格的表体时表格定义对象为空");
		if (null == curFunc)
			throw new SubjectException("生成表格的表体时功能对象为空");
		if (null == rows || 0 >= rows.size())
			throw new SubjectException("生成表格的表体时结果为空");
		// 最后返回的表格行列表
		List body = new ArrayList();
		export.clear();
		if (SubjectConst.YES.equalsIgnoreCase(subTable.row_head_swap)) {
			// 纵转横
			TableRowStruct row = null;
			String left = null;
			String right = null;
			Iterator iter = rows.iterator();
			StringBuffer sb = new StringBuffer();
			while (iter.hasNext()) {
				row = (TableRowStruct) iter.next();
				sb.delete(0, sb.length());
				left = row.leftHTML.toString();
				right = row.rightHTML.toString();
				left = left.replace("</tr>", "");
				right = SubjectStringUtil.removeHtmlTag(right, "tr");
				sb.append(left).append(right).append("</tr>");
				body.add(sb.toString());
				export.add(row.exportHTML.toString());
			}
		} else {
			// 由于固定表头原因声明左右两部分
			List left = new ArrayList();
			List right = new ArrayList();
			Iterator iter = rows.iterator();
			while (iter.hasNext()) {
				TableRowStruct row = (TableRowStruct) iter.next();
				if (SubjectConst.NO.equalsIgnoreCase(subTable.sum_display)
						&& row.isSumRow) {

				} else {

					left.add(row.leftHTML.toString());
					right.add(row.rightHTML.toString());
					export.add(row.exportHTML.toString());
				}
			}

			body.add(SubjectStringUtil.genFixedBodyLeftPart1());
			body.addAll(left);
			// 在这要加上一行多余的行,为了对齐高度
			body.add("<tr>");
			List tabCols = subTable.tableCols;
			iter = tabCols.iterator();
			while (iter.hasNext()) {
				SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
						&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure))
					body.add("<td class=\"table-td-withbg\">&nbsp;</td>");
			}
			body.add("</tr>");
			body.add(SubjectStringUtil.genFixedBodyLeftPart2());
			body.add(SubjectStringUtil.genFixedBodyRightPart1());
			body.addAll(right);
			body.add(SubjectStringUtil.genFixedBodyRightPart2());
		}
		return body;
	}

	public List getTableExportBody() throws SubjectException {
		return this.export;
	}
}
