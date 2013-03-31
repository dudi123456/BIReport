package com.ailk.bi.bulletin.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.bulletin.dao.IBulletinDao;
import com.ailk.bi.bulletin.entity.MartInfoBulletin;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "rawtypes" })
public class BulletinBoardHTMLAction extends HTMLActionSupport {
	private static Log logger = LogFactory
			.getLog(BulletinBoardHTMLAction.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}

		HttpSession session = request.getSession();
		// DAOFactory.getCommonFac().getFirstUserHeadMenu(session);

		String opt_type = StringB.NulltoBlank(request.getParameter("opt_type"));
		opt_type.replaceAll("\"", "'");
		String strReturn = "";
		if ("main".equals(opt_type)) {// 导航到号段sysadmin\noSectionAdmin\main.jsp主页面
			strReturn = "bulletinBoardMain";
		} else if ("navadd".equals(opt_type)) {// 导航到添加页面numAdd.jsp
			strReturn = "bulletinBoardAdd";
		} else if ("doadd".equals(opt_type)) {// 处理添加页面
			saveMartBulletinBoard(request, response);
			String url = "bulletinAdmin.rptdo?opt_type=bAdmin";
			session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE,
					"<font size=2><b>公告保存成功！</b></font>", url);
		} else if ("bAdmin".equals(opt_type)) {// 导航到管理页面

			session.setAttribute("BULLETIN_BOARD_LIST",
					qryMartBulletinInfoList(request, response));
			strReturn = "bulletinList";
		} else if ("delBulletin".equals(opt_type)) {// 删除记录
			deleteMartBulletinInfo(request);
			String url = "bulletinAdmin.rptdo?opt_type=bAdmin";
			session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE,
					"<font size=2><b>删除信息成功！</b></font>", url);

		} else if ("invalid".equals(opt_type)) {// 过期公告

			session.setAttribute("BULLETIN_BOARD_LIST",
					qryInvalidBulletin(request, response));
			strReturn = "bulletinInvalidList";
		} else if ("delBulletinSel".equals(opt_type)) {// 删除选中项
			deleteMartBulletinSelectItem(request);
			String url = "bulletinAdmin.rptdo?opt_type=invalidc";
			session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE,
					"<font size=2><b>删除信息成功！</b></font>", url);

		} else if ("bshow".equals(opt_type)) {// 公告显示

			session.setAttribute("BULLETIN_BOARD_LIST",
					qryValidMartBulletinInfoList(request, response));
			strReturn = "bulletinListShow";
		} else if ("openshow".equals(opt_type)) {// 打开公告

			session.setAttribute("BULLETIN_DETAIL",
					qryBulletinById(request));
			strReturn = "openbulletinShow";
		}

		strReturn += ".screen";

		setNextScreen(request, strReturn);
	}

	private MartInfoBulletin qryBulletinById(HttpServletRequest request) {
		IBulletinDao sysDao = DAOFactory.getBulletinDao();
		String id = request.getParameter("id");
		MartInfoBulletin obj = new MartInfoBulletin();
		String sql = "select title,news_msg from ui_info_bulletin where id="
				+ id;
		String[][] objInfo = sysDao.qryObjectInfoList(sql);
		if (objInfo != null && objInfo.length > 0) {
			obj.setTitle(objInfo[0][0]);
			obj.setNewsMsg(objInfo[0][1]);
		}
		return obj;

	}

	private void deleteMartBulletinInfo(HttpServletRequest request) {
		IBulletinDao sysDao = DAOFactory.getBulletinDao();
		String id = request.getParameter("id");
		sysDao.deleteMartBulletinInfo(id);
	}

	private void deleteMartBulletinSelectItem(HttpServletRequest request) {

		String[] id = request.getParameterValues("chxSong");
		String[] sql = new String[2];
		if (id != null && id.length > 0) {
			sql[1] = "delete from UI_INFO_BULLETIN where id in(";
			sql[2] = "delete from ui_rule_bulletin_usergrp where bulletin_id in(";

			for (int i = 0; i < id.length - 1; i++) {
				sql[1] += id[i] + ",";
				sql[2] += id[i] + ",";

			}
			sql[1] += id[id.length - 1] + ")";
			sql[2] += id[id.length - 1] + ")";
			try {
				WebDBUtil.execTransUpdate(sql);
			} catch (AppException e) {

				e.printStackTrace();
			}
			// sysDao.ex(sql);
		}

	}

	private String[][] qryInvalidBulletin(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		IBulletinDao sysDao = DAOFactory.getBulletinDao();

		Calendar ca = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strNow = formatter.format(ca.getTime());

		String sql = "select title,news_msg,to_char(valid_end_date,'yyyy-mm-dd'),id from  UI_INFO_BULLETIN a WHERE"

				+ " valid_end_date<=to_date('" + strNow + "','yyyy-mm-dd')";

		return sysDao.qryObjectInfoList(sql);
	}

	private List qryMartBulletinInfoList(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		IBulletinDao sysDao = DAOFactory.getBulletinDao();
		String city_id = StringB.NulltoBlank(request.getParameter("qry_city"));
		String startDate = StringB.NulltoBlank(
				request.getParameter("qry_starttime")).trim();
		if (startDate.length() > 0) {
			String[] startDateSplit = startDate.split(" ");
			startDate = startDateSplit[0];

		}

		String title = StringB.NulltoBlank(request.getParameter("qry_title"));

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.city_id = city_id;
		qryStruct.dim1 = startDate;
		qryStruct.dim2 = title;
		String typeId = StringB
				.NulltoBlank(request.getParameter("qry_type_id"));
		qryStruct.dim3 = typeId;

		String systemId = StringB.NulltoBlank((String) request.getSession()
				.getAttribute("system_id"));
		if (systemId.length() == 0) {
			systemId = "1";
		}

		qryStruct.dim5 = systemId;

		request.getSession().setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT,
				qryStruct);
		return sysDao.qryMartBulletinInfoList(qryStruct);

	}

	private String[][] qryValidMartBulletinInfoList(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		IBulletinDao sysDao = DAOFactory.getBulletinDao();

		ReportQryStruct qryStruct = new ReportQryStruct();
		qryStruct.city_id = "";
		qryStruct.dim1 = "";
		qryStruct.dim2 = "";

		InfoOperTable loginUser = CommonFacade.getLoginUser(request
				.getSession());

		qryStruct.dim3 = loginUser.user_id;
		qryStruct.dim4 = loginUser.group_id;
		String systemId = StringB.NulltoBlank((String) request.getSession()
				.getAttribute("system_id"));
		if (systemId.length() == 0) {
			systemId = "1";
		}

		qryStruct.dim5 = systemId;

		request.getSession().setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT,
				qryStruct);
		return sysDao.qryMartBulletinInfoList(qryStruct, true);

	}

	private String saveMartBulletinBoard(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		MartInfoBulletin obj = new MartInfoBulletin();
		IBulletinDao sysDao = DAOFactory.getBulletinDao();
		logger.info("do add");
		obj.setId(StringB.NulltoBlank(request.getParameter("id")));
		obj.setCityId(StringB.NulltoBlank(request.getParameter("city")));
		String strRight = StringB.NulltoBlank(request
				.getParameter("hidTxtBulRight"));

		String stDate = StringB.NulltoBlank(request.getParameter("starttime"))
				.trim();
		if (stDate.length() > 0) {
			String[] startDate = stDate.split(" ");
			stDate = startDate[0];
			obj.setValidBeginDate(stDate);

		}

		stDate = StringB.NulltoBlank(request.getParameter("endtime")).trim();
		if (stDate.length() > 0) {

			String[] startDate = stDate.split(" ");
			stDate = startDate[0];
			obj.setValidEndDate(stDate);
		}

		obj.setTitle(StringB.NulltoBlank(request.getParameter("title")));
		obj.setNewsMsg(StringB.NulltoBlank(request.getParameter("content")));

		Calendar ca = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strNow = formatter.format(ca.getTime());
		obj.setAddDt(strNow);

		InfoOperTable loginUser = (InfoOperTable) request.getSession()
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		obj.setCreator(loginUser.oper_no);
		logger.info(obj.toString());

		String typeId = StringB.NulltoBlank(request.getParameter("type_id"));
		obj.setTypeId(typeId);
		obj.setGroupId(strRight);
		obj.setFieldBak01("0");
		if (strRight.length() > 0) {
			obj.setFieldBak01("1");
			obj.setGroupId(strRight);
		}

		String system_id = StringB.NulltoBlank(request
				.getParameter("system_id"));
		obj.setSystemId(system_id);

		sysDao.insertMartBulletinInfo(obj);
		return "";
	}
}
