package com.ailk.bi.subject.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
import com.ailk.bi.subject.dao.impl.OppDao;
import com.ailk.bi.subject.facade.OppFacade;
import com.ailk.bi.subject.service.impl.OppService;
import com.ailk.bi.base.common.CSysCode;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class OppSubjectHTMLAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();

		// 获取页面screen标示
		String optype = request.getParameter("optype");

		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}

		// 查询结构，接受界面条件值
		ReportQryStruct qryStruct = null;
		qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		} else {
			qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
			}
		}
		qryStruct.optype = optype;
		if (qryStruct.price_plan_id.length() == 0) {
			qryStruct.price_plan_desc = "全部";
		}

		// 加入权限控制条件-用户控制区域
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}

		if (qryStruct.gather_month == null || "".equals(qryStruct.gather_month)) {
			if (optype.equals("lose_off_fc") || optype.equals("lose_off_ppc")) {
				qryStruct.gather_month = DateUtil.getDiffMonth(0,
						DateUtil.getNowDate());
			} else {
				qryStruct.gather_month = DateUtil.getDiffMonth(-1,
						DateUtil.getNowDate());
			}
		}
		if (qryStruct.date_s == null || "".equals(qryStruct.date_s)) {
			qryStruct.date_s = qryStruct.gather_month.substring(0, 4) + "01";
		}
		if (qryStruct.date_e == null || "".equals(qryStruct.date_e)) {
			qryStruct.date_e = DateUtil.getDiffMonth(0, DateUtil.getNowDate());
		}

		if ("OppStable".equals(optype)) {
			//
			OppFacade facade = new OppFacade();
			OppService service = new OppService();
			service.setDao(new OppDao());
			facade.setService(service);
			//
			HashMap map = (HashMap) facade
					.getOppParamWeightMap(CSysCode.SUBJECT_OPP_STABLE);
			//
			if (qryStruct.call_times == null || "".equals(qryStruct.call_times)) {
				qryStruct.call_times = map.get("call_times") + "";
			}

			if (qryStruct.calling_times == null
					|| "".equals(qryStruct.calling_times)) {
				qryStruct.calling_times = map.get("calling_times") + "";
			}

			if (qryStruct.called_times == null
					|| "".equals(qryStruct.called_times)) {
				qryStruct.called_times = map.get("called_times") + "";
			}

			if (qryStruct.call_dura == null || "".equals(qryStruct.call_dura)) {
				qryStruct.call_dura = map.get("call_dura") + "";
			}

			if (qryStruct.calling_dura == null
					|| "".equals(qryStruct.calling_dura)) {
				qryStruct.calling_dura = map.get("calling_dura") + "";
			}

			if (qryStruct.called_dura == null
					|| "".equals(qryStruct.called_dura)) {
				qryStruct.called_dura = map.get("called_dura") + "";
			}

			if (qryStruct.avg_call_dura == null
					|| "".equals(qryStruct.avg_call_dura)) {
				qryStruct.avg_call_dura = map.get("avg_call_dura") + "";
			}

			if (qryStruct.sphere_num == null || "".equals(qryStruct.sphere_num)) {
				qryStruct.sphere_num = map.get("sphere_num") + "";
			}

			if (qryStruct.opp_stable_value == null
					|| "".equals(qryStruct.opp_stable_value)) {
				qryStruct.opp_stable_value = "10";
			}

			if (qryStruct.opp_rownum == null || "".equals(qryStruct.opp_rownum)) {
				qryStruct.opp_rownum = "501";
			}

			if (qryStruct.carrier_id == null || "".equals(qryStruct.carrier_id)) {
				qryStruct.carrier_id = "20";
			}

			if (qryStruct.carrier_name == null
					|| "".equals(qryStruct.carrier_name)) {
				qryStruct.carrier_name = "中国移动";
			}

			try {
				System.out.println(qryStruct.toXml());
			} catch (AppException e) {

				e.printStackTrace();
			}

			String[] param = { qryStruct.carrier_name, qryStruct.call_times,
					qryStruct.calling_times, qryStruct.called_times,
					qryStruct.call_dura, qryStruct.calling_dura,
					qryStruct.called_dura, qryStruct.avg_call_dura,
					qryStruct.sphere_num, qryStruct.carrier_id,
					qryStruct.gather_month, qryStruct.opp_stable_value,
					qryStruct.opp_rownum };

			String[][] value = CommTool.queryArrayFacade("OPP1101", param);

			// 大数据量导出
			try {
				String[] param1 = { qryStruct.carrier_name,
						qryStruct.call_times, qryStruct.calling_times,
						qryStruct.called_times, qryStruct.call_dura,
						qryStruct.calling_dura, qryStruct.called_dura,
						qryStruct.avg_call_dura, qryStruct.sphere_num,
						qryStruct.carrier_id, qryStruct.gather_month,
						qryStruct.opp_stable_value, CSysCode.SUBJECT_OPP_ROWNUM };

				String sql = SQLGenator.genSQL("OPP1101", param1);
				session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_SQL, sql);
				List listField = new ArrayList();
				listField.add("NUM1|手机号码||||");
				listField.add("NUM2|运营商||||");
				listField.add("NUM11|稳定性系数|||2|c");
				listField.add("NUM3|通话次数系数|||2|c");
				listField.add("NUM4|主叫次数系数|||2|c");
				listField.add("NUM5|被叫次数系数|||2|c");
				listField.add("NUM6|通话时长系数|||2|c");
				listField.add("NUM7|主叫时长系数|||2|c");
				listField.add("NUM8|被叫时长系数|||2|c");
				listField.add("NUM9|次均时长系数|||2|c");
				listField.add("NUM10|交往圈系数|||2|c");

				session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_TABLE_TITLE,
						listField);
			} catch (AppException ex1) {
				//
				ex1.printStackTrace();
			}

			session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_STABLE_lIST, value);

		} else if ("OppValue".equals(optype)) {
			//
			OppFacade facade = new OppFacade();
			OppService service = new OppService();
			service.setDao(new OppDao());
			facade.setService(service);
			//
			HashMap map = (HashMap) facade
					.getOppParamWeightMap(CSysCode.SUBJECT_OPP_VALUE);
			//
			if (qryStruct.avg_3_call_times == null
					|| "".equals(qryStruct.avg_3_call_times)) {
				qryStruct.avg_3_call_times = map.get("avg_3_call_times") + "";
			}

			if (qryStruct.avg_3_call_dura == null
					|| "".equals(qryStruct.avg_3_call_dura)) {
				qryStruct.avg_3_call_dura = map.get("avg_3_call_dura") + "";
			}

			if (qryStruct.avg_3_avg_call_dura == null
					|| "".equals(qryStruct.avg_3_avg_call_dura)) {
				qryStruct.avg_3_avg_call_dura = map.get("avg_3_avg_call_dura")
						+ "";
			}

			if (qryStruct.avg_3_zbjxs == null
					|| "".equals(qryStruct.avg_3_zbjxs)) {
				qryStruct.avg_3_zbjxs = map.get("avg_3_zbjxs") + "";
			}

			if (qryStruct.avg_3_sphere_num == null
					|| "".equals(qryStruct.avg_3_sphere_num)) {
				qryStruct.avg_3_sphere_num = map.get("avg_3_sphere_num") + "";
			}

			if (qryStruct.stable_value == null
					|| "".equals(qryStruct.stable_value)) {
				qryStruct.stable_value = map.get("stable_value") + "";
			}

			if (qryStruct.avg_3_ct_vip_unum == null
					|| "".equals(qryStruct.avg_3_ct_vip_unum)) {
				qryStruct.avg_3_ct_vip_unum = map.get("avg_3_ct_vip_unum") + "";
			}

			if (qryStruct.avg_3_ct_hign_value_unum == null
					|| "".equals(qryStruct.avg_3_ct_hign_value_unum)) {
				qryStruct.avg_3_ct_hign_value_unum = map
						.get("avg_3_ct_hign_value_unum") + "";
			}

			if (qryStruct.opp_valued_value == null
					|| "".equals(qryStruct.opp_valued_value)) {
				qryStruct.opp_valued_value = "10";
			}

			if (qryStruct.opp_rownum == null || "".equals(qryStruct.opp_rownum)) {
				qryStruct.opp_rownum = "501";
			}

			if (qryStruct.carrier_id == null || "".equals(qryStruct.carrier_id)) {
				qryStruct.carrier_id = "20";
			}

			if (qryStruct.carrier_name == null
					|| "".equals(qryStruct.carrier_name)) {
				qryStruct.carrier_name = "中国移动";
			}

			try {
				System.out.println(qryStruct.toXml());
			} catch (AppException e) {

				e.printStackTrace();
			}

			String[] param = { qryStruct.carrier_name,
					qryStruct.avg_3_call_times, qryStruct.avg_3_call_dura,
					qryStruct.avg_3_avg_call_dura, qryStruct.avg_3_zbjxs,
					qryStruct.avg_3_sphere_num, qryStruct.stable_value,
					qryStruct.avg_3_ct_vip_unum,
					qryStruct.avg_3_ct_hign_value_unum, qryStruct.carrier_id,
					qryStruct.gather_month, qryStruct.opp_valued_value,
					qryStruct.opp_rownum };
			String[][] value = CommTool.queryArrayFacade("OPP1102", param);

			// 大数据量导出
			try {
				String[] param1 = { qryStruct.carrier_name,
						qryStruct.avg_3_call_times, qryStruct.avg_3_call_dura,
						qryStruct.avg_3_avg_call_dura, qryStruct.avg_3_zbjxs,
						qryStruct.avg_3_sphere_num, qryStruct.stable_value,
						qryStruct.avg_3_ct_vip_unum,
						qryStruct.avg_3_ct_hign_value_unum,
						qryStruct.carrier_id, qryStruct.gather_month,
						qryStruct.opp_valued_value, CSysCode.SUBJECT_OPP_ROWNUM };

				String sql = SQLGenator.genSQL("OPP1102", param1);
				session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_SQL, sql);
				List listField = new ArrayList();

				listField.add("NUM1|手机号码||||");
				listField.add("NUM2|运营商||||");
				listField.add("NUM11|稳定性系数|||2|c");
				listField.add("NUM3|近3月通话次数估值|||2|c");
				listField.add("NUM4|近3月通话时长估值|||2|c");
				listField.add("NUM5|近3月次均时长估值|||2|c");
				listField.add("NUM6|近3月主被叫估值|||2|c");
				listField.add("NUM7|近3月交往圈估值|||2|c");
				listField.add("NUM8|稳定性估值|||2|c");
				listField.add("NUM9|电信高端客户数估值|||2|c");
				listField.add("NUM10|电信高价值客户数估值|||2|c");

				session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_TABLE_TITLE,
						listField);
			} catch (AppException ex1) {
				//
				ex1.printStackTrace();
			}

			session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_VALUE_lIST, value);

		} else if ("opcust_cf".equals(optype)) {
			OppFacade facade = new OppFacade();
			OppService service = new OppService();
			service.setDao(new OppDao());
			facade.setService(service);
			//
			HashMap map = (HashMap) facade
					.getOppParamWeightMap(CSysCode.SUBJECT_OPP_BACK);
			//
			if (qryStruct.high_value_cust_amt_lvl_id == null
					|| "".equals(qryStruct.high_value_cust_amt_lvl_id)) {
				qryStruct.high_value_cust_amt_lvl_id = map
						.get("high_value_cust_amt_lvl_id") + "";
			}

			if (qryStruct.rela_net_cust_amt_lvl_id == null
					|| "".equals(qryStruct.rela_net_cust_amt_lvl_id)) {
				qryStruct.rela_net_cust_amt_lvl_id = map
						.get("rela_net_cust_amt_lvl_id") + "";
			}

			if (qryStruct.high_cust_amt_lvl_id == null
					|| "".equals(qryStruct.high_cust_amt_lvl_id)) {
				qryStruct.high_cust_amt_lvl_id = map
						.get("high_cust_amt_lvl_id") + "";
			}

			if (qryStruct.stability_lvl_id == null
					|| "".equals(qryStruct.stability_lvl_id)) {
				qryStruct.stability_lvl_id = map.get("stability_lvl_id") + "";
			}

			if (qryStruct.opp_user_value_eval_val == null
					|| "".equals(qryStruct.opp_user_value_eval_val)) {
				qryStruct.opp_user_value_eval_val = map
						.get("opp_user_value_eval_val") + "";
			}

			if (qryStruct.busi_use_tag == null
					|| "".equals(qryStruct.busi_use_tag)) {
				qryStruct.busi_use_tag = map.get("busi_use_tag") + "";
			}

			if (qryStruct.cservice_tag == null
					|| "".equals(qryStruct.cservice_tag)) {
				qryStruct.cservice_tag = map.get("cservice_tag") + "";
			}

			if (qryStruct.call_times_lvl_id == null
					|| "".equals(qryStruct.call_times_lvl_id)) {
				qryStruct.call_times_lvl_id = map.get("call_times_lvl_id") + "";
			}

			if (qryStruct.call_evarage_dura_lvl_id == null
					|| "".equals(qryStruct.call_evarage_dura_lvl_id)) {
				qryStruct.call_evarage_dura_lvl_id = map
						.get("call_evarage_dura_lvl_id") + "";
			}
			if (qryStruct.opp_valued_value == null
					|| "".equals(qryStruct.opp_valued_value)) {
				qryStruct.opp_valued_value = "10";
			}

			if (qryStruct.opp_stable_value == null
					|| "".equals(qryStruct.opp_stable_value)) {
				qryStruct.opp_stable_value = "10";
			}

			if (qryStruct.opp_rownum == null || "".equals(qryStruct.opp_rownum)) {
				qryStruct.opp_rownum = "501";
			}

			if (qryStruct.carrier_id == null || "".equals(qryStruct.carrier_id)) {
				qryStruct.carrier_id = "20";
			}

			if (qryStruct.carrier_name == null
					|| "".equals(qryStruct.carrier_name)) {
				qryStruct.carrier_name = "中国移动";
			}
			try {
				System.out.println(qryStruct.toXml());
			} catch (AppException e) {

				e.printStackTrace();
			}

			String[] param = { qryStruct.carrier_name,
					qryStruct.high_value_cust_amt_lvl_id,
					qryStruct.high_cust_amt_lvl_id, qryStruct.stability_lvl_id,
					qryStruct.busi_use_tag, qryStruct.cservice_tag,
					qryStruct.rela_net_cust_amt_lvl_id,
					qryStruct.call_times_lvl_id,
					qryStruct.call_evarage_dura_lvl_id, qryStruct.carrier_id,
					qryStruct.gather_month, qryStruct.opp_stable_value,
					qryStruct.opp_valued_value, qryStruct.opp_rownum };
			String[][] value = CommTool.queryArrayFacade("OPP1103", param);

			// 大数据量导出
			try {

				String[] param1 = { qryStruct.carrier_name,
						qryStruct.high_value_cust_amt_lvl_id,
						qryStruct.high_cust_amt_lvl_id,
						qryStruct.stability_lvl_id, qryStruct.busi_use_tag,
						qryStruct.cservice_tag,
						qryStruct.rela_net_cust_amt_lvl_id,
						qryStruct.call_times_lvl_id,
						qryStruct.call_evarage_dura_lvl_id,
						qryStruct.carrier_id, qryStruct.gather_month,
						qryStruct.opp_stable_value, qryStruct.opp_valued_value,
						CSysCode.SUBJECT_OPP_ROWNUM };

				String sql = SQLGenator.genSQL("OPP1103", param1);
				session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_SQL, sql);
				List listField = new ArrayList();

				listField.add("NUM1|手机号码||||");
				listField.add("NUM2|运营商||||");
				listField.add("NUM12|策反评估指数|||2|c");
				listField.add("NUM3|含电信高价值客户估值|||2|c");
				listField.add("NUM4|含电信高端客户估值|||2|c");
				listField.add("NUM5|稳定性系数估值|||2|c");
				listField.add("NUM6|高价值系数估值|||2|c");
				listField.add("NUM7|业务预警估值|||2|c");
				listField.add("NUM8|客服预警估值|||2|c");
				listField.add("NUM9|近3个月月均交往圈客户数等级估值|||2|c");
				listField.add("NUM10|近3个月月均话次数系数估值|||2|c");
				listField.add("NUM11|近3月月均通话时长系数估值|||2|c");

				session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_TABLE_TITLE,
						listField);
			} catch (AppException ex1) {
				//
				ex1.printStackTrace();
			}

			session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_CF_lIST, value);

		} else if ("opcust_fcf".equals(optype)) {
			//
			OppFacade facade = new OppFacade();
			OppService service = new OppService();
			service.setDao(new OppDao());
			facade.setService(service);
			//
			HashMap map = (HashMap) facade
					.getOppParamWeightMap(CSysCode.SUBJECT_OPP_CUST_FCF_VALUE);
			//
			if (qryStruct.opp_gwdfx == null || "".equals(qryStruct.opp_gwdfx)) {
				qryStruct.opp_gwdfx = map.get("opp_gwdfx") + "";
			}

			if (qryStruct.opp_gjzfx == null || "".equals(qryStruct.opp_gjzfx)) {
				qryStruct.opp_gjzfx = map.get("opp_gjzfx") + "";
			}

			if (qryStruct.opp_jwq == null || "".equals(qryStruct.opp_jwq)) {
				qryStruct.opp_jwq = map.get("opp_jwq") + "";
			}

			if (qryStruct.opp_gjzkh == null || "".equals(qryStruct.opp_gjzkh)) {
				qryStruct.opp_gjzkh = map.get("opp_gjzkh") + "";
			}

			if (qryStruct.opp_kfyj == null || "".equals(qryStruct.opp_kfyj)) {
				qryStruct.opp_kfyj = map.get("opp_kfyj") + "";
			}

			if (qryStruct.opp_ywyj == null || "".equals(qryStruct.opp_ywyj)) {
				qryStruct.opp_ywyj = map.get("opp_ywyj") + "";
			}

			//
			if (qryStruct.opp_valued_value == null
					|| "".equals(qryStruct.opp_valued_value)) {
				qryStruct.opp_valued_value = "10";
			}

			if (qryStruct.opp_stable_value == null
					|| "".equals(qryStruct.opp_stable_value)) {
				qryStruct.opp_stable_value = "10";
			}

			if (qryStruct.opp_rownum == null || "".equals(qryStruct.opp_rownum)) {
				qryStruct.opp_rownum = "501";
			}

			try {
				System.out.println(qryStruct.toXml());
			} catch (AppException e) {

				e.printStackTrace();
			}

			String[] param = { qryStruct.opp_gjzkh, qryStruct.opp_jwq,
					qryStruct.opp_gjzfx, qryStruct.opp_gwdfx,
					qryStruct.opp_ywyj, qryStruct.opp_kfyj,
					qryStruct.gather_month, qryStruct.opp_stable_value,
					qryStruct.opp_valued_value, qryStruct.opp_rownum };
			String[][] value = CommTool.queryArrayFacade("OPP1104", param);

			try {
				String[] param1 = { qryStruct.opp_gjzkh, qryStruct.opp_jwq,
						qryStruct.opp_gjzfx, qryStruct.opp_gwdfx,
						qryStruct.opp_ywyj, qryStruct.opp_kfyj,
						qryStruct.gather_month, qryStruct.opp_stable_value,
						qryStruct.opp_valued_value, CSysCode.SUBJECT_OPP_ROWNUM };

				String sql = SQLGenator.genSQL("OPP1104", param1);
				session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_SQL, sql);
				List listField = new ArrayList();
				listField.add("NUM1|手机号码||||");
				listField.add("NUM8|策反风险系数|||2|c");
				listField.add("NUM2|高价值客户评估值|||2|c");
				listField.add("NUM3|交往圈客户评估值|||2|c");
				listField.add("NUM4|高价值对手风险评估值|||2|c");
				listField.add("NUM5|高稳定对收风险评估值|||2|c");
				listField.add("NUM6|业务预警估值|||2|c");
				listField.add("NUM7|客服预警估值|||2|c");

				session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_lIST_TABLE_TITLE,
						listField);

			} catch (AppException ex1) {
				//
				ex1.printStackTrace();
			}
			session.setAttribute(WebKeys.ATTR_OPP_SUBJECT_FCF_lIST, value);

		} else if ("feeCommendShortMsg".equalsIgnoreCase(optype)) {
			// 短信资费推荐
			/*
			 * //dim1：资费类别 if (qryStruct.dim1 == null ||
			 * "".equals(qryStruct.dim1)) { qryStruct.dim1 = "1"; //默认是短信
			 * ，2：表示流量 }
			 */
			// dim2：饱和度
			if (qryStruct.dim2 == null || "".equals(qryStruct.dim2)) {
				qryStruct.dim2 = "5";
			}

			// dim3：增值资费ID
			if (qryStruct.dim3 == null || "".equals(qryStruct.dim3)) {
				qryStruct.dim3 = "20314"; // 短信5元
			}

			if (qryStruct.dim15 == null || "".equals(qryStruct.dim15)) {
				qryStruct.dim15 = "";
			}

			if (qryStruct.dim16 == null || "".equals(qryStruct.dim16)) {
				qryStruct.dim16 = "";
			}

		} else if ("feeCommendFlow".equalsIgnoreCase(optype)) {
			// 流量资费推荐
			/*
			 * //dim1：资费类别 if (qryStruct.dim1 == null ||
			 * "".equals(qryStruct.dim1)) { qryStruct.dim1 = "1"; //默认是短信
			 * ，2：表示流量 }
			 */
			// dim2：饱和度
			if (qryStruct.dim2 == null || "".equals(qryStruct.dim2)) {
				qryStruct.dim2 = "5";
			}

			// dim3：增值资费ID
			if (qryStruct.dim3 == null || "".equals(qryStruct.dim3)) {
				qryStruct.dim3 = "20518"; // 3G-10元套餐(行业流量)
			}

			qryStruct.dim15 = "";
			qryStruct.dim16 = "";

		} else if ("contentComm".equalsIgnoreCase(optype)) {
			// 内容订购推荐

			// dim3：增值资费ID
			if (qryStruct.dim3 == null || "".equals(qryStruct.dim3)) {
				qryStruct.dim3 = "235000000000000000789"; // 新闻早晚报
			}

			if (qryStruct.dim15 == null || "".equals(qryStruct.dim15)) {
				qryStruct.dim15 = "";
			}

			if (qryStruct.dim16 == null || "".equals(qryStruct.dim16)) {
				qryStruct.dim16 = "";
			}

		} else if (optype.equalsIgnoreCase("opp_user_detail_show")) {
			// 竞争对手清单

			if (qryStruct.dim3 == null || "".equals(qryStruct.dim3)) {
				qryStruct.dim3 = "20"; // 中国移动
			}
			if (qryStruct.dim4 == null || "".equals(qryStruct.dim4)) {
				qryStruct.dim4 = "call_times"; // 通话次数
			}
			if (qryStruct.dim5 == null || "".equals(qryStruct.dim5)) {
				qryStruct.dim5 = "1000"; // 条数
			}
		} else if (optype.equalsIgnoreCase("lose_cust")) {
			if (qryStruct.online_lvl_id == null
					|| "".equals(qryStruct.online_lvl_id)) {
				qryStruct.online_lvl_id = ""; // 在网时长
			}
			if (qryStruct.online_lvl_desc == null
					|| "".equals(qryStruct.online_lvl_desc)) {
				qryStruct.online_lvl_desc = "全部"; // 在网时长
			}
		}
		session.setAttribute("VIEW_TREE_LIST", qryOppUserDetail(qryStruct));
		request.setAttribute("init", true);
		// setNextScreen(request, "opp_user_detail_show.screen");

		// 把结果集存入会话
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		// 返回

		String tabFeeFlag = request.getParameter("tab_fee_flag");

		if (tabFeeFlag != null) {
			session.setAttribute("monitor_addval_tabFee", tabFeeFlag);
		}

		setNextScreen(request, optype + ".screen");
	}

	private String[][] qryOppUserDetail(ReportQryStruct qryStruct) {

		String sql = "select t.op_time,t.opp_acc_nbr,t.opp_carrier_id,t.online_dura,t.call_times,"
				+ "t.calling_times,t.called_times,t.call_dura,t.calling_dura,t.called_dura "
				+ "from new_bi_ui.FUI_OPP_BUSI_SPHERE_M t where op_time="
				+ qryStruct.gather_month;

		if (!qryStruct.dim3.equalsIgnoreCase("0")) {
			sql += " and opp_carrier_id=" + qryStruct.dim3;
		}

		if (qryStruct.dim4.length() > 0) {
			sql += " order by " + qryStruct.dim4 + " desc";
		}

		String sqlTmp = "select op_time,opp_acc_nbr,opp_carrier_id,online_dura,call_times,"
				+ "calling_times,called_times,call_dura,calling_dura,called_dura from ("
				+ " select JFTOT.*,ROWNUM RN from ("
				+ sql
				+ ") JFTOT Where rownum<=?) where RN>?";

		String strWhere[] = new String[] { qryStruct.dim5 + "|1", "0|1" };

		System.out.println(sqlTmp);
		String res[][] = null;
		try {
			res = WebDBUtil.execQryArray(sqlTmp, strWhere, "");
		} catch (AppException e) {

			e.printStackTrace();
		}

		return res;
	}

}
