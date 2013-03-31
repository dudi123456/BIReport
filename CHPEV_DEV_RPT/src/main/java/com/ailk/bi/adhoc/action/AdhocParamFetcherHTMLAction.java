/**
 * 
 */
package com.ailk.bi.adhoc.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.adhoc.struct.AdhocViewQryStruct;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.adhoc.util.AdhocUtil;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;

//import com.ailk.bi.common.dbtools.DAOFactory;

/**
 * 界面参数提取Action
 * 
 * 界面参数包括： （1）条件 （2）纬度 （3）指标
 * 
 * @author Chunming
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocParamFetcherHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 得到会话
		HttpSession session = request.getSession();
		String oper_type = CommTool.getParameterGB(request, "oper_type");
		if (oper_type == null) {
			oper_type = "";
		}

		session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);

		if ("".equals(oper_type)) {
			//
			String root = CommTool.getParameterGB(request, "adhoc_root");
			if (root == null || "".equals(root)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "即席查询功能根节点缺失，请通知系统管理员！");

			}
			// 表格ID
			String group_tag = CommTool.getParameterGB(request,
					AdhocConstant.ADHOC_TABLE_WEBKEYS);// "group_tag"
			if (group_tag == null || "".equals(group_tag)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"即席查询界面属性表格标记缺失，请通知系统管理员！");

			}
			// 属性表格ID
			String group_msu_tag = CommTool.getParameterGB(request,
					AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);// "group_msu_tag"
			if (group_msu_tag == null || "".equals(group_msu_tag)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"即席查询界面指标表格标记缺失，请通知系统管理员！");

			}
			ArrayList conList = new ArrayList();
			ArrayList dimList = new ArrayList();
			ArrayList msuList = new ArrayList();
			// 取得条件的选择列表
			String[] conArr = request
					.getParameterValues(AdhocConstant.ADHOC_CONDITION_NAME);
			for (int i = 0; conArr != null && i < conArr.length; i++) {
				conList.add(conArr[i].substring(3));
			}

			// 取得纬度的选择列表
			String[] dimArr = request
					.getParameterValues(AdhocConstant.ADHOC_DIM_NAME);
			for (int i = 0; dimArr != null && i < dimArr.length; i++) {
				dimList.add(dimArr[i].substring(2));
			}
			// 取得度量的选择列表
			String[] msuArr = request
					.getParameterValues(AdhocConstant.ADHOC_MSU_NAME);
			for (int i = 0; msuArr != null && i < msuArr.length; i++) {
				msuList.add(msuArr[i].substring(3));
			}

			conArr = (String[]) conList.toArray(new String[conList.size()]);
			dimArr = (String[]) dimList.toArray(new String[dimList.size()]);
			msuArr = (String[]) msuList.toArray(new String[msuList.size()]);

			//
			session.removeAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS, root);

			session.removeAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS, group_tag);

			session.removeAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS,
					group_msu_tag);

			session.removeAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS, conArr);

			session.removeAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS, dimArr);

			session.removeAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);
			session.setAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS, msuArr);
		} else {// 删除对应定制结果
				//
			String type = CommTool.getParameterGB(request, "type");
			if (type == null || "".equals(type)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "缺失删除定制结果类型，请通知系统管理员！");
			}
			//
			String id = CommTool.getParameterGB(request, "id");
			if (id == null || "".equals(id)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "缺失删除定制结果标识，请通知系统管理员！");
			}
			//
			if ("con".equals(type)) {
				String[] conArr = (String[]) session
						.getAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
				String[] tmpArr = AdhocUtil.removeArr(conArr, id);
				session.removeAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS,
						tmpArr);

			} else if ("dim".equals(type)) {
				String[] dimArr = (String[]) session
						.getAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);
				String[] tmpArr = AdhocUtil.removeArr(dimArr, id);
				session.removeAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_DIM_WEBKEYS, tmpArr);

			} else if ("msu".equals(type)) {

				String[] msuArr = (String[]) session
						.getAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);
				String[] tmpArr = AdhocUtil.removeArr(msuArr, id);

				session.removeAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS);
				session.setAttribute(AdhocConstant.ADHOC_MSU_WEBKEYS, tmpArr);
			}

		}

		// DAOFactory.getCommonFac().getFirstUserHeadMenu(session);

		// 即席查询视图结构
		AdhocViewQryStruct qryStruct = new AdhocViewQryStruct();
		// 取得查询结构数据
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException ex) {
			throw new HTMLActionException(request.getSession(),
					HTMLActionException.WARN_PAGE, "查询条件受理信息有误！");
		}
		//
		if (qryStruct.gather_mon == null || "".equals(qryStruct.gather_mon)) {
			qryStruct.gather_mon = DateUtil.getDiffMonth(-1,
					DateUtil.getNowDate());
		}

		//
		// qryStruct.adhoc_id = adhoc_root;
		// 保存查询结构
		session.removeAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
		session.setAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT, qryStruct);

		this.setNextScreen(request, "AdhocParam.screen");

	}

}
