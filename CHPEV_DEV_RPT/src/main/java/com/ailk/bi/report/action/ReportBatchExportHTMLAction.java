package com.ailk.bi.report.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.CodeParamUtil;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.BatchExportUtil;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * 报表批量导出 oper_type = init 查询初始化参数报表列表（日报列表）
 * 
 * @author Chunming
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportBatchExportHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 获取用户session
		HttpSession session = request.getSession();
		String filePath = session.getServletContext().getRealPath(
				session.getServletContext().getInitParameter("zip.param"));
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(filePath));
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		// 是否按照按报表分类(分组)定义导出落地多个excel文件
		String group_tag = props.getProperty("zip_group_tag");
		// 默认下载压缩文件名称
		String zip_file_name = props.getProperty("zip_file_name");
		// 默认压缩文件目录
		String zip_dir_path = props.getProperty("zip_file_dir");

		String operType = CommTool.getParameterGB(request, "oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "query";
		}
		//
		String user_no = CommonFacade.getLoginId(session);
		// logcommon.debug("basePath================"+basePath);

		if ("query".equalsIgnoreCase(operType)) {// 默认初始查询，提取关联报表链表
			//
			String report_date = CommTool
					.getParameterGB(request, "report_date");

			ReportQryStruct qryStruct = new ReportQryStruct();
			// 取得查询结构数据
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException ex) {
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "批量导出查询条件发生错误！");
			}
			// 报表统计周期（日报,月报）
			if (qryStruct.rpt_cycle == null || "".equals(qryStruct.rpt_cycle)) {
				qryStruct.rpt_cycle = "6";
			}
			if ("6".equals(qryStruct.rpt_cycle)) {// 日
				if (report_date == null || "".equals(report_date)) {
					if (qryStruct.rpt_date == null
							|| "".equals(qryStruct.rpt_date)) {
						qryStruct.rpt_date = DateUtil.getDiffDay(-1,
								DateUtil.getNowDate());
					}
				} else {
					qryStruct.rpt_date = report_date;
				}

			} else if ("4".equals(qryStruct.rpt_cycle)) {// 月
				if (report_date == null || "".equals(report_date)) {
					if (qryStruct.rpt_date == null
							|| "".equals(qryStruct.rpt_date)) {
						qryStruct.rpt_date = DateUtil.getDiffMonth(-1,
								DateUtil.getNowDate());
					}
				} else {
					qryStruct.rpt_date = report_date;
				}

			}
			//
			qryStruct.date_s = qryStruct.rpt_date;
			qryStruct.date_e = qryStruct.rpt_date;
			// 报表HTML
			qryStruct.first_view = ReportConsts.NO;
			qryStruct.visible_data = ReportConsts.YES;

			// 报表分类，（报表分组）
			String[] report_type = request.getParameterValues("rpt_type");
			for (int i = 0; report_type != null && i < report_type.length; i++) {
				if (qryStruct.rpt_type.length() > 0) {
					qryStruct.rpt_type += ",";
				}
				qryStruct.rpt_type += "'" + report_type[i] + "'";
			}

			// 查询报表列表
			RptResourceTable[] infoRpt = queryReportInfo(qryStruct);

			session.setAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT, qryStruct);

			session.setAttribute(WebKeys.ATTR_REPORT_RESOURCE_DEFINE, infoRpt);

			this.setNextScreen(request, "BatchExport.screen");

		} else if ("group_single_export".equalsIgnoreCase(operType)) {// 分组,分类导出
			// 得到分组标识
			// 得到查询日期
			// 得到报表列表
			// 批量导出
			// 返回

		} else if ("export".equalsIgnoreCase(operType)) {

			session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
			// 传递数据结构
			ReportQryStruct qryStruct = (ReportQryStruct) session
					.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
			if (qryStruct == null) {
				qryStruct = new ReportQryStruct();
			}
			//
			try {
				logcommon.debug(qryStruct.toXml());
			} catch (AppException e) {

				e.printStackTrace();
			}

			// 选中导出报表
			String[] report_id = request.getParameterValues("report_check");
			if (report_id == null || report_id.length <= 0) {
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "批量导出没有选择报表列表！");

			}

			String reportStr = "";
			for (int i = 0; i < report_id.length; i++) {
				if (reportStr.length() > 0) {
					reportStr += ",";
				}
				reportStr += "'" + report_id[i] + "'";
			}

			// 提取报表信息
			ILReportService rptService = new LReportServiceImpl();
			ArrayList list = new ArrayList();
			try {
				list = (ArrayList) rptService.getReports("AND RPT_ID IN("
						+ reportStr + ")");
			} catch (AppException e) {

				e.printStackTrace();
			}

			RptResourceTable[] infoRpt = (RptResourceTable[]) list
					.toArray(new RptResourceTable[list.size()]);
			// 清空导出方式
			for (int i = 0; i < infoRpt.length; i++) {
				infoRpt[i].rpt_export_rule = "";
			}

			// 设置导出方式
			HashMap ruleMap = CodeParamUtil.codeListParamFetcher(request,
					"RPT_EXPORT_RULE");
			String[] ruleArr = (String[]) ruleMap.keySet().toArray(
					new String[ruleMap.size()]);
			Arrays.sort(ruleArr);

			for (int index = 0; ruleArr != null && index < ruleArr.length; index++) {
				String checkName = "report_export_rule_" + ruleArr[index];
				String[] report = request.getParameterValues(checkName);
				//
				for (int a = 0; infoRpt != null && a < infoRpt.length; a++) {
					for (int b = 0; report != null && b < report.length; b++) {
						if (report[b].trim().equalsIgnoreCase(
								infoRpt[a].rpt_id.trim())) {
							if (infoRpt[a].rpt_export_rule.trim().length() > 0) {
								infoRpt[a].rpt_export_rule += ",";
							}// end if
							infoRpt[a].rpt_export_rule += ruleArr[index];
						}// end if
					}// end for
				}// end for
			}// end for

			int result = 0;
			try {

				result = BatchExportUtil.excute(request, infoRpt, qryStruct,
						user_no, group_tag, zip_dir_path, zip_file_name);

			} catch (AppException e) {

				result = -1;
				e.printStackTrace();
			}
			session.removeAttribute(WebKeys.ATTR_ANYFLAG);

			if (result >= 0) {
				//
				session.setAttribute(WebKeys.ATTR_ANYFLAG, "2");
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.SUCCESS_PAGE, "批量导出成功,请单击下载文件保存！",
						"ReportBatchExportAction.rptdo?oper_type=download");
			} else {
				session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.ERROR_PAGE, "批量导出失败,请检查导出规则！");
			}

		} else if ("download".equalsIgnoreCase(operType)) {// 下载
			session.removeAttribute(WebKeys.ATTR_ANYFLAG);
			// 下载文件名
			String filename = zip_dir_path + "//" + user_no + "//"
					+ zip_file_name;
			// 下载
			downLoadFile(filename, request, response);
			//
			this.setNvlNextScreen(request);

		}
	}

	/**
	 * 文件下载
	 * 
	 * @param filePath
	 *            文件
	 * @param response
	 * @param isOnLine
	 *            是否在线打开
	 * @throws Exception
	 */
	public void downLoadFile(String savePath, HttpServletRequest request,
			HttpServletResponse response) {

		String filePath = savePath;
		// request.getSession().getServletContext().getRealPath(savePath);
		// logcommon.debug("filePath=================="+filePath);

		try {
			File f = new File(filePath);
			if (!f.exists()) {
				response.sendError(404,
						"File not found,please check the path of the file!");
				return;
			}
			BufferedInputStream br = new BufferedInputStream(
					new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;
			// response.setContentType(getContentType(aFileName) + ";
			// charset=UTF-8");
			// response.setHeader("Content-disposition", "attachment; filename="
			// + aFileName);
			response.reset(); // 非常重要
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ f.getName());

			OutputStream out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询报表列表
	 * 
	 * @param qryStruct
	 * @return
	 */
	private static RptResourceTable[] queryReportInfo(ReportQryStruct qryStruct) {

		RptResourceTable[] infoRpt = null;
		String whereStr = "";
		if (qryStruct.rpt_name != null && !"".equals(qryStruct.rpt_name)) {
			whereStr += " AND A.NAME LIKE '%" + qryStruct.rpt_name + "%'";
		}

		if (qryStruct.rpt_type != null && !"".equals(qryStruct.rpt_type)) {
			whereStr += " AND B.GROUP_ID IN ( " + qryStruct.rpt_type + ")";
		}

		if (qryStruct.rpt_cycle != null && !"".equals(qryStruct.rpt_cycle)) {
			whereStr += " AND A.cycle = " + qryStruct.rpt_cycle;
		}

		String sql = "";
		try {
			sql = SQLGenator.genSQL("RPT001", whereStr);
			// logcommon.debug(sql);
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			if (arr != null) {
				infoRpt = new RptResourceTable[arr.length];
				for (int i = 0; i < arr.length; i++) {
					infoRpt[i] = new RptResourceTable();
					infoRpt[i].rpt_id = arr[i][0];
					infoRpt[i].name = arr[i][1];
					infoRpt[i].rpt_type = arr[i][2];
					infoRpt[i].rpt_export_rule = arr[i][3];
				}

			}

		} catch (AppException e) {

			e.printStackTrace();
		}
		return infoRpt;
	}
}
