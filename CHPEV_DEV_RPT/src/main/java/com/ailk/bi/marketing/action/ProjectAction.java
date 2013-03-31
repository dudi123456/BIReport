package com.ailk.bi.marketing.action;
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
import com.ailk.bi.marketing.entity.FileInfo;
import com.ailk.bi.marketing.entity.PassInfo;
import com.ailk.bi.marketing.entity.ProjectInfo;
import com.ailk.bi.marketing.entity.TacticInfo;
import com.ailk.bi.marketing.service.IActivityService;
import com.ailk.bi.marketing.service.IChannleService;
import com.ailk.bi.marketing.service.IFileService;
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
public class ProjectAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "projectService")
	private IProjectService projectService;
	@Resource(name = "tacticService")
	private ITacticService tacticService;
	@Resource(name = "channleService")
	private IChannleService channleService;
	@Resource(name = "fileService")
	private IFileService fileService;
	@Resource(name = "passService")
	private IPassService passService;
	@Resource(name = "activityService")
	private IActivityService activityService;

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
		String projectName = request.getParameter("qry__project_name");
		String projectType = request.getParameter("qry__project_type");
		String projectState = request.getParameter("qry__project_state");
		String projectLevel = request.getParameter("qry__project_level");//方案级别
		String projectPriority = request.getParameter("qry__project_Priority");//方案优先级
		String warnName = request.getParameter("warnName");
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
		if (StringTool.checkEmptyString(qryStruct.project_name)) {
			qryStruct.project_name = projectName;
		}
		if (StringTool.checkEmptyString(qryStruct.project_type)) {
			qryStruct.project_type = projectType;
		}
		if (StringTool.checkEmptyString(qryStruct.project_level)) {
			qryStruct.project_level = projectLevel;
		}
		if (StringTool.checkEmptyString(qryStruct.project_Priority)) {
			qryStruct.project_Priority = projectPriority;
		}
		if (StringTool.checkEmptyString(qryStruct.project_state)) {
			qryStruct.project_state = projectState;
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

		InitServlet.init(super.config, this, "projectService");
		InitServlet.init(super.config, this, "tacticService");
		InitServlet.init(super.config, this, "channleService");
		InitServlet.init(super.config, this, "fileService");
		InitServlet.init(super.config, this, "passService");
		InitServlet.init(super.config, this, "activityService");


		if (null != projectService) {
			ProjectInfo entity = new ProjectInfo();
			if (!StringTool.checkEmptyString(projectName)) {
				entity.setProjectName(projectName);
			}
			if (!StringTool.checkEmptyString(projectState)) {
				entity.setState(Integer.parseInt(projectState));
			}else{
				entity.setState(-999);//不做查询条件
			}

			if (!StringTool.checkEmptyString(projectType)) {
				entity.setProjectType(Integer.parseInt(projectType));
			}else{
				entity.setProjectType(-999);//不做查询条件
			}

			if (!StringTool.checkEmptyString(projectLevel)) {
				entity.setProjectLevel(Integer.parseInt(projectLevel));
			}else{
				entity.setProjectLevel(-999);//不做查询条件
			}
			if (!StringTool.checkEmptyString(projectPriority)) {
				entity.setPriority(Integer.parseInt(projectPriority));
			}else{
				entity.setPriority(-999);//不做查询条件
			}
			if (!StringTool.checkEmptyString(warnName)&&!"null".equals(warnName)) {
				entity.setWarnName(warnName);
				request.setAttribute("warnName", warnName);
			}
			entity.setClientType(-999);
			if ("search".equals(doType)) {
				session.setAttribute("files",null);
				List<ProjectInfo> list = projectService.getAll(entity, 0);
				request.setAttribute("projectList", list);
				session.setAttribute("ProjectInfoList",list);
			}else if("delect".equals(doType)) {
				String ids = "";
				String[] arr = request.getParameterValues("checkbox");
				if(null!=arr){
					for(int i=0;i<arr.length;i++){
					int prowount = 	activityService.getCountByProjectID(Integer.parseInt(arr[i]));
					 if(prowount>0){
						request.setAttribute("delectMsg", "该策略被某个活动引用,不能直接删除！");
						}else{
							//先删除改目录下的文件
							fileService.delete(String.valueOf(arr[i]));
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
					projectService.delete(ids);
				}
				//删除后重新刷新
				List<ProjectInfo> list = projectService.getAll(entity, 0);
				request.setAttribute("projectList", list);
			}else if("add".equals(doType)) {
				//将包含的 营销策略类表查出来
				TacticInfo tacticInfo = new TacticInfo();
				tacticInfo.setState(1);
				tacticInfo.setTacticType(-999);//不做查询条件
				List<TacticInfo> list = tacticService.getAll(tacticInfo, 0);
				session.setAttribute("tacticInfoList", list);
				///************************************************//
				ProjectInfo ProjectInfo = new ProjectInfo();
				Date date = new Date();
				ProjectInfo.setCreateDate(date);
				ProjectInfo.setCreator(loginUser.user_id);
				session.setAttribute("ProjectInfo", ProjectInfo);

				PassInfo passInfo = new PassInfo();
				passInfo.setCreateDate(date);
				passInfo.setCreator(loginUser.user_id);
				passInfo.setStep(1);
				passInfo.setWarnName("未指定");
				passInfo.setAdvice("暂无");
				session.setAttribute("passProjectStep1", passInfo);
			}else if("modify".equals(doType)) {
				session.setAttribute("passProjectStep1", null);
				TacticInfo tacticInfo = new TacticInfo();
				tacticInfo.setState(1);
				tacticInfo.setTacticType(-999);//不做查询条件
				List<TacticInfo> list = tacticService.getAll(tacticInfo, 0);
				request.setAttribute("tacticInfoList", list);
				///************************************************//
				ProjectInfo ProjectInfo =null;
				String projectId = request.getParameter("projectId");
				if (!StringTool.checkEmptyString(projectId)) {
					ProjectInfo = projectService.getById(Integer.parseInt(projectId));
				}
				List<FileInfo> editlist =  fileService.getAllByRelastionshipID(ProjectInfo.getProjectId());
				List<PassInfo> passlist =  passService.getAllByTypeId(ProjectInfo.getProjectId());
				session.setAttribute("files",editlist);
				session.setAttribute("projectPassList", passlist);
				session.setAttribute("ProjectInfo", ProjectInfo);
			}else if("save".equals(doType)) {
				ProjectInfo ProjectInfo = (ProjectInfo) session.getAttribute("ProjectInfo");
				if(null==ProjectInfo){
					ProjectInfo = new ProjectInfo();
				}
				String name = request.getParameter("txt_projectName");
				String typeId = request.getParameter("txt__project_type");
				String level = request.getParameter("txt__project_level");
				String priority = request.getParameter("txt__project_Priority");
				String channleId = request.getParameter("txt__channle");
				String tacticId = request.getParameter("txt_tacticId");
				String content = request.getParameter("txt_projectContent");
				String date01 =  request.getParameter("txt_date01");
				String date02 =  request.getParameter("txt_date02");
				String fileUrl="";


			 	if(!"".equals(fileUrl)&&fileUrl.length()>0&&"&".equals(fileUrl.charAt(fileUrl.length()-1)))
			 	{
			 		fileUrl = fileUrl.substring(0,fileUrl.length()-1);
			 	}
			 	if (!StringTool.checkEmptyString(name)) {
					ProjectInfo.setProjectName(name);
				}
				if (!StringTool.checkEmptyString(typeId)) {
					ProjectInfo.setProjectType(Integer.parseInt(typeId));
				}else {
					ProjectInfo.setProjectType(-999);
					}
				if (!StringTool.checkEmptyString(level)) {
					ProjectInfo.setProjectLevel(Integer.parseInt(level));
				}else{
					ProjectInfo.setProjectLevel(-999);
				}
				if (!StringTool.checkEmptyString(priority)) {
					ProjectInfo.setPriority(Integer.parseInt(priority));
				}else{
					ProjectInfo.setPriority(-999);
				}
				if (!StringTool.checkEmptyString(content)) {
					ProjectInfo.setProjectContent(content);
				}
				if (!StringTool.checkEmptyString(tacticId)) {
					TacticInfo tacticInfo = tacticService.getById(Integer.parseInt(tacticId));
					ProjectInfo.setTacticInfo(tacticInfo);
				}
				if (!StringTool.checkEmptyString(channleId)) {
					ChannleInfo channleInfo =channleService.getById(Integer.parseInt(channleId));
					ProjectInfo.setChannleInfo(channleInfo);
				}
				if (!StringTool.checkEmptyString(date01)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date effectDate = new Date();
					try {
						effectDate = df.parse(date01);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ProjectInfo.setEffectDate(effectDate);
				}
				if (!StringTool.checkEmptyString(date02)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date invaildDate = new Date();
					try {
						invaildDate = df.parse(date02);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ProjectInfo.setInvaildDate(invaildDate);
				}

				ProjectInfo.setState(0);//默认状态为等待审批
				boolean  addResult = projectService.save(ProjectInfo);
				int newid = ProjectInfo.getProjectId();

		    	///////开始添加文件
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
				///////开始添加审批流程的第一步
				PassInfo passInfo = (PassInfo) session.getAttribute("passProjectStep1");
			 	if(null!=passInfo){
			 		passInfo.setTypeId(newid);
			 		passService.save(passInfo);
				}
				session.setAttribute("passProjectStep1",null);
				//保存后重新刷新
				List<ProjectInfo> list = projectService.getAll(entity, 0);
				request.setAttribute("projectList", list);
			 	request.setAttribute("addResult", String.valueOf(addResult));
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
