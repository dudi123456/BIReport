package com.ailk.bi.marketing.action;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.annotation.Resource;
import com.ailk.bi.base.common.InitServlet;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.marketing.entity.ChannleInfo;
import com.ailk.bi.marketing.entity.ChannleTypeInfo;
import com.ailk.bi.marketing.service.IActivityService;
import com.ailk.bi.marketing.service.IChannleService;
import com.ailk.bi.marketing.service.IChannleTypeService;
import com.ailk.bi.marketing.service.IProjectService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
/**
 *〈一句话功能简述〉
 *〈功能详细描述〉
 * @author w00211529
 * @see
 * @since 渠道管理
 */
public class ChannelManaAction extends HTMLActionSupport implements
		Serializable {

	/**
	 *为保证 serialVersionUID 值跨不同 java 编译器实现的一致性，
	 *序列化类必须声明一个明确的 serialVersionUID 值
	 */
	private static final long serialVersionUID = 1L;

	//注入渠道类型service类和渠道Service类
	@Resource( name = "channleTypeService")
	private IChannleTypeService channleTypeService;
	@Resource( name = "channleService")
	private IChannleService channleService;
	@Resource( name = "activityService")
	private IActivityService activityService;
	@Resource( name = "projectService")
	private IProjectService projectService;

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
		{
			return;
		}

		//取得session对象
		HttpSession session = request.getSession();

		//optype为指定要返回的页面；doType为指定要执行的方法
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		String doType = request.getParameter("doType");//获得具体的操作
		if (optype == null || "".equals(optype))
		{
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}

		//此类集成整个系统查询条件，所有查询条件都保存到此类中
		ReportQryStruct qryStruct = new ReportQryStruct();

		//此处解释待定
		String p_condition = request.getParameter("p_condition");
		if (StringTool.checkEmptyString(p_condition))
		{
			p_condition = ReportConsts.NO;
		}

		//此处解释待定
		try
		{
			if (ReportConsts.YES.equals(p_condition))
			{
				qryStruct = (ReportQryStruct) session
						.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
			}
			else
			{
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			}
		}
		catch (AppException ex)
		{
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
					"提取界面查询信息失败，请注意是否登陆超时！");
		}

		/**
		 * 取出列表查询条件，如果没有查询条件的页面取出值为空
		 * 如果类qryStruct中的查询条件属性为空字符串则将查询条件值赋进去
		 */
		String channleName = request.getParameter("qry_channle_name");
		String channleType = request.getParameter("qry_channle_type");
		String channleState = request.getParameter("qry_channle_state");
		String channleCode = request.getParameter("qry_channle_code");
		String createDate = request.getParameter("qry_channle_createDate");

		if (StringTool.checkEmptyString(qryStruct.channle_name)) {
			qryStruct.channle_name = channleName;
		}
		if (StringTool.checkEmptyString(qryStruct.channel_type)) {
			qryStruct.channel_type = channleType;
		}
		if (StringTool.checkEmptyString(qryStruct.channle_state)) {
			qryStruct.channle_state = channleState;
		}
		if (StringTool.checkEmptyString(qryStruct.channel_code)) {
			qryStruct.channel_code = channleCode;
		}
		if (StringTool.checkEmptyString(qryStruct.channle_createDate)) {
			qryStruct.channle_createDate = createDate;
		}

		/**
		 *加入权限控制条件-用户控制区域
		 *自己理解详细描述待定
		 */
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null)
		{
			ctlStruct = new UserCtlRegionStruct();
		}

		/**
		 * 以上为此action类必须具有的语句，详细涵义，见注释
		 * 一下为调用不同函数查出数据返回给Action类
		 */

		//实例化ChannleTypeService类
		InitServlet.init(super.config, this, "channleService");
		InitServlet.init(super.config, this, "channleTypeService");
		InitServlet.init(super.config, this, "projectService");
		InitServlet.init(super.config, this, "activityService");


		/**
		 * 查出渠道类型列表集合
		 */
		try
		{
			if(channleService != null)
			{
				ChannleInfo entity = new ChannleInfo();
				if (!StringTool.checkEmptyString(channleName)) {
					entity.setChannleName(channleName);
				}
				if (!StringTool.checkEmptyString(channleType)) {
					ChannleTypeInfo channleTypeInfo =channleTypeService.getById(Integer.parseInt(channleType));
					entity.setChannleType(channleTypeInfo);
				}
				if (!StringTool.checkEmptyString(channleState)) {
					entity.setState(Integer.parseInt(channleState));
				}
				else
				{
					entity.setState(-999);
				}
				if (!StringTool.checkEmptyString(channleCode)) {
					entity.setChannleCode(channleCode);
				}
				if (!StringTool.checkEmptyString(createDate)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					entity.setCreateDate(sdf.parse(createDate));
				}
				if("search".equals(doType))
				{

					List<ChannleInfo> list = channleService.getAll(entity, 0);

					/**
					 * 把结果数据保存到request中
					 */
					request.setAttribute("channleList", list);
				}
				else if("delect".equals(doType))
				{
					String ids = "";
					String[] arr = request.getParameterValues("checkbox");
					if(null!=arr){
						for(int i=0;i<arr.length;i++){

							int arowount = 	activityService.getCountByChannleId(Integer.parseInt(arr[i]));
							int prowount = 	projectService.getCountByChannleId(Integer.parseInt(arr[i]));
							if(prowount>0){
								request.setAttribute("delectMsg", "该策略被某个方案引用,不能直接删除！");
								}else if(arowount>0){
								request.setAttribute("delectMsg", "该策略被某个活动引用,不能直接删除！");
								}else{
									//先删除改目录下的文件
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
						 channleService.delete(ids);
					}

					//删除后重新刷新
					List<ChannleInfo> list = channleService.getAll(entity, 0);
					request.setAttribute("channleList", list);

				}
				else if("add".equals(doType))
				{
					session.setAttribute("channleInfo",null);
					ChannleInfo channleInfo = new ChannleInfo();
					Date date = new Date();
					channleInfo.setCreateDate(date);
					session.setAttribute("channleInfo", channleInfo);

				}
				else if("modify".equals(doType))
				{
					session.setAttribute("channleInfo",null);
					ChannleInfo channleInfo =null;
					String channleId = request.getParameter("channleId");
					if (!StringTool.checkEmptyString(channleId)) {
						channleInfo = channleService.getById(Integer.parseInt(channleId));
					}
					session.setAttribute("channleInfo", channleInfo);
				}
				else if("save".equals(doType))
				{
					ChannleInfo channleInfo = (ChannleInfo) session.getAttribute("channleInfo");
					if(null==channleInfo){
						channleInfo = new ChannleInfo();
					}
					String channle_name = request.getParameter("channleName");
					String channle_type_Id = request.getParameter("channleTypeId");
					String channle_code = request.getParameter("channleCode");
					//String channle_createDate = request.getParameter("channleCreateDate");
					String effectDate = request.getParameter("effectDate");
					String invaildDate = request.getParameter("invaildDate");
					String channleDesc = request.getParameter("channleDesc");
					if (!StringTool.checkEmptyString(channle_name)) {
						channleInfo.setChannleName(channle_name);
					}
					if (!StringTool.checkEmptyString(channle_type_Id)) {
						ChannleTypeInfo	channleTypeInfo =	channleTypeService.getById(Integer.parseInt(channle_type_Id));
						channleInfo.setChannleType(channleTypeInfo);
					}
					if (!StringTool.checkEmptyString(channle_code)) {
						channleInfo.setChannleCode(channle_code);
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if (!StringTool.checkEmptyString(effectDate)) {
						channleInfo.setEffectDate(sdf.parse(effectDate));
					}
					if (!StringTool.checkEmptyString(invaildDate)) {
						channleInfo.setInvaildDate(sdf.parse(invaildDate));
					}
					if (!StringTool.checkEmptyString(channleDesc)) {
						channleInfo.setChannle_desc(channleDesc);
					}
					channleInfo.setState(1);

					boolean  addResult = channleService.save(channleInfo);
					session.setAttribute("channleInfo",null);
					//保存后重新刷新
					List<ChannleInfo> list = channleService.getAll(entity, 0);
					request.setAttribute("channleList", list);
					request.setAttribute("addResult", addResult);

				}
			}
		}catch(ParseException e){
			e.getMessage();
		}
		/**
		 *将查询条件值保存到session中
		 */
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);


		/**
		 * 通过request把结果数据返回到前台
		 */
		setNextScreen(request, optype+".screen");

	}

}
