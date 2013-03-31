package com.ailk.bi.marketing.action;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.ailk.bi.base.common.InitServlet;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.marketing.entity.ActivityModeInfo;
import com.ailk.bi.marketing.entity.FileInfo;
import com.ailk.bi.marketing.service.IActivityModeService;
import com.ailk.bi.marketing.service.IFileService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
/**
 *实现对营销案例增删改查的控制
 * 【action控制层】活动目标控制层
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ActivityModeAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "activityModeService")
	private IActivityModeService activityModeService;
	@Resource(name = "fileService")
	private IFileService fileService;
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
		// 获取页面screen标示
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		String doType = request.getParameter("doType");//获得具体的操作
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
		 * 业务开始
		 *@author f00211612
		 * */

		InitServlet.init(super.config, this, "activityModeService");
		InitServlet.init(super.config, this, "fileService");

		if (null != activityModeService) {
			if ("search".equals(doType)) {
			}else if("delect".equals(doType)) {
			}else if("add".equals(doType)) {
			}else if("modify".equals(doType)) {
			}else if("save".equals(doType)) {

				String content = request.getParameter("txt_modeContent");
				String activityId = request.getParameter("txt_activityId");
				ActivityModeInfo modeInfo = (ActivityModeInfo)session.getAttribute("modeInfo");
				if(null ==modeInfo){
					 modeInfo  = new ActivityModeInfo();
				}
				Date date = new Date();
				modeInfo.setCreateDate(date);
			 	if (!StringTool.checkEmptyString(activityId)) {
			 		modeInfo.setActivityId(Integer.parseInt(activityId));
				}
				if (!StringTool.checkEmptyString(content)) {
					modeInfo.setContent(content);
				}
				boolean  addResult = activityModeService.save(modeInfo);
				int newid = modeInfo.getModeId();
				session.setAttribute("modeInfo",modeInfo);
		    	///////开始添加文件
				fileService.delete(String.valueOf(newid));
				@SuppressWarnings("unchecked")
				List<FileInfo> files = (List<FileInfo>) session.getAttribute("modeFiles");
			 	if(null!=files){
				    for(int i =0;i<files.size();i++){
				    	FileInfo ff = files.get(i);
				    	ff.setRelationshipId(newid);
				    	fileService.save(ff);
				  	}
				}
				session.setAttribute("modeFiles",null);
				//保存后重新刷新
			 	request.setAttribute("addResult", String.valueOf(addResult));
				try {
					response.sendRedirect("activityAction.rptdo?optype=activityPass&doType=modify&activityId="+activityId);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
