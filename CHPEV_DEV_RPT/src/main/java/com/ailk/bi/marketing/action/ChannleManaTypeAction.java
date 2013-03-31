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
import com.ailk.bi.marketing.entity.ChannleTypeInfo;
import com.ailk.bi.marketing.service.IChannleService;
import com.ailk.bi.marketing.service.IChannleTypeService;
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
public class ChannleManaTypeAction extends HTMLActionSupport implements
		Serializable {
	/**
	 *为保证 serialVersionUID 值跨不同 java 编译器实现的一致性，
	 *序列化类必须声明一个明确的 serialVersionUID 值
	 */
	private static final long serialVersionUID = 1L;

	//注入渠道类型service类
	@Resource( name = "channleTypeService")
	private IChannleTypeService channleTypeService;
	@Resource( name = "channleService")
	private IChannleService channleService;


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
		String channleTypeName = request.getParameter("qry_channleType_name");
		String channleTypeState = request.getParameter("qry_channleType_state");
		if (StringTool.checkEmptyString(qryStruct.channleType_name)) {
			qryStruct.channleType_name = channleTypeName;
		}
		if (StringTool.checkEmptyString(qryStruct.channleType_state)) {
			qryStruct.channleType_state = channleTypeState;
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
		InitServlet.init(super.config, this, "channleTypeService");
		InitServlet.init(super.config, this, "channleService");

		/**
		 * 查出渠道类型列表集合
		 */
		try
		{
			if(channleTypeService != null)
			{
				ChannleTypeInfo entity = new ChannleTypeInfo();
				if (!StringTool.checkEmptyString(channleTypeName)) {
					entity.setChannleTypeName(channleTypeName);
				}
				if (!StringTool.checkEmptyString(channleTypeState)) {
					entity.setState(Integer.parseInt(channleTypeState));
				}
				else
				{
					entity.setState(-999);
				}
				entity.setChannleBigNumber(-999);
				if("search".equals(doType))
				{
					List<ChannleTypeInfo> list = channleTypeService.getAll(entity, 0);

					/**
					 * 把结果数据保存到request中
					 */
					request.setAttribute("channleTypeList", list);
				}
				else if("delect".equals(doType))
				{
					String ids = "";
					String[] arr = request.getParameterValues("checkbox");
					if(null!=arr){
						for(int i=0;i<arr.length;i++){
							int prowount = 	channleService.getCountByTypeId(Integer.parseInt(arr[i]));
							 if(prowount>0){
								request.setAttribute("delectMsg", "该类型被某个渠道引用,不能直接删除！");
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
					   channleTypeService.delete(ids);
					}

					//删除后重新刷新
					List<ChannleTypeInfo> list = channleTypeService.getAll(entity, 0);
					request.setAttribute("channleTypeList", list);
				}
				else if("add".equals(doType))
				{
					ChannleTypeInfo channleTypeInfo = new ChannleTypeInfo();
					session.setAttribute("channleTypeInfo", channleTypeInfo);
				}
				else if("modify".equals(doType))
				{
					ChannleTypeInfo channleTypeInfo =null;
					String channleTypeId = request.getParameter("channleTypeId");
					if (!StringTool.checkEmptyString(channleTypeId)) {
						channleTypeInfo = channleTypeService.getById(Integer.parseInt(channleTypeId));
					}
					session.setAttribute("channleTypeInfo", channleTypeInfo);
				}
				else if("save".equals(doType))
				{
					ChannleTypeInfo channleTypeInfo = (ChannleTypeInfo) session.getAttribute("channleTypeInfo");
					if(null == channleTypeInfo){
						channleTypeInfo = new ChannleTypeInfo();
					}
					session.setAttribute("channleTypeInfo",null);
					String channleType_name = request.getParameter("channleTypeName");
					String set_time = request.getParameter("setTime");
					String type_desc = request.getParameter("typeDesc");
					if (!StringTool.checkEmptyString(channleType_name)) {
						channleTypeInfo.setChannleTypeName(channleType_name);
					}
					//去掉最大可派单数--开始
					//String channle_big_number = request.getParameter("channleBigNumber");
					//if (!StringTool.checkEmptyString(channle_big_number)) {
					//	channleTypeInfo.setChannleBigNumber(Integer.parseInt(channle_big_number));
					//}
					//去掉最大可派单数--结束
					if (!StringTool.checkEmptyString(set_time)) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						channleTypeInfo.setSetTime(sdf.parse(set_time));
					}
					if (!StringTool.checkEmptyString(type_desc)) {
						channleTypeInfo.setTypeDesc(type_desc);
					}
					channleTypeInfo.setState(1);

					boolean  addResult = channleTypeService.save(channleTypeInfo);
					//保存后重新刷新
					List<ChannleTypeInfo> list = channleTypeService.getAll(entity, 0);
					request.setAttribute("channleTypeList", list);
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
