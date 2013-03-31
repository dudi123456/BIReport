package com.ailk.bi.marketing.action;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.ailk.bi.marketing.entity.ContactPlyInfo;
import com.ailk.bi.marketing.entity.PassInfo;
import com.ailk.bi.marketing.entity.TacticInfo;
import com.ailk.bi.marketing.entity.TargetInfo;
import com.ailk.bi.marketing.entity.TargetOpInfo;
import com.ailk.bi.marketing.service.IActivityService;
import com.ailk.bi.marketing.service.IContactPlyService;
import com.ailk.bi.marketing.service.IPassService;
import com.ailk.bi.marketing.service.IProjectService;
import com.ailk.bi.marketing.service.ITacticService;
import com.ailk.bi.marketing.service.ITargetOpService;
import com.ailk.bi.marketing.service.ITargetService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;

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
public class TacticAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "tacticService")
	private ITacticService tacticService;
	@Resource(name = "targetService")
	private ITargetService targetService;
	@Resource(name = "targetOpService")
	private ITargetOpService targetOpService;
	@Resource(name = "contactPlyService")
	private IContactPlyService contactPlyService;
	@Resource(name = "passService")
	private IPassService passService;
	@Resource(name = "projectService")
	private IProjectService projectService;
	@Resource(name = "activityService")
	private IActivityService activityService;

	public boolean isExist(List<TargetOpInfo>arrList1 ,int tacticId ){
		for (int j=0;j<arrList1.size();j++){
			if(arrList1.get(j).getTargetInfo().getTargetId()==tacticId){
				return true;
			}
		}
		return false;
	}
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		// 获取页面screen标示
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		String doType = request.getParameter("doType");//获得具体的操作
		String tacticNmae = request.getParameter("qry__tactic_name");
		String tacticType = request.getParameter("qry__tactic_type");
		String tacticState = request.getParameter("qry__tactic_state");
		String tacticCreator = request.getParameter("qry__tactic_creator");
		String decider = request.getParameter("decider");
		String step = request.getParameter("step");
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
		if (StringTool.checkEmptyString(qryStruct.target_name)) {
			qryStruct.tactic_name = tacticNmae;
		}
		if (StringTool.checkEmptyString(qryStruct.target_state)) {
			qryStruct.tactic_state = tacticState;
		}
		if (StringTool.checkEmptyString(qryStruct.target_type)) {
			qryStruct.tactic_type = tacticType;
		}
		if (StringTool.checkEmptyString(qryStruct.tactic_creator)) {
			qryStruct.tactic_creator = tacticCreator;
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
		InitServlet.init(super.config, this, "tacticService");
		InitServlet.init(super.config, this, "targetService");
		InitServlet.init(super.config, this, "targetOpService");
		InitServlet.init(super.config, this, "contactPlyService");
		InitServlet.init(super.config, this, "projectService");
		InitServlet.init(super.config, this, "activityService");


		if (null != tacticService) {
			TacticInfo entity = new TacticInfo();
			if (!StringTool.checkEmptyString(tacticNmae)) {
				entity.setTacticName(tacticNmae);
			}
			if (!StringTool.checkEmptyString(tacticCreator)) {
				entity.setTacticName(tacticCreator);
			}
			if (!StringTool.checkEmptyString(tacticNmae)) {
				entity.setTacticName(tacticNmae);
			}
			if (!StringTool.checkEmptyString(tacticType)) {
				entity.setTacticType(Integer.parseInt(tacticType));
			}else{
				entity.setTacticType(-999);//不做查询条件
			}
			if (!StringTool.checkEmptyString(tacticState)) {
				entity.setState(Integer.parseInt(tacticState));
			}else{
				entity.setState(-999);
			}

			if (!StringTool.checkEmptyString(decider)&&!"null".equals(decider)) {
				entity.setDecider(decider);
				request.setAttribute("decider", decider);
			}

			if ("search".equals(doType)) {
				session.setAttribute("isModify", "false");
					session.setAttribute("TacticInfo",null);//清除第一步
					session.setAttribute("TargetOpInfoLis1",null);//清除第2步
					session.setAttribute("ContactPlyInfoList",null);;//清除第3步
				List<TacticInfo> list = tacticService.getAll(entity, 0);
				request.setAttribute("tacticList", list);
			}else if("delect".equals(doType)) {
				session.setAttribute("isModify", "false");
				request.setAttribute("delectMsg",null);
				String ids = "";
				String[] arr = request.getParameterValues("checkbox");
				if(null!=arr){
					for(int i=0;i<arr.length;i++){
						int prowount = 	projectService.getByTacticId(Integer.parseInt(arr[i]));
						int arowount = 	activityService.getCountByTacticID(Integer.parseInt(arr[i]));
						if(prowount>0){
								request.setAttribute("delectMsg", "该策略被某个方案引用,不能直接删除！");
						}else if(arowount>0){
							request.setAttribute("delectMsg", "该策略被某个活动引用,不能直接删除！");
						}else{
							targetOpService.deleteByTacticID(Integer.parseInt(arr[i]));//删除指标中的引用
							contactPlyService.deleteByTacticID(Integer.parseInt(arr[i]));//删除规则中的应用
							ids+=arr[i]+",";
						}
					}
				}
				if(""!=ids){
					char lastChar =',';
					if(ids.charAt(ids.length()-1)==lastChar){
						ids = ids.substring(0, ids.length()-1);
					}
					tacticService.delete(ids);
				}
				//删除后重新刷新
				List<TacticInfo> list = tacticService.getAll(entity, 0);
				request.setAttribute("tacticList", list);
			}else if("add".equals(doType)) {

				if("step1".equals(step)){
					TacticInfo info  =	(TacticInfo)session.getAttribute("TacticInfo");
					if(null ==info){
						 info = new TacticInfo();
					}
					info.setCreator(loginUser.user_id);
					Date date = new Date();
					info.setCreateDate(date);
					String name =request.getParameter("txt_tacticName");
					String type =request.getParameter("txt_tactic_type");
					String date01 =request.getParameter("date01");
					String date02 =request.getParameter("date02");
					String dispatch =request.getParameter("txt_tactic_dispatch");
					String content =request.getParameter("txt_content");
					if (!StringTool.checkEmptyString(name)) {
						info.setTacticName(name);
					}
					if (!StringTool.checkEmptyString(content)) {
						info.setContent(content);
					}
					if (!StringTool.checkEmptyString(type)) {
						info.setTacticType(Integer.parseInt(type));
					}
					if (!StringTool.checkEmptyString(dispatch)) {
						info.setLeashCyc(Integer.parseInt(dispatch));
					}
					if (!StringTool.checkEmptyString(date01)) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Date effectDate = new Date();
						try {
							effectDate = df.parse(date01);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						info.setStartDate(effectDate);
					}
					if (!StringTool.checkEmptyString(date02)) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						Date effectDate = new Date();
						try {
							effectDate = df.parse(date02);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						info.setEndDate(effectDate);
					}
					session.setAttribute("TacticInfo", info);


				}else if("step2Delete".equals(step)){
					@SuppressWarnings("unchecked")
					List<TargetOpInfo> arrList1  =	(List<TargetOpInfo>)session.getAttribute("TargetOpInfoLis1");
					if(null==arrList1){
						arrList1 = new ArrayList<TargetOpInfo>();
					}
					List<TargetOpInfo> delList = new ArrayList<TargetOpInfo>();
					String[] deleteIds = request.getParameterValues("yixuan");
					if(null!=deleteIds){
						 for(int i=0;i<deleteIds.length;i++){
							 TargetOpInfo ff= arrList1.get(Integer.parseInt(deleteIds[i]));
							 delList.add(ff);
						 }
					}
					for(int i=0;i<delList.size();i++){
						arrList1.remove(delList.get(i));
					}
					session.setAttribute("TargetOpInfoLis1",arrList1);
				}else	if("step2".equals(step)){
					@SuppressWarnings("unchecked")
					List<TargetOpInfo> arrList1  =	(List<TargetOpInfo>)session.getAttribute("TargetOpInfoLis1");
					if(null==arrList1){
						arrList1 = new ArrayList<TargetOpInfo>();
					}
					String[] opTypes = request.getParameterValues("txt_opType");
					String[] opValues = request.getParameterValues("txt_opValue");

					String ids = request.getParameter("ids");
					String[] arrList2=request.getParameterValues("");
					if (!StringTool.checkEmptyString(ids)) {
						 arrList2 = ids.split(",");
					}
					if(null!=opTypes){
						for(int i =0;i<opTypes.length;i++){
							TargetInfo tInfo =arrList1.get(i).getTargetInfo();
							if(null!=tInfo){
								TargetOpInfo topInfo = new TargetOpInfo();
								topInfo.setOpType(Integer.valueOf(opTypes[i]));
								topInfo.setOpValue(Integer.valueOf(opValues[i]));
								topInfo.setTargetInfo(tInfo);
								arrList1.remove(i);
								arrList1.add(i, topInfo);
							}
					}
					}

					 if(null!=arrList2){
							for(int i =0;i<arrList2.length;i++){
								TargetInfo tInfo = 	targetService.getById(Integer.parseInt(arrList2[i]));
								if(null!=tInfo){
								if(!isExist(arrList1,tInfo.getTargetId())){
									TargetOpInfo topInfo = new TargetOpInfo();
									topInfo.setTargetInfo(tInfo);
									arrList1.add(topInfo);
								}

								}
						}
					}
					session.setAttribute("TargetOpInfoLis1",arrList1);
				}else	if("step3Delete".equals(step)){
					@SuppressWarnings("unchecked")
					List<ContactPlyInfo> contacList  =	(List<ContactPlyInfo>)session.getAttribute("ContactPlyInfoList");
					if(null==contacList){
						contacList = new ArrayList<ContactPlyInfo>();
					}
					List<ContactPlyInfo> delList = new ArrayList<ContactPlyInfo>();
					String[] deleteIds = request.getParameterValues("yixuan");
					if(null!=deleteIds){
						 for(int i=0;i<deleteIds.length;i++){
							 ContactPlyInfo ff= contacList.get(Integer.parseInt(deleteIds[i]));
							 delList.add(ff);
						 }
					}
					for(int i=0;i<delList.size();i++){
						contacList.remove(delList.get(i));
					}
					session.setAttribute("ContactPlyInfoList",contacList);
				}else	if("step3".equals(step)){
					@SuppressWarnings("unchecked")
					List<ContactPlyInfo> contacList  =	(List<ContactPlyInfo>)session.getAttribute("ContactPlyInfoList");
					if(null==contacList){
						contacList = new ArrayList<ContactPlyInfo>();
					}
					String[] opScriptIDs = request.getParameterValues("hiddenId");
					String[] opModes = request.getParameterValues("modeId");
					String[] opDates = request.getParameterValues("txt_opDate");
					String[] opDays = request.getParameterValues("txt_opDay");
					if(null!=opModes){
						for(int i =0;i<opModes.length;i++){
							ContactPlyInfo cInfo =contacList.get(i);
								if(null!=cInfo){
									cInfo.setMode(Integer.parseInt(opModes[i]));
									cInfo.setContactTime(Integer.parseInt(opDates[i]));
									cInfo.setContactDay(Integer.parseInt(opDays[i]));
									cInfo.setScriptId(Integer.parseInt(opScriptIDs[i]));
									contacList.remove(i);
									contacList.add(i, cInfo);
								}
						}
					}
					String addOne = request.getParameter("addOne");
					if ("one".equals(addOne)) {
						ContactPlyInfo cInfo =new ContactPlyInfo();
						cInfo.setMode(0);
						cInfo.setContactTime(0);
						cInfo.setContactDay(0);
						cInfo.setMode(2);
						contacList.add(contacList.size(), cInfo);
					}

					session.setAttribute("ContactPlyInfoList",contacList);
				}
			}else if("modify".equals(doType)) {
				session.setAttribute("isModify", "true");
			//	session.setAttribute("passTacticStep1", null);
				TacticInfo TacticInfo =null;
				String targetId = request.getParameter("tactictId");
				if (!StringTool.checkEmptyString(targetId)) {
					TacticInfo = tacticService.getById(Integer.parseInt(targetId));
					List<TargetOpInfo> arrList1 =targetOpService.getAllByTacticID(TacticInfo.getTacticId());
					List<ContactPlyInfo> contacList =contactPlyService.getAllByTacticID(TacticInfo.getTacticId());
					session.setAttribute("TargetOpInfoLis1",arrList1);
					session.setAttribute("ContactPlyInfoList",contacList);
					//审批信息
					List<PassInfo> passlist =  passService.getAllByTypeId(TacticInfo.getTacticId());
					session.setAttribute("tacticPassList", passlist);
				}
				session.setAttribute("TacticInfo", TacticInfo);
			}else if("save".equals(doType)) {
				int newid =0;
				TacticInfo info  =	(TacticInfo)session.getAttribute("TacticInfo");
				if(null!=info){
					//开始保存第1步
					tacticService.save(info);
					 newid = info.getTacticId();
					//开始保存第2步
					@SuppressWarnings("unchecked")
					List<TargetOpInfo> arrList1  =	(List<TargetOpInfo>)session.getAttribute("TargetOpInfoLis1");
					if(null!=arrList1){
						targetOpService.deleteByTacticID(newid);
						for(int i=0;i<arrList1.size();i++){
							TargetOpInfo newOpInfo = arrList1.get(i);
							newOpInfo.setTacticId(newid);
							targetOpService.save(newOpInfo);
						}
					}

					//开始保存第3步
					@SuppressWarnings("unchecked")
					List<ContactPlyInfo> contacList  =	(List<ContactPlyInfo>)session.getAttribute("ContactPlyInfoList");
					if(null!=contacList){
						contactPlyService.deleteByTacticID(newid);
						for(int i=0;i<contacList.size();i++){
							ContactPlyInfo contactPlyInfo = contacList.get(i);
							contactPlyInfo.setTacticId(newid);
							contactPlyInfo.setStep(i+1);
							contactPlyService.getAllByTacticID(newid);
							contactPlyService.save(contactPlyInfo);
						}
					}
				}
				//开始添加审批流程的第一步
				//审批信息
				String isModify =(String) session.getAttribute("isModify");
				if(!"true".equals(isModify)){
					Date date = new Date();
					PassInfo passInfo = new PassInfo();
					passInfo.setCreateDate(date);
					passInfo.setCreator(loginUser.user_id);
					passInfo.setStep(1);
					passInfo.setWarnName("未指定");
					passInfo.setAdvice("暂无");
					passInfo.setTypeId(newid);
			 		passService.save(passInfo);
					//session.setAttribute("passTacticStep1", passInfo);
				}
				session.setAttribute("isModify", "false");
//				PassInfo passInfo = (PassInfo) session.getAttribute("passTacticStep1");
//					 	if(null!=passInfo){
//					 		passInfo.setTypeId(newid);
//					 		passService.save(passInfo);
//						}
				//session.setAttribute("passTacticStep1",null);
				//保存后重新刷新

				session.setAttribute("TacticInfo",null);//清除第一步
				session.setAttribute("TargetOpInfoLis1",null);//清除第2步
				session.setAttribute("ContactPlyInfoList",null);;//清除第3步
			List<TacticInfo> list = tacticService.getAll(null, 0);
			request.setAttribute("tacticList", list);
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
