package com.ailk.bi.subject.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.common.app.AppConst;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.subject.admin.dao.impl.AiModResultDaoImpl;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class AiModResultHTMLAction extends HTMLActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3542068140768380229L;

	@SuppressWarnings("rawtypes")
	public void doTrans(HttpServletRequest request, HttpServletResponse response) throws HTMLActionException {
		//String head_string = "";
		String head_detail = "";
		String result2 = "";
		List result_string = new ArrayList(); // 本次预演
		List result_string2 = new ArrayList();// 预演前
		// 判断用户是否有效登陆
//		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
//			return;
		HttpSession session = request.getSession();
		//预演的案例id
		 String case_id =
		 StringB.NulltoBlank(request.getParameter("case_id"));
		 if(case_id==null || case_id.equals("")){
		    throw new HTMLActionException(session,
		      HTMLActionException.WARN_PAGE, "传入的案例编码为空，结果不能展示！");
		 }
		//登录用户所属省份
		String province_code = StringB.NulltoBlank(request.getParameter("province_code"));
		
		String[] param = { case_id };
		// 政策是否是新增的
		String isadd = "0";
		// 查询案例关联的模板（是否导入模板）
		List modids = new ArrayList();
		List headlist2 = new ArrayList();
		/*1. 获取案例关联的政策*/
		try {
		    modids = AiModResultDaoImpl.getHeadList("6", param, "","");
		}catch(Exception e){
		    throw new HTMLActionException(e.toString());
		}
		if(modids.size()==0){
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "案例不存在！");
		}
		Map mods = (Map) modids.get(0);
		//案例关联政策ID的来源 现网政策ID
		String mod_src_id = (String) mods.get("MOD_SRC_ID");
		//案例关联政策ID
		String mod_id = (String) mods.get("MOD_ID");
		//计算文件来源  0:现网数据  1:预演数据
		String isimport = (String) mods.get("CAL_DATA_SRC");
		//预演数据的关联计算附件ID
		String temp_id = (String) mods.get("TEMP_ID");
		//现网数据：计算数据时间
		String month = (String) mods.get("CAL_DATA_DATE");
		if (isimport != null && isimport.equals(AppConst.IS_NoImport)) {
			if (mod_src_id == null || mod_src_id.equals("0"))
				isadd = "1";
		} else {
			isadd = "1";
		}

		String mod_name = "";
		String import_type="";
		String head1 = "";
		String head2 = "";
		String create_year = "";
		try {
		    /*2.根据计算数据来源，返回展示结果table的表头 */
		    /*2.1 导入模板类型*/
			if (isimport != null && isimport.equals(AppConst.IS_Import)) {
				String[] params3 = { temp_id };
				/*2.1.1 获取模板计算数据的类型及导入时间*/
				List mods2 = AiModResultDaoImpl.getHeadList("7", params3, "","");
				if(mods2==null || mods2.size()==0){
					throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "导入的模板不存在！");
				}
				Map temp = (Map) mods2.get(0);
				//模板类型
				import_type = (String) temp.get("TEMP_TYPE");
				//创建日期
				create_year = (String) temp.get("CREATE_DATE");
				//导入模板类型 01：用户数量表；02：用户比例表；03：产品数量表；04：产品比例表  设置表头信息
				if (import_type.equals(AppConst.USER_TEMP)) {
					head1 = AppConst.USER_NUMBER;
					head2 = AppConst.USER_CHANL_NUMBER;
				} else if (import_type.equals(AppConst.PRODUCT_TEMP)) {
					head1 = AppConst.PRODUCT_NUMBER;
					head2 = AppConst.PRODUCT_CHANL_NUMBER;
				} else if (import_type.equals(AppConst.USER_BI_TEMP)) {
					head1 = AppConst.USER_NUMBER;
					head2 = AppConst.USER_NUMBER_BI;
				} else {
					head1 = AppConst.PRODUCT_NUMBER;
					head2 = AppConst.PRODUCT_NUMBER_BI;
				}
				session.setAttribute("head1", head1);
				session.setAttribute("head2", head2);
			}
			 /*2.2 现网数据类型*/
			if (isadd.equals("0")) {
				String[] param3 = { mod_src_id };
				String mod_name2 = "";
				/*2.2.1 查询现网政策名称*/
				List mod2 = AiModResultDaoImpl.getHeadList("0", param3, "","");
				if (mod2 != null) {
					try {
						Map mod_map = (Map) mod2.get(0);
						mod_name2 = (String) mod_map.get("MOD_NAME");
					} catch (Exception e) {
						throw new HTMLActionException(session,HTMLActionException.WARN_PAGE, "政策不存在");
					}
				}
				/*2.2.2 查询现网政策关联的规则*/
				String rule_id2 = AiModResultDaoImpl.getRuleId(mod_src_id);
				if(rule_id2==null || rule_id2.equals("")){
					throw new HTMLActionException(session,HTMLActionException.WARN_PAGE, "政策规则不存在");
				}
				// 0：条件指标   1：计算指标
				String[] params = { "0", rule_id2 };
				/*2.2.3 查询规则关联的指标名称*/
				headlist2 = AiModResultDaoImpl.getHeadList("1", params, "","");
				result_string2 = AiModResultDaoImpl.getResultString(mod_name2,
						isimport, rule_id2, mod_src_id, month, "",temp_id,province_code,"0");
			}
			session.setAttribute("head2list", headlist2);

			//获取案例关联政策名称
			String[] params2 = { mod_id };
			List mod = AiModResultDaoImpl.getHeadList("0", params2, "","");
			if (mod != null) {
				try {
					Map mod_map = (Map) mod.get(0);
					mod_name = (String) mod_map.get("MOD_NAME");
				} catch (Exception e) {
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "政策不存在");
				}
			}
			//政策关联规则ID
			String rule_id = AiModResultDaoImpl.getRuleId(mod_id);
			if(rule_id==null || rule_id.equals("")){
				throw new HTMLActionException(session,HTMLActionException.WARN_PAGE, "政策规则不存在");
			}
			// 条件指标获取
			List headlist = new ArrayList();
			// 0：条件指标   1：计算指标
			String[] params = { "0", rule_id };
			//获取条件指标的名称
			headlist = AiModResultDaoImpl.getHeadList("1", params, "","");
			session.setAttribute("head", headlist);
			result_string = AiModResultDaoImpl.getResultString(mod_name,
					isimport, rule_id, mod_id, month, create_year,temp_id,province_code,"1");
			if(isimport.equals(AppConst.IS_Import)){
				result2 = AiModResultDaoImpl.getResult2( mod_id, temp_id,import_type);
				session.setAttribute("result2", result2);
				session.setAttribute("param", create_year+","+mod_id+","+temp_id+","+import_type);
			}
		} catch (AppException e) {
		    throw new HTMLActionException(e.toString());
		}
		session.setAttribute("isadd", isadd);
		session.setAttribute("result_string", result_string);
		if (isimport != null && isimport.equals(AppConst.IS_NoImport)) {
			session.setAttribute("result_string2", result_string2);
			setNextScreen(request, "AiModResultNoImpot.screen");
		} else {
			session.setAttribute("head_detail", head_detail);
			setNextScreen(request, "AiModResultImpot.screen");
		}
	}
}
