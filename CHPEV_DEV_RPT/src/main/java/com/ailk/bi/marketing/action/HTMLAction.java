package com.ailk.bi.marketing.action;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ailk.bi.base.common.InitServlet;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.marketing.entity.TacticInfo;
import com.ailk.bi.marketing.service.ITacticService;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
/**
 * @实现对营销策略增删改查的控制
 * 【action控制层】营销策略控制层
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HTMLAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "tacticService")
	private ITacticService tacticService;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		/**
		 * 业务开始
		 *@author f00211612
		 * */
		// 查询结构，接受界面条件值
		HttpSession session = request.getSession();
		// 查询结构，接受界面条件值
		ReportQryStruct qryStruct = new ReportQryStruct();
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		String doType = request.getParameter("doType");//获得具体的操作
			if("searchTactic".equals(doType)){
				String tacticName = request.getParameter("tacticName");//获得条件值
				String tacticType = request.getParameter("tacticType");//获得条件值
				if (StringTool.checkEmptyString(qryStruct.tactic_name)) {
					qryStruct.tactic_name = tacticName;
				}
				if (StringTool.checkEmptyString(qryStruct.tactic_type)) {
					qryStruct.tactic_type = tacticType;
				}
				InitServlet.init(super.config, this, "tacticService");
				if (null != tacticService) {
					TacticInfo entity = new TacticInfo();
					if (!StringTool.checkEmptyString(tacticName)) {
						entity.setTacticName(tacticName);
					}
					if (!StringTool.checkEmptyString(tacticType)) {
						entity.setTacticType(Integer.parseInt(tacticType));
					}else{
						entity.setTacticType(-999);//不做查询条件
					}
					entity.setState(2);//设置条件 已审批的
					List<TacticInfo> list = tacticService.getAll(entity, 0);
					session.setAttribute("tacticInfoList", list);
				}else if("searchProject".equals(doType)){


				}
		}


		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");

	}
}
