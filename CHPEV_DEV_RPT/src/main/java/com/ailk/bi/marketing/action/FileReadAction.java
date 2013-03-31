package com.ailk.bi.marketing.action;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
/**
 * @实现文件上传
 * 【action控制层】实现文件上传
 * @author  方慧
 * @version  [版本号, 2012-04-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FileReadAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
		// 获取页面screen标示
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		//String doType = request.getParameter("fileUrl");//获得具体的操作
		String fileUrl=request.getParameter("fileUrl");
		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}
		// 查询结构，接受界面条件值
		ReportQryStruct qryStruct = new ReportQryStruct();
		// 判断是否有外部传入条件，p_condition约定名称
		String p_condition = request.getParameter("p_condition");
		if (StringTool.checkEmptyString(p_condition)) {
			p_condition = ReportConsts.NO;
		}
		try {
			if (ReportConsts.YES.equals(p_condition)) {
				qryStruct = (ReportQryStruct) session
						.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
			} else {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			}
		} catch (AppException ex) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
					"提取界面查询信息失败，请注意是否登陆超时！");
		}
		// 加入权限控制条件-用户控制区域
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}
		/**
		 *
		 *开始上传文件
		 *@author  方慧 f00211612
		 *@date 2012-04-21
		 * */
		List<String> list = new ArrayList<String>();
			BufferedReader in = null;
		    String   line="";
			try {
				in = new   BufferedReader(new   FileReader( fileUrl));
				while((line   =   in.readLine())!=null){
					list.add(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
