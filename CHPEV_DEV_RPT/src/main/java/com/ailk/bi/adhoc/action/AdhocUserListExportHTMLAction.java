package com.ailk.bi.adhoc.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;
import com.ailk.bi.adhoc.util.AdhocBuildDownLoadFile;
import com.ailk.bi.adhoc.util.AdhocBuildXlsBean;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.adhoc.util.AdhocExportXlsTask;
import com.ailk.bi.adhoc.util.AdhocSaveTaskInJob;
import com.ailk.bi.adhoc.util.FileOperator;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.filemgr.common.Constants;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "static-access" })
public class AdhocUserListExportHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8661036395187437763L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();
		// 操作类型
		String operType = CommTool.getParameterGB(request, "oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "view";
		}

		String oper_no = CommonFacade.getLoginId(session);

		// 记录条数
		if ("exportcsv".equals(operType)) {// 导出Excel

			this.setNextScreen(request, "AdhocUserExportCVS.screen");

		} else if ("doSaveTask".equals(operType)) {
			// 保存导出任务到ui_adhoc_buildxls_task表中

			String flag = doSaveTask(request, session);
			request.setAttribute("flag", flag);

			setNextScreen(request, "AdhocUserExportGuideNext.screen");

		} else if ("deleteXls".equals(operType)) {
			// 删除ui_adhoc_buildxls_task表中数据

			deleteXls(request, session);

		} else if ("qryTaskSts".equals(operType)) {
			// 查询任务运行情况
			request.setAttribute("init", "true");
			session.setAttribute("VIEW_TREE_LIST",
					qryTaskRunStatus(request, oper_no));
			setNextScreen(request, "AdhocUserXlsTaskStatus.screen");

		} else if ("subjectdtl".equals(operType)) {
			// 分析型表格清单
			String table_id = request.getParameter("table_id");
			if (table_id.equalsIgnoreCase("opp_warn_002_3")) {
				// 客服预警

				request.setAttribute("init", "true");
				session.setAttribute("VIEW_TREE_LIST",
						qryUserPhoneNumber(request));
				setNextScreen(request, "SubjectUserNumDtl.screen");

			}

		} else if ("qrySubjectTaskSts".equals(operType)) {

			// 查询专题分析导出清单运行情况
			request.setAttribute("init", "true");

			session.setAttribute("VIEW_TREE_LIST", qryTaskRunStatus(request));

			setNextScreen(request, "SubjectUserXlsTaskStatus.screen");

		} else if ("qryJobQueue".equals(operType)) {
			// 查看任务队列情况
			request.setAttribute("init", "true");
			request.setAttribute("VIEW_TREE_LIST", qryJobQueue(request));
			setNextScreen(request, "AdhocJobQueueStatus.screen");

		} else if ("doDownload".equals(operType)) {
			// 下载文件
			doDownLoad(request, oper_no, response);

		} else if ("doexportcsv".equals(operType)) {
			String row_count = CommTool.getParameterGB(request, "row");
			if (row_count == null) {
				row_count = "-1";
			}
			session.setAttribute(AdhocConstant.ADHOC_EXPORT_ROWCNT, row_count);
			int tmpRowCnt = Integer.parseInt(row_count);
			if (tmpRowCnt != 10000) {
				// 使用任务导出

				setNextScreen(request, "AdhocUserExportGuide.screen");

			} else {
				AdhocBuildDownLoadFile adhocBuild = new AdhocBuildDownLoadFile();
				adhocBuild.setRequest(request);
				adhocBuild.setMrowCnt(Integer.parseInt(row_count));
				adhocBuild.setMcntSql((String) session
						.getAttribute(AdhocConstant.ADHOC_RECORD_CNT));
				adhocBuild.setPerPageCnt(50000);// 每次读取多少条记录
				adhocBuild.setPerFileRowLimit(50000);// 文件行数限定

				// adhocBuild.setPerPageCnt(2000);//每次读取多少条记录
				// adhocBuild.setPerFileRowLimit(300);//文件行数限定

				adhocBuild.setMfileName("用户清单_" + oper_no);// 文件名
				adhocBuild.setmZipName("用户清单_" + oper_no + ".zip");

				adhocBuild.setMqrySql((String) session
						.getAttribute(AdhocConstant.ADHOC_QRY_SQL));
				adhocBuild
						.setMdefineInfo((UiAdhocUserListTable[]) session
								.getAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO));
				String saveDirectory = request.getSession().getServletContext()
						.getRealPath("/")
						+ File.separatorChar + "adhocDownLoad";
				adhocBuild.setMrootPath(saveDirectory);

				// 写日志
				writeQryLog(request, "I_ADHOC_LOG_01", adhocBuild.getMqrySql());

				String downFileName = "";
				try {
					// downFileName = adhocBuild.buildCsvFile();
					downFileName = adhocBuild.buildXLSFile(request);

					logcommon.debug("downFileName:" + downFileName
							+ ":zipname:" + adhocBuild.getmZipName());
					// HttpServletResponse response =
					// ServletActionContext.getResponse();
					response.reset();
					response.setContentType("application/x-download; charset=utf-8"); // linux

					// response.addHeader("Content-Disposition",
					// "attachment; filename=\"" + new
					// String(myName.getBytes("gb2312"),"iso8859-1") + "\"");
					// //linux

					response.addHeader(
							"Content-Disposition",
							"attachment; filename=\""
									+ URLEncoder.encode(
											adhocBuild.getmZipName(), "utf-8")
									+ "\""); // linux
					OutputStream output = null;
					FileInputStream fis = null;
					try {
						// 新建File对象
						File f = new File(downFileName);

						// 新建文件输入输出流对象
						output = response.getOutputStream();
						fis = new FileInputStream(f);
						// 设置每次写入缓存大小
						byte[] b = new byte[(int) f.length()];
						// out.print(f.length());
						// 把输出流写入客户端
						int i = 0;

						while ((i = fis.read(b)) > 0) {
							output.write(b, 0, i);
						}

						output.flush();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (fis != null) {
							fis.close();
							fis = null;
						}
						if (output != null) {
							output.close();
							output = null;
						}
					}

				} catch (AppException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
				logcommon.debug("OK");
			}

		}

	}

	private void doDownLoad(HttpServletRequest request, String oper_no,
			HttpServletResponse response) {
		String id = request.getParameter("id");
		String author = request.getParameter("auth");
		String grp_id = DAOFactory.getCommonFac().getLoginUser(
				request.getSession()).group_id;
		boolean isRight = false;
		String fileName = request.getParameter("fileName");

		if (!grp_id.equals("25")) {
			// 非系统管理员只能查自己的任务
			if (oper_no.equals(author)) {
				// 本人才能下载自己的
				isRight = true;
			}
		} else {
			isRight = true;
		}

		System.out.println(id + ":" + author + ":" + grp_id + ":" + fileName);
		if (isRight) {
			try {
				response.reset();
				response.setContentType("application/x-download; charset=utf-8"); // linux

				// response.addHeader("Content-Disposition",
				// "attachment; filename=\"" + new
				// String(myName.getBytes("gb2312"),"iso8859-1") + "\"");
				// //linux

				response.addHeader(
						"Content-Disposition",
						"attachment; filename=\""
								+ URLEncoder.encode(fileName, "utf-8") + "\""); // linux
				OutputStream output = null;
				InputStream fis = null;
				try {
					// 新建File对象

					// 新建文件输入输出流对象
					output = response.getOutputStream();
					fis = WebDBUtil.readBlogContent("id", "FILECONTNT",
							"ui_adhoc_buildxls_task", id);
					System.out.println("fis:" + fis);
					// 设置每次写入缓存大小
					if (fis != null) {
						byte[] b = new byte[1024];
						// out.print(f.length());
						// 把输出流写入客户端

						int i = 0;
						while ((i = fis.read(b)) > 0) {
							output.write(b, 0, i);
						}
						output.flush();
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fis != null) {
						fis.close();
						fis = null;
					}
					if (output != null) {
						output.close();
						output = null;
					}
				}

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @param request
	 * @param oper_no
	 * @return
	 * @desc:查询任务队列情况
	 */
	private String[][] qryJobQueue(HttpServletRequest request) {
		String id = StringB.NulltoBlank(request.getParameter("id"));
		String machine = StringB.NulltoBlank(request.getParameter("machine"));

		String sql = "select id,xls_name,oper_no,RECORDCNT,case when RECORDCNT>0 then '等待运行' else '等待运行[正在查询数据集]' end  from ui_adhoc_buildxls_task where id<="
				+ id
				+ " and flag = 0 and RUNMACHINE="
				+ machine
				+ " order by id ";

		try {

			return WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;

	}

	private String[][] qryTaskRunStatus(HttpServletRequest request,
			String oper_no) {

		String startDate = StringB.NulltoBlank(request
				.getParameter("startDate"));
		ReportQryStruct struct = new ReportQryStruct();

		String endDate = StringB.NulltoBlank(request.getParameter("endDate"));

		String in_oper_no = StringB.NulltoBlank(request
				.getParameter("in_oper_no"));
		String grp_id = DAOFactory.getCommonFac().getLoginUser(
				request.getSession()).group_id;
		if (!grp_id.equals("1")) {
			// 非系统管理员只能查自己的任务
			in_oper_no = oper_no;
		} else {

		}
		String flag = StringB.NulltoBlank(request.getParameter("flag"));

		struct.dim1 = startDate;
		struct.dim2 = endDate;
		struct.dim3 = in_oper_no;
		struct.dim4 = flag;

		request.getSession().setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT,
				struct);
		String sql = "SELECT id,xls_name,xls_desc,to_char(add_date,'YYYY-MM-DD hh24:mi:ss'),flag,file_name,oper_no,bak_fld_01,bak_fld_02,RUNMACHINE,to_char(RUN_DATE,'YYYY-MM-DD hh24:mi:ss'),to_char(FINISH_DATE,'YYYY-MM-DD hh24:mi:ss'),RECORDCNT,bak_fld_03 from ui_adhoc_buildxls_task where 1=1 ";

		if (in_oper_no.length() > 0) {
			sql += " and oper_no='" + in_oper_no + "'";
		}

		if (startDate.length() > 0) {
			sql += " and add_date>=to_date('" + startDate + " 00:00:00"
					+ "','yyyy-mm-dd hh24:mi:ss')";
		}

		if (endDate.length() > 0) {
			sql += " and add_date<=to_date('" + endDate + " 23:59:59"
					+ "','yyyy-mm-dd hh24:mi:ss')";
		}

		if (startDate.length() == 0 && endDate.length() == 0) {
			sql += " and add_date>=to_date('"
					+ DateUtil.getDiffDay(0, DateUtil.getNowDate())
					+ "','yyyy-mm-dd')";
			struct.dim1 = DateUtil.getDiffDay(0, DateUtil.getNowDate());
			struct.dim2 = DateUtil.getDiffDay(0, DateUtil.getNowDate());
		}

		if (flag.length() > 0) {
			sql += " and flag=" + flag;
		}

		sql += " order by id";
		// System.out.println(sql);
		try {

			return WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;

	}

	private String[][] qryTaskRunStatus(HttpServletRequest request) {

		String startDate = StringB.NulltoBlank(request
				.getParameter("startDate"));
		ReportQryStruct struct = new ReportQryStruct();

		String endDate = StringB.NulltoBlank(request.getParameter("endDate"));

		String in_oper_no = StringB.NulltoBlank(request
				.getParameter("in_oper_no"));
		String grp_id = DAOFactory.getCommonFac().getLoginUser(
				request.getSession()).group_id;

		// String typeId =
		// StringB.NulltoBlank(CommTool.getParameterGB(request,"typeId"));
		// String resId =
		// StringB.NulltoBlank(CommTool.getParameterGB(request,"resId"));

		String typeId = StringB.NulltoBlank(request.getParameter("typeId"));
		String resId = StringB.NulltoBlank(request.getParameter("resId"));

		String oper_no = CommonFacade.getLoginId(request.getSession());

		if (!grp_id.equals("1")) {
			// 非系统管理员只能查自己的任务
			in_oper_no = oper_no;
		} else {

		}

		struct.dim1 = startDate;
		struct.dim2 = endDate;
		struct.dim3 = in_oper_no;
		struct.dim4 = typeId;
		struct.dim5 = resId;

		request.getSession().setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT,
				struct);
		String sql = "SELECT id,xls_name,xls_desc,to_char(add_date,'YYYY-MM-DD hh24:mi:ss'),flag,file_name,oper_no,bak_fld_01,bak_fld_02,RUNMACHINE,to_char(RUN_DATE,'YYYY-MM-DD hh24:mi:ss'),to_char(FINISH_DATE,'YYYY-MM-DD hh24:mi:ss'),RECORDCNT,bak_fld_03 from ui_adhoc_buildxls_task where 1=1 ";

		if (in_oper_no.length() > 0) {
			sql += " and oper_no='" + oper_no + "'";
		}

		/*
		 * if (startDate.length()>0){ sql += " and add_date>=to_date('" +
		 * startDate + " 00:00:00" + "','yyyy-mm-dd hh24:mi:ss')"; }
		 * 
		 * 
		 * if (endDate.length()>0){ sql += " and add_date<=to_date('" + endDate
		 * + " 23:59:59" + "','yyyy-mm-dd hh24:mi:ss')"; }
		 */

		if (typeId.length() > 0) {
			sql += " and adhoc_id='" + typeId + "'";
		}

		if (resId.length() > 0) {
			sql += " and bak_fld_02='" + resId + "'";
		}

		/*
		 * if (startDate.length()==0 && endDate.length()==0){ sql +=
		 * " and add_date>=to_date('" + DateUtil.getDiffDay(0,
		 * DateUtil.getNowDate()) + "','yyyy-mm-dd')"; struct.dim1 =
		 * DateUtil.getDiffDay(0, DateUtil.getNowDate()); struct.dim2 =
		 * DateUtil.getDiffDay(0, DateUtil.getNowDate()); }
		 */
		sql += " order by add_date desc";
		// System.out.println(sql);
		try {

			return WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;

	}

	private void deleteXls(HttpServletRequest request, HttpSession session)
			throws HTMLActionException {

		String id = StringB.NulltoBlank(request.getParameter("id"));
		String sql = "select id,file_name,bak_fld_02 from ui_adhoc_buildxls_task t where t.id="
				+ id;
		try {
			String[][] res = WebDBUtil.execQryArray(sql, "");
			if (res != null && res.length > 0) {

				// String saveDirectory =
				// request.getSession().getServletContext().getRealPath("/") +
				// File.separatorChar + "adhocDownLoad";
				String saveDirectory = Constants.FILE_PATH_PREFIX
						+ File.separatorChar + "adhocFile";

				String fileName = saveDirectory + File.separatorChar
						+ res[0][1];
				if (FileOperator.fileExist(fileName)) {
					FileOperator.delFile(fileName);
				}
				String[] sqlDel = new String[2];
				sqlDel[0] = "delete from ui_adhoc_buildxls_task where id=" + id;
				sqlDel[1] = "delete from ui_adhoc_buildxls_task_extend where refid="
						+ id;
				WebDBUtil.execTransUpdate(sqlDel);

				String url = "AdhocInfoExport.rptdo?oper_type=qryTaskSts";
				throw new HTMLActionException(session,
						HTMLActionException.SUCCESS_PAGE, "信息删除成功！", url);

			}

		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	private String doSaveTask(HttpServletRequest request, HttpSession session) {

		String mainID = CommTool.dbGetMaxIDBySeqName("ADHOC_BUILDXLS_TASK_SEQ");
		String oper_no = CommonFacade.getLoginId(session);
		String name = StringB.NulltoBlank(request.getParameter("taskName"));
		String desc = StringB.NulltoBlank(request.getParameter("taskDesc"));

		String qryExist = "select id from ui_adhoc_buildxls_task where xls_name='"
				+ name + "' and oper_no='" + oper_no + "'";
		try {
			String[][] dataExist = WebDBUtil.execQryArray(qryExist, "");
			if (dataExist != null && dataExist.length > 0) {
				// 已经有改名称的任务
				return "1";

			}

		} catch (AppException e1) {

			e1.printStackTrace();
			return "2";
		}

		String qrySql = (String) session
				.getAttribute(AdhocConstant.ADHOC_QRY_SQL);
		qrySql = qrySql.replaceAll("'", "''");

		String adhocId = StringB.NulltoBlank(request.getParameter("adhoc_id"));
		String rcnt = StringB.NulltoBlank(request.getParameter("row_cnt"));
		String cntSqlIn = (String) session
				.getAttribute(AdhocConstant.ADHOC_RECORD_CNT);

		String cntSql = cntSqlIn.replaceAll("'", "''");
		UiAdhocUserListTable[] mdefineInfo = (UiAdhocUserListTable[]) session
				.getAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO);

		String sessId = session.getId();
		String ip = CommTool.getClientIP(request);

		String[] sqlM = new String[mdefineInfo.length + 1];

		sqlM[0] = "insert into ui_adhoc_buildxls_task(id,oper_no,xls_name,xls_desc,row_cnt,cnt_sql,qry_sql,flag,add_date,adhoc_id,session_id,ip_addr) "
				+ " values("
				+ mainID
				+ ",'"
				+ oper_no
				+ "','"
				+ name
				+ "','"
				+ desc
				+ "','"
				+ rcnt
				+ "','"
				+ cntSql
				+ "','"
				+ qrySql
				+ "',0,sysdate,'"
				+ adhocId
				+ "','"
				+ sessId
				+ "','"
				+ ip
				+ "')";

		logcommon.debug(sqlM[0]);

		for (int j = 0; j < mdefineInfo.length; j++) {
			sqlM[j + 1] = "insert into ui_adhoc_buildxls_task_extend values(ADHOC_BLDXLS_TASKEXT_SEQ.nextval,"
					+ mainID + "," + mdefineInfo[j].toString() + ")";
			logcommon.debug(sqlM[j + 1]);
		}

		try {
			WebDBUtil.execTransUpdate(sqlM);
		} catch (AppException e) {

			e.printStackTrace();
			return "2";
		}

		// sendInfoToSocket(mainID);

		AdhocBuildXlsBean bean = new AdhocBuildXlsBean();
		bean.setMainID(mainID);
		// bean.setAdhocId(adhocId);
		// bean.setOper_no(oper_no);
		// bean.setName(name);
		// bean.setDesc(desc);
		bean.setRcnt(rcnt);
		bean.setCntSql(cntSqlIn);
		// bean.setSessId(sessId);
		// bean.setIp(ip);
		// bean.setMdefineInfo(mdefineInfo);
		// bean.setQrySql(qrySql);

		AdhocSaveTaskInJob jobSave = new AdhocSaveTaskInJob(bean);
		AdhocExportXlsTask task = new AdhocExportXlsTask(jobSave);
		Timer timer = new Timer();
		timer.schedule(task, 100);

		// 写日志
		// qrySql = qrySql.replaceAll("'", "''");
		writeQryLog(request, "I_ADHOC_LOG_01", qrySql);

		return "0";
	}

	private void sendInfoToSocket(String id) {
		try {
			Socket socket = new Socket("172.19.31.47", 10984);
			System.out.println("send request to server");

			PrintWriter out = null;
			try {

				out = new PrintWriter(socket.getOutputStream());
				System.out.println("send task id:" + id);
				out.println(id);
				out.flush();

			} finally {
				System.out.println("Finally");
				socket.close();
				out.close();

			}

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void writeQryLog(HttpServletRequest request, String sql_no,
			String qrysql) {
		String qry = qrysql.replace("'", "''");
		String sql = getLogSql(request, sql_no, qry);
		// logcommon.debug("write log:" + sql);
		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 取得日志信息SQL
	 * 
	 * @param request
	 * @param oper_no
	 * @param type
	 * @return
	 */
	private String getLogSql(HttpServletRequest request, String sql_no,
			String qrysql) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_oper_no = CommonFacade.getLoginId(request.getSession());

		Calendar ca = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strNow = formatter.format(ca.getTime());

		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_oper_no, ip,
					strNow, qrysql);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

	private String[][] qryUserPhoneNumber(HttpServletRequest request) {
		String pmsid = StringB.NulltoBlank(request.getParameter("pmsid"));
		String op_time = request.getParameter("op_time");
		// String msuFld = request.getParameter("msu_fld");
		// System.out.println(op_time + ":" + msuFld);
		String table_id = request.getParameter("table_id");
		String msuName = StringB.NulltoBlank(request.getParameter("msu_name"));
		String pms = "";
		String cservice_times_lvl_id = StringB.NulltoBlank(request
				.getParameter("cservice_times_lvl_id"));
		String whereSql = " ";
		String dimFld = " ";
		if (pmsid.equalsIgnoreCase("1")) {

			if (cservice_times_lvl_id.length() == 0) {
				whereSql = " and cm_cservice_times_lvl_id>0";
			} else {
				whereSql = " and cm_cservice_times_lvl_id="
						+ cservice_times_lvl_id;
			}
			dimFld = ",CM_O_CSERVICE_TIMES";
			pms = "与移动客服";
		} else if (pmsid.equalsIgnoreCase("2")) {
			if (cservice_times_lvl_id.length() == 0) {
				whereSql = " and cu_cservice_times_lvl_id>0";
			} else {
				whereSql = " and cu_cservice_times_lvl_id="
						+ cservice_times_lvl_id;
			}
			dimFld = ",CU_O_CSERVICE_TIMES";
			pms = "与联通客服";
		} else if (pmsid.equalsIgnoreCase("3")) {
			if (cservice_times_lvl_id.length() == 0) {
				whereSql = " and cm_cu_cservice_times_lvl_id>0";
			} else {
				whereSql = " and cm_cu_cservice_times_lvl_id="
						+ cservice_times_lvl_id;
			}
			dimFld = ",CM_CU_O_CSERVICE_TIMES";
			pms = "交叉客服";
		}

		ReportQryStruct struct = new ReportQryStruct();
		struct.dim1 = op_time;
		struct.dim2 = cservice_times_lvl_id;
		struct.dim3 = table_id;
		struct.dim4 = msuName;
		struct.dim5 = pms;
		struct.dim6 = pmsid;

		request.getSession().setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT,
				struct);

		String sql = "SELECT ACC_NBR " + dimFld
				+ " FROM FUI_OPP_ALERT_BUSI_USE_M A WHERE op_time=" + op_time
				+ " AND  CARRIER_ID=10 " + whereSql + " and  rownum<=2000";

		try {
			System.out.println(sql);
			return WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;
	}

}
