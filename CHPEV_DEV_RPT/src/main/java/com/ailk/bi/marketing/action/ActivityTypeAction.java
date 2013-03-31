package com.ailk.bi.marketing.action;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.ailk.bi.marketing.entity.ActivityTypeInfo;
import com.ailk.bi.marketing.service.IActivityService;
import com.ailk.bi.marketing.service.IActivityTypeService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class ActivityTypeAction extends HTMLActionSupport implements
		Serializable {

	/**
	 *为保证 serialVersionUID 值跨不同 java 编译器实现的一致性，
	 *序列化类必须声明一个明确的 serialVersionUID 值
	 */
	private static final long serialVersionUID = 1L;

	//注入活动类型service类
	@Resource( name = "activityTypeService")
	private IActivityTypeService activityTypeService;
	@Resource( name = "activityService")
	private IActivityService activityService;

	/**
	 *中心控制类，是连接前台展示和后台处理的桥梁
	 *此函数为前台与后台的中心控制函数，前台调用后台方法实现不同的功能，
	 *必须通过此类调用并且将返回值通过此类返回前台。
	 *@param [参数1]     [参数1说明]
	 *@return  [返回类型说明]
	 *@exception/throws [异常类型] [异常说明]
	 *@see   [类、类#方法、类#成员]
	 */
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		// 验证用户是否已登陆
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;
		//取得session对象
		HttpSession session = request.getSession();
		//optype为指定要返回的页面；doType为指定要执行的方法
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		String doType = request.getParameter("doType");//获得具体的操作
		if (optype == null || "".equals(optype))		{
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}
		//此类集成整个系统查询条件，所有查询条件都保存到此类中
		ReportQryStruct qryStruct = new ReportQryStruct();
		//此处解释待定
		String p_condition = request.getParameter("p_condition");
		if (StringTool.checkEmptyString(p_condition)){
			p_condition = ReportConsts.NO;
		}
		//此处解释待定
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
		/**
		 * 取出列表查询条件，如果没有查询条件的页面取出值为空
		 * 如果类qryStruct中的查询条件属性为空字符串则将查询条件值赋进去
		 */
		String activityTypeName = request.getParameter("qry_activityType_name");
		if (StringTool.checkEmptyString(qryStruct.activityType_name)) {
			qryStruct.activityType_name = activityTypeName;
		}

		/**
		 *加入权限控制条件-用户控制区域
		 *自己理解详细描述待定
		 */
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null){
			ctlStruct = new UserCtlRegionStruct();
		}

		/**
		 * 以上为此action类必须具有的语句，详细涵义，见注释
		 * 一下为调用不同函数查出数据返回给Action类
		 */

		//实例化ChannleTypeService类
		InitServlet.init(super.config, this, "activityTypeService");
		InitServlet.init(super.config, this, "activityService");


		/**
		 * 查出渠道类型列表集合
		 */

			if(activityTypeService != null)
			{
				ActivityTypeInfo entity = new ActivityTypeInfo();
				if (!StringTool.checkEmptyString(activityTypeName)) {
					entity.setActivityTypeName(activityTypeName);
				}
				entity.setContactCount(-999);
				entity.setState(-999);
				if("search".equals(doType))
				{
					List<ActivityTypeInfo> list = activityTypeService.getAll(entity, 0);

					/**
					 * 把结果数据保存到request中
					 */
					request.setAttribute("activityTypeList", list);
				}
				else if("delect".equals(doType))
				{
					String ids = "";
					String[] arr = request.getParameterValues("checkbox");
					if(null!=arr){
						for(int i=0;i<arr.length;i++){

							int prowount = 	activityService.getCountBytypeId(Integer.parseInt(arr[i]));
							 if(prowount>0){
								request.setAttribute("delectMsg", "该类型被某个活动引用,不能直接删除！");
								}else{
									ids+=arr[i]+",";
								}
						}
					}
					if(""!=ids){
						char lastChar =',';
						if(ids.charAt(ids.length()-1)==lastChar)
						{
							ids = ids.substring(0, ids.length()-1);
						}
						activityTypeService.delete(ids);
					}

					//删除后重新刷新
					List<ActivityTypeInfo> list = activityTypeService.getAll(entity, 0);
					request.setAttribute("activityTypeList", list);
				}
				else if("add".equals(doType))
				{
					ActivityTypeInfo activityTypeInfo = new ActivityTypeInfo();
					session.setAttribute("activityTypeInfo", activityTypeInfo);
				}
				else if("modify".equals(doType))
				{
					session.setAttribute("activityTypeInfo", null);
					ActivityTypeInfo activityTypeInfo =null;
					String activityTypeId = request.getParameter("activityTypeId");
					if (!StringTool.checkEmptyString(activityTypeId)) {
						activityTypeInfo = activityTypeService.getById(Integer.parseInt(activityTypeId));
					}
					session.setAttribute("activityTypeInfo", activityTypeInfo);
				}
				else if("save".equals(doType))
				{
					ActivityTypeInfo activityTypeInfo = (ActivityTypeInfo) session.getAttribute("activityTypeInfo");
					if(null==activityTypeInfo){
						activityTypeInfo = new ActivityTypeInfo();
					}
					session.setAttribute("activityTypeInfo",null);
					String activityType_name = request.getParameter("activityTypeName");
					String contackCount = request.getParameter("contactCount");
					String set_time = request.getParameter("setTime");
					String type_desc = request.getParameter("typeDesc");
					if (!StringTool.checkEmptyString(activityType_name)) {
						activityTypeInfo.setActivityTypeName(activityType_name);
					}
					if (!StringTool.checkEmptyString(contackCount)) {
						activityTypeInfo.setContactCount(Integer.parseInt(contackCount));
					}
					if (!StringTool.checkEmptyString(set_time)) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						try {
							activityTypeInfo.setSetTime(sdf.parse(set_time));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if (!StringTool.checkEmptyString(type_desc)) {
						activityTypeInfo.setTypeDesc(type_desc);
					}
					activityTypeService.save(activityTypeInfo);
					//保存后重新刷新
					List<ActivityTypeInfo> list = activityTypeService.getAll(entity, 0);
					request.setAttribute("activityTypeList", list);
				}
			}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
