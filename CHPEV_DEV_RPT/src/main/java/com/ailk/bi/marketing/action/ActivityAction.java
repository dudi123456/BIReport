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
import com.ailk.bi.marketing.entity.ActivityGroupInfo;
import com.ailk.bi.marketing.entity.ActivityInfo;
import com.ailk.bi.marketing.entity.ActivityListInfo;
import com.ailk.bi.marketing.entity.ActivityModeInfo;
import com.ailk.bi.marketing.entity.ActivityTypeInfo;
import com.ailk.bi.marketing.entity.ChannleInfo;
import com.ailk.bi.marketing.entity.FileInfo;
import com.ailk.bi.marketing.entity.GroupInfo;
import com.ailk.bi.marketing.entity.NameListInfo;
import com.ailk.bi.marketing.entity.PassInfo;
import com.ailk.bi.marketing.entity.ProjectInfo;
import com.ailk.bi.marketing.entity.TacticInfo;
import com.ailk.bi.marketing.service.IActivityGroupService;
import com.ailk.bi.marketing.service.IActivityListService;
import com.ailk.bi.marketing.service.IActivityModeService;
import com.ailk.bi.marketing.service.IActivityService;
import com.ailk.bi.marketing.service.IActivityTypeService;
import com.ailk.bi.marketing.service.IChannleService;
import com.ailk.bi.marketing.service.IFileService;
import com.ailk.bi.marketing.service.IGroupService;
import com.ailk.bi.marketing.service.INameListService;
import com.ailk.bi.marketing.service.IPassService;
import com.ailk.bi.marketing.service.IProjectService;
import com.ailk.bi.marketing.service.ITacticService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
/**
 *实现对营销目标增删改查的控制
 * 【action控制层】活动目标控制层
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ActivityAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "activityService")
	private IActivityService activityService;
	@Resource(name = "tacticService")
	private ITacticService tacticService;
	@Resource(name = "channleService")
	private IChannleService channleService;
	@Resource(name = "projectService")
	private IProjectService projectService;
	@Resource(name = "nameListService")
	private INameListService nameListService;
	@Resource(name = "fileService")
	private IFileService fileService;
	@Resource(name = "activityGroupService")
	private IActivityGroupService activityGroupService;
	@Resource(name = "groupService")
	private IGroupService groupService;
	@Resource(name = "passService")
	private IPassService passService;
	@Resource(name = "activityModeService")
	private IActivityModeService activityModeService;
	@Resource(name = "activityTypeService")
	private IActivityTypeService activityTypeService;
	@Resource(name = "activityListService")
	private IActivityListService activityListService;

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
		//获得所有的查询条件（保存的之在save方法中）
		String activityName = request.getParameter("qry__activity_name");
		String activityType = request.getParameter("qry__activity_type");
		String activityState = request.getParameter("qry__activity_state");
		String activityClient = request.getParameter("qry__activity_client");//活动对应的客户类型
		String activityProject = request.getParameter("qry__activity_project");//活动对应的营销方案
		String decider = request.getParameter("decider");

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
		if (StringTool.checkEmptyString(qryStruct.activity_name)) {
			qryStruct.activity_name = activityName;
		}
		if (StringTool.checkEmptyString(qryStruct.activity_type)) {
			qryStruct.activity_type = activityType;
		}
		if (StringTool.checkEmptyString(qryStruct.activity_state)) {
			qryStruct.activity_state = activityState;
		}
		if (StringTool.checkEmptyString(qryStruct.activity_client)) {
			qryStruct.activity_client = activityClient;
		}
		if (StringTool.checkEmptyString(qryStruct.activity_project)) {
			qryStruct.activity_project = activityProject;
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
		InitServlet.init(super.config, this, "activityService");
		InitServlet.init(super.config, this, "projectService");
		InitServlet.init(super.config, this, "nameListService");
		InitServlet.init(super.config, this, "channleService");
		InitServlet.init(super.config, this, "tacticService");
		InitServlet.init(super.config, this, "fileService");
		InitServlet.init(super.config, this, "activityGroupService");
		InitServlet.init(super.config, this, "groupService");
		InitServlet.init(super.config, this, "passService");
		InitServlet.init(super.config, this, "activityModeService");
		InitServlet.init(super.config, this, "activityTypeService");



		if (null != activityService) {
			ActivityInfo entity = new ActivityInfo();
			if (!StringTool.checkEmptyString(activityName)) {
				entity.setActivityName(activityName);
			}
			if (!StringTool.checkEmptyString(activityState)) {
				entity.setState(Integer.parseInt(activityState));
			}else{
				entity.setState(-999);//不做查询条件
			}
			if (!StringTool.checkEmptyString(activityType)) {
				ActivityTypeInfo type= 	activityTypeService.getById(Integer.parseInt(activityType));
				entity.setActivityType(type);
			}else{
				entity.setActivityType(null);//不做查询条件
			}

			if (!StringTool.checkEmptyString(activityClient)) {
				entity.setClientType(Integer.parseInt(activityClient));
			}else{
				entity.setClientType(-999);//不做查询条件
			}
			if (!StringTool.checkEmptyString(activityProject)) {
				ProjectInfo pp = 	projectService.getById(Integer.parseInt(activityProject));
				entity.setProjectInfo(pp);
			}else{
				entity.setProjectInfo(null);//不做查询条件
			}
			if (!StringTool.checkEmptyString(decider)&&!"null".equals(decider)) {
				entity.setDecider(decider);
				request.setAttribute("decider", decider);
			}

			if ("search".equals(doType)) {
				session.setAttribute("files",null);
				List<ActivityInfo> list = null;
				String isDialog = request.getParameter("isDialog");
				if (!StringTool.checkEmptyString(isDialog)){
					 request.setAttribute("isDialog", isDialog);
					 entity.showLevel = Integer.parseInt(isDialog);
					 list = activityService.getAlldownLevel(entity, 0);
				}else{
					 list = activityService.getAll(entity, 0);
				}
				request.setAttribute("activityList", list);
			}else if("delect".equals(doType)) {
				String ids = "";
				String[] arr = request.getParameterValues("checkbox");
				if(null!=arr){
					for(int i=0;i<arr.length;i++){
						//先删除改目录下的文件
						fileService.delete(String.valueOf(arr[i]));
						ids+=arr[i]+",";
					}
				}
				char lastChar =',';
				if(ids.charAt(ids.length()-1)==lastChar){
					ids = ids.substring(0, ids.length()-1);
				}
				boolean delResult = activityService.delete(ids);
				//删除后重新刷新
				List<ActivityInfo> list = activityService.getAll(entity, 0);
				request.setAttribute("activityList", list);
				request.setAttribute("delResult", delResult);
			}else if("newActivity".equals(doType)) {
				ActivityInfo activityInfo = new ActivityInfo();
				String projectId =request.getParameter("projectId");
				if (!StringTool.checkEmptyString(projectId)) {
					ProjectInfo pinfo = new ProjectInfo();
					pinfo = projectService.getById(Integer.parseInt(projectId));
					activityInfo.setClientType(pinfo.getClientType());
					activityInfo.setChannleInfo(pinfo.getChannleInfo());
					activityInfo.setProjectInfo(pinfo);
					activityInfo.setTacticInfo(pinfo.getTacticInfo());
				}
				Date date = new Date();
				activityInfo.setCreateDate(date);
				activityInfo.setCreator(loginUser.user_id);
				session.setAttribute("ActivityInfo", activityInfo);
			}else if("add".equals(doType)) {
				session.setAttribute("groups",null);
				ActivityInfo activityInfo = new ActivityInfo();
				Date date = new Date();
				activityInfo.setCreateDate(date);
				activityInfo.setCreator(loginUser.user_id);
				session.setAttribute("ActivityInfo", activityInfo);
				//审批信息
				PassInfo passInfo = new PassInfo();
				passInfo.setCreateDate(date);
				passInfo.setCreator(loginUser.user_id);
				passInfo.setStep(1);
				passInfo.setWarnName("未指定");
				passInfo.setAdvice("暂无");
				session.setAttribute("passActivityStep1", passInfo);
			}else if("modify".equals(doType)) {
				session.setAttribute("passActivityStep1", null);
				ActivityInfo activityInfo =null;
				String activityId = request.getParameter("activityId");
				if (!StringTool.checkEmptyString(activityId)) {
					activityInfo = activityService.getById(Integer.parseInt(activityId));
				}
				List<ActivityGroupInfo> aglist = activityGroupService.getAllByActivityID(activityInfo.getActivityId());
				List<ActivityListInfo> activityList =activityListService.getAllActivityID(activityInfo.getActivityId());
				activityInfo.cont_activityIds="";
				activityInfo.cont_activityNames="";
				if(null!=activityList){
					for(int i=0;i<activityList.size();i++){
						activityInfo.cont_activityIds+=activityList.get(i).getConActivityId()+",";
						ActivityInfo aa = activityService.getById(activityList.get(i).getConActivityId());
						if(null!=aa){
							activityInfo.cont_activityNames+=aa.getActivityName()+",";
						}

					}
				}
				if (!StringTool.checkEmptyString(activityInfo.cont_activityIds)) {
					if(','==(activityInfo.cont_activityIds.charAt(activityInfo.cont_activityIds.length()-1))){
						activityInfo.cont_activityIds = activityInfo.cont_activityIds.substring(0,activityInfo.cont_activityIds.length()-1);
					}
				}
				if (!StringTool.checkEmptyString(activityInfo.cont_activityNames)) {
					if(','==(activityInfo.cont_activityNames.charAt(activityInfo.cont_activityNames.length()-1))){
						activityInfo.cont_activityNames = activityInfo.cont_activityNames.substring(0,activityInfo.cont_activityNames.length()-1);
					}
				}


				List<GroupInfo> glist = new  ArrayList<GroupInfo>();
				if(null!=aglist){
					for(int i=0;i<aglist.size();i++){
						GroupInfo info = groupService.getById(aglist.get(i).getGroupId());
						info.numCount=aglist.get(i).getNumCount();
						glist.add(info);
					}
				}
				//通过活动id获得案例对象，再去file获得文件列表
				ActivityModeInfo modeInfo = 	activityModeService.getByActivityId(activityInfo.getActivityId());
				session.setAttribute("modeFiles",null);
				session.setAttribute("modeInfo", null);
				if(null!=modeInfo){
					List<FileInfo> modeFiles =  fileService.getAllByRelastionshipID(modeInfo.getModeId());
					session.setAttribute("modeFiles",modeFiles);
					session.setAttribute("modeInfo", modeInfo);
				}
				//审批信息
				List<PassInfo> passlist =  passService.getAllByTypeId(activityInfo.getActivityId());
				session.setAttribute("activityPassList", passlist);
				session.setAttribute("groups",glist);
				List<FileInfo> editlist =  fileService.getAllByRelastionshipID(activityInfo.getActivityId());
				session.setAttribute("files",editlist);
				session.setAttribute("ActivityInfo", activityInfo);
			}else if("save".equals(doType)) {
				ActivityInfo activityInfo = (ActivityInfo) session.getAttribute("ActivityInfo");
				if(null==activityInfo){
					activityInfo = new ActivityInfo();
				}
				String name = request.getParameter("txt_activityName");
				String typeId = request.getParameter("txt_activityType");
				String cede = request.getParameter("txt_activityCode");
				String channleId = request.getParameter("txt__channle_id");
				String level = request.getParameter("txt_activityLevel");
				String priority = request.getParameter("txt__activityPriority");
				String client = request.getParameter("txt__client_type");
				String nameListType = request.getParameter("txt__nameListType");
				String date01 =  request.getParameter("txt_date01");
				String date02 =  request.getParameter("txt_date02");
				String date03 =  request.getParameter("txt_date03");
				String dispatchCyc =  request.getParameter("txt__dispatch_cyc");
				String content =  request.getParameter("txt_activityContent");
				String tactic =  request.getParameter("txt_tacticId");
				String project =  request.getParameter("txt_projectID");
				String nameListID =  request.getParameter("hid_nameListID");
				String wave =  request.getParameter("txt__wave");
				if (!StringTool.checkEmptyString(wave)) {
					activityInfo.setWave(Integer.parseInt(wave));
				}else {
					activityInfo.setWave(0);
					}
				if (!StringTool.checkEmptyString(project)) {
					ProjectInfo pp = 	projectService.getById(Integer.parseInt(project));
					activityInfo.setProjectInfo(pp);
				}else{
					activityInfo.setProjectInfo(null);
				}
				if (!StringTool.checkEmptyString(tactic)) {
					TacticInfo tt = 	tacticService.getById(Integer.parseInt(tactic));
					activityInfo.setTacticInfo(tt);
				}else{
					activityInfo.setTacticInfo(null);
				}
				if (!StringTool.checkEmptyString(channleId)) {
					ChannleInfo cc= 	channleService.getById(Integer.parseInt(channleId));
					activityInfo.setChannleInfo(cc);
				}else{
					activityInfo.setChannleInfo(null);
				}
				if (!StringTool.checkEmptyString(nameListID)) {
					NameListInfo nn= 	nameListService.getById(Integer.parseInt(nameListID));
					activityInfo.setNameListId(nn);
				}else{
					activityInfo.setNameListId(null);
				}
			 	if (!StringTool.checkEmptyString(name)) {
					activityInfo.setActivityName(name);
				}
//				if (!StringTool.checkEmptyString(typeId)) {
//					activityInfo.setActivityType(Integer.parseInt(typeId));
//				}else {
//					activityInfo.setActivityType(0);
//					}
				if (!StringTool.checkEmptyString(typeId)) {
					ActivityTypeInfo type= 	activityTypeService.getById(Integer.parseInt(typeId));
					activityInfo.setActivityType(type);
				}else{
					activityInfo.setActivityType(null);//不做查询条件
				}

				if (!StringTool.checkEmptyString(dispatchCyc)) {
					activityInfo.setDisCyc(Integer.parseInt(dispatchCyc));
				}else {
					activityInfo.setDisCyc(0);
					}
				if (!StringTool.checkEmptyString(level)) {
					activityInfo.setActivityLevel(Integer.parseInt(level));
				}else{
					activityInfo.setActivityLevel(0);
				}
				if (!StringTool.checkEmptyString(priority)) {
					activityInfo.setPriority(Integer.parseInt(priority));
				}else{
					activityInfo.setPriority(0);
				}
				if (!StringTool.checkEmptyString(content)) {
					activityInfo.setContent(content);
				}
				if (!StringTool.checkEmptyString(cede)) {
					activityInfo.setActivityCode(cede);
				}
				if (!StringTool.checkEmptyString(client)) {
					activityInfo.setClientType(Integer.parseInt(client));
				}else{
					activityInfo.setClientType(0);
				}
				if (!StringTool.checkEmptyString(nameListType)) {
					activityInfo.setNameListType(Integer.parseInt(nameListType));
				}else{
					activityInfo.setNameListType(0);
				}

				if (!StringTool.checkEmptyString(date01)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date effectDate = new Date();
					try {
						effectDate = df.parse(date01);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					activityInfo.setStartData(effectDate);
				}
				if (!StringTool.checkEmptyString(date02)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date invaildDate = new Date();
					try {
						invaildDate = df.parse(date02);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					activityInfo.setEndDate(invaildDate);
				}
				if (!StringTool.checkEmptyString(date03)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date invaildDate = new Date();
					try {
						invaildDate = df.parse(date03);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					activityInfo.setDisDat(invaildDate);
				}
				activityInfo.setState(0);//默认状态为等待审批
				boolean  addResult = activityService.save(activityInfo);
				int newid = activityInfo.getActivityId();
				fileService.delete(String.valueOf(newid));
				@SuppressWarnings("unchecked")
				List<FileInfo> files = (List<FileInfo>) session.getAttribute("files");
			 	if(null!=files){
				    for(int i =0;i<files.size();i++){
				    	FileInfo ff = files.get(i);
				    	ff.setRelationshipId(newid);
				    	fileService.save(ff);
				  	}
				}
				session.setAttribute("files",null);
			//开始保存客户名单
				activityGroupService.delete(String.valueOf(newid));
				@SuppressWarnings({"unchecked" })
				List<GroupInfo> glist = (List<GroupInfo>) session.getAttribute("groups");

				//为已有的list赋值
				String[] numCountArr =request.getParameterValues("numCount");
				if(null!=numCountArr){
					if(glist.size()==numCountArr.length){
						for(int i=0;i<numCountArr.length;i++){
							glist.get(i).numCount = Integer.parseInt(numCountArr[i]);
						}
					}
				}

			 	if(null!=glist){
				    for(int i =0;i<glist.size();i++){
				    	ActivityGroupInfo agInfo = new ActivityGroupInfo();
				    	agInfo.setActivityId(newid);
				    	GroupInfo ginfo = glist.get(i);
				    	agInfo.setGroupId(ginfo.getGroupId());
				    	agInfo.setNumCount(ginfo.numCount);
				    	int gTypeId = ginfo.getGroupTypeId();
				    	agInfo.setGroupTypeId(gTypeId);
				    	activityGroupService.save(agInfo);
				  	}
				}
				session.setAttribute("groups",null);

				//开始添加互斥活动
				activityListService.delectByActivityId(newid);
				String conActivityIds = request.getParameter("txt_conActivityIds");
				if (!StringTool.checkEmptyString(conActivityIds)) {
					String []ids = conActivityIds.split(",");
					if(null!=ids){
						for(int i=0;i<ids.length;i++){
							ActivityListInfo cont = new ActivityListInfo();
							cont.setActivityId(newid);
							cont.setConActivityId(Integer.parseInt(ids[i]));
							activityListService.save(cont);
						}
					}
				}
			//开始添加审批流程的第一步
					PassInfo passInfo = (PassInfo) session.getAttribute("passActivityStep1");
						 	if(null!=passInfo){
						 		passInfo.setTypeId(newid);
						 		passService.save(passInfo);
							}
				session.setAttribute("passActivityStep1",null);
				//保存后重新刷新
				List<ActivityInfo> list = activityService.getAll(entity, 0);
				request.setAttribute("activityList", list);
			 	request.setAttribute("addResult", String.valueOf(addResult));
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
