package com.ailk.bi.marketing.action;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.common.InitServlet;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.marketing.entity.ChannleInfo;
import com.ailk.bi.marketing.entity.PackageInfo;
import com.ailk.bi.marketing.service.IChannleService;
import com.ailk.bi.marketing.service.IPackageService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionSupport;
import waf.controller.web.action.HTMLActionException;

public class PackageManaAction extends HTMLActionSupport implements
		Serializable {

	/**
	 *为保证 serialVersionUID 值跨不同 java 编译器实现的一致性
	 *序列化类必须声明一个明确的 serialVersionUID 值
	 */
	private static final long serialVersionUID = 1L;
	//注入套餐类型service类
	@Resource( name = "packageService")
	private IPackageService packageService;
	@Resource(name = "channleService")
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
		//取到用户登录信息
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);

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
		String package_name = request.getParameter("qry_package_name");
		String package_type = request.getParameter("qry_package_type");
		String package_state = request.getParameter("qry_package_state");
		if (StringTool.checkEmptyString(qryStruct.package_name)) {
			qryStruct.package_name = package_name;
		}
		if (StringTool.checkEmptyString(qryStruct.package_type)) {
			qryStruct.package_type = package_type;
		}
		if (StringTool.checkEmptyString(qryStruct.package_state)) {
			qryStruct.package_state = package_state;
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
		InitServlet.init(super.config, this, "packageService");

		/**
		 * 查出渠道类型列表集合
		 */
		try
		{
			if(packageService != null)
			{
				PackageInfo entity = new PackageInfo();
				if (!StringTool.checkEmptyString(package_name)) {
					entity.setPackageName(package_name);
				}
				if (!StringTool.checkEmptyString(package_type)) {
					entity.setPackageType(Integer.parseInt(package_type));
				}else{
					entity.setPackageType(-999);
				}
				if (!StringTool.checkEmptyString(package_state)) {
					entity.setState(Integer.parseInt(package_state));
				}else{
					entity.setState(-999);
				}

				if("search".equals(doType))
				{

					List<PackageInfo> list = packageService.getAll(entity, 0);

					/**
					 * 把结果数据保存到request中
					 */
					request.setAttribute("packageList", list);
				}
				/*if("searchParent".equals(doType))
				{

					List<PackageParentInfo> list = packageService.getParentAll(entity, 0);

					*//**
					 * 把结果数据保存到request中
					 *//*
					request.setAttribute("packageParentList", list);
				}*/
				else if("delect".equals(doType))
				{
					String ids = "";
					String[] arr = request.getParameterValues("checkbox");
					if(null!=arr){
						for(int i=0;i<arr.length;i++){
							ids+=arr[i]+",";
						}
					}
					char lastChar =',';
					if(ids.charAt(ids.length()-1)==lastChar)
					{
						ids = ids.substring(0, ids.length()-1);
					}
					//判断此删除记录是否有子记录
					String delResultMsg = null;
					boolean delResult = false;
					List<PackageInfo> sonList = packageService.getAllSonPackage(ids);
					if(sonList.size()>0){
						delResultMsg = "删除失败，此套餐下有子套餐，不能删除。";
					}else{
						delResult = packageService.delete(ids);
					}
					//删除后重新刷新
					List<PackageInfo> list = packageService.getAll(entity, 0);
					request.setAttribute("packageList", list);
					request.setAttribute("delResult", delResult);
					request.setAttribute("delFailResultMsg", delResultMsg);
				}
				else if("add".equals(doType))
				{
					PackageInfo packageInfo = new PackageInfo();
					Date date = new Date();
					packageInfo.setCreateDate(date);
					packageInfo.setCreator(loginUser.user_id);
					session.setAttribute("packageInfo", packageInfo);

				}
				else if("modify".equals(doType))
				{
					PackageInfo packageInfo =null;
					PackageInfo parentPackageInfo =null;
					String packageId = request.getParameter("packageId");
					if (!StringTool.checkEmptyString(packageId)) {
						packageInfo = packageService.getById(Integer.parseInt(packageId));
						parentPackageInfo = packageService.getById(packageInfo.getParentPackageId());
					}
					session.setAttribute("packageInfo", packageInfo);
					session.setAttribute("parentPackageInfo", parentPackageInfo);
				}
				else if("save".equals(doType))
				{
					PackageInfo packageInfo = (PackageInfo) session.getAttribute("packageInfo");
					String packageName = request.getParameter("packageName");
					String packageType = request.getParameter("packageType");
					String parentPackageId = request.getParameter("parentPackageId");//父节点ID
					String productType = request.getParameter("productType");
					String makePriceMode = request.getParameter("makePriceMode");
					String languageType = request.getParameter("languageType");
					String clientGroup = request.getParameter("clientGroup");
					String freeProject = request.getParameter("freeProject");
					String effectDate = request.getParameter("effectDate");
					String invalidDate = request.getParameter("invalidDate");
					String packgeDesc = request.getParameter("packgeDesc");
					String channelId = request.getParameter("channelId");
					if (!StringTool.checkEmptyString(packageName)) {
						packageInfo.setPackageName(packageName);
					}
					if (!StringTool.checkEmptyString(packageType)) {
						packageInfo.setPackageType(Integer.parseInt(packageType));
					}
					if (!StringTool.checkEmptyString(parentPackageId)) {
						//PackageInfo	parentPackage =	packageService.getById(Integer.parseInt(parentPackageId));
						//parentPackage.setParentPackage(parentPackage);
						packageInfo.setParentPackageId(Integer.parseInt(parentPackageId));
					}
					if (!StringTool.checkEmptyString(productType)) {
						packageInfo.setProductType(Integer.parseInt(productType));
					}
					if (!StringTool.checkEmptyString(makePriceMode)) {
						packageInfo.setPriceMode(Integer.parseInt(makePriceMode));
					}
					if (!StringTool.checkEmptyString(languageType)) {
						packageInfo.setVoiceType(Integer.parseInt(languageType));
					}

					if (!StringTool.checkEmptyString(clientGroup)) {
						packageInfo.setCustGroup(Integer.parseInt(clientGroup));
					}
					if (!StringTool.checkEmptyString(freeProject)) {
						packageInfo.setFreeProject(freeProject);
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					if (!StringTool.checkEmptyString(effectDate)) {
						packageInfo.setEffectDate(sdf.parse(effectDate));
					}
					if (!StringTool.checkEmptyString(invalidDate)) {
						packageInfo.setInvaildDate(sdf.parse(invalidDate));
					}
					if (!StringTool.checkEmptyString(packgeDesc)) {
						packageInfo.setPackageContent(packgeDesc);
					}
					if (!StringTool.checkEmptyString(channelId)) {
						ChannleInfo channleInfo =channleService.getById(Integer.parseInt(channelId));
						packageInfo.setChannleInfo(channleInfo);
					}
					boolean  addResult = packageService.save(packageInfo);
					//保存后重新刷新
					List<PackageInfo> list = packageService.getAll(entity, 0);
					request.setAttribute("packageList", list);
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
