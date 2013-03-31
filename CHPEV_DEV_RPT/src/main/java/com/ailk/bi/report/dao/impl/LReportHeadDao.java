package com.ailk.bi.report.dao.impl;

import com.ailk.bi.base.exception.ReportHeadException;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportHeadDao;

public class LReportHeadDao implements ILReportHeadDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asiabi.report.dao.ILReportDao#getReportHead(java.lang.String)
	 */
	public String getReportHead(String rpt_id) {
		String arrHead = "";
		try {
			String strSql = SQLGenator.genSQL("Q3160", rpt_id);
			// System.out.println("Sql Q3160==>" + sql);
			String[][] result = WebDBUtil.execQryArray(strSql, "");
			if (result != null && result.length > 0) {
				arrHead = result[0][1];
			} else {
				arrHead = "<TH>缺少表头定义代码</TH>\n";
			}
		} catch (AppException ex) {
			arrHead = "<TH>获取表头代码定义失败</TH>\n";
		}
		return arrHead;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportDao#getReportHeadDefine(java.lang.String)
	 */
	public String getReportHeadDefine(String rpt_id) throws ReportHeadException {
		String head = null;
		if (null != rpt_id) {
			head = "";// 即使没有也设置空串
			try {
				String strSql = SQLGenator.genSQL("Q3160", rpt_id);
				String[][] ret = WebDBUtil.execQryArray(strSql, "");
				if (null != ret && ret.length > 0) {
					if (ret[0].length >= 2) {
						head = ret[0][1];
						if (null != head && !"".equals(head)) {
							// 加上<table></table>
							String tmpStr = "<table cellspacing=\"1\" cellpadding=\"1\" width=\"100%\" summary=\"\" border=\"1\">";
							// 需要将回车替换掉
							head = head.replaceAll("\\r\\n", "");
							head = tmpStr + head + "</table>";
						}
					}
				}
			} catch (AppException ae) {
				throw new ReportHeadException("获取报表表头失败", ae);
			}
		}
		return head;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportHeadDao#insertRptHead(java.lang.String,
	 * java.lang.String)
	 */
	public void insertRptHead(String rpt_id, String rptHead)
			throws ReportHeadException {
		if (null != rpt_id && !"".equals(rpt_id)) {
			try {
				String update = null;
				String strSql = SQLGenator.genSQL("Q3160", rpt_id);
				// 判断是否已经有表头了
				String[][] ret = WebDBUtil.execQryArray(strSql, "");
				String head = rptHead;
				head = head.replaceAll("'", "");
				head = head.replaceAll("\"", "\\'");
				if (null != ret && ret.length > 0) {
					// 更新记录
					update = SQLGenator.genSQL("U3471", rptHead, rpt_id);
					// update = "UPDATE UI_RPT_INFO_HEAD SET RPT_HEADER=? WHERE
					// RPT_ID=?";
				} else {
					// 插入记录
					update = SQLGenator.genSQL("I3470", rpt_id, rptHead);
					// update = "INSERT INTO UI_RPT_INFO_HEAD(RPT_HEADER,RPT_ID)
					// VALUES(?,?)";
				}
				WebDBUtil.execUpdate(update);
			} catch (AppException ae) {
				System.out.println(ae);
				throw new ReportHeadException("保存报表表头失败", ae);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asiabi.report.dao.ILReportDao#deleteRptHead(java.lang.String)
	 */
	public void deleteRptHead(String rpt_id) throws ReportHeadException {
		if (null == rpt_id || "".equals(rpt_id))
			throw new IllegalArgumentException("表报标识为空");
		try {
			String sql = SQLGenator.genSQL("D3472", rpt_id);
			WebDBUtil.execUpdate(sql);
		} catch (AppException ae) {
			throw new ReportHeadException("删除表报表头出错", ae);
		}
	}

	// 总账报表描述
	public String[][] getGLRptComment(String rpt_id) {
		String[][] rs = null;

		try {
			String sql = SQLGenator.genSQL("gl001", rpt_id);
			rs = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {

			e.printStackTrace();
		}
		return rs;
	}

	// 总账数据生成时间
	public String getGLRptCreateDate(String rpt_id, String rpt_date) {
		String date = null;

		try {
			String sql = SQLGenator.genSQL("gl002", rpt_id, rpt_date);
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0)
				date = rs[0][0];
		} catch (AppException e) {

			e.printStackTrace();
		}
		return date;
	}
}
