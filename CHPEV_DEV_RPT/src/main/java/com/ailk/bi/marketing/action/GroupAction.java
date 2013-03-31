package com.ailk.bi.marketing.action;
import java.util.ArrayList;
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
import com.ailk.bi.marketing.entity.GroupInfo;
import com.ailk.bi.marketing.service.IGroupService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
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
public class GroupAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "groupService")
	private IGroupService groupService;
	private boolean isExist(List<GroupInfo> list,String id){
	  if(null!=list){
		  for(int i=0;i<list.size();i++){
				if (!StringTool.checkEmptyString(id)) {
					if(id.equals(list.get(i).getGroupId())){
						return true;
					}
				}
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
		//InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		// 获取页面screen标示
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		String doType = request.getParameter("doType");//获得具体的操作
		String groupName = request.getParameter("qry__groupName");
		String groupType = request.getParameter("qry__groupType");
		String createType = request.getParameter("qry__createType");
		String brandType = request.getParameter("qry__brandType");
		String status = request.getParameter("qry__status");
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
		if (StringTool.checkEmptyString(qryStruct.groupName)) {
			qryStruct.groupName = groupName;
		}
		if (StringTool.checkEmptyString(qryStruct.groupType)) {
			qryStruct.groupType = groupType;
		}
		if (StringTool.checkEmptyString(qryStruct.createType)) {
			qryStruct.createType = createType;
		}
		if (StringTool.checkEmptyString(qryStruct.brandType)) {
			qryStruct.brandType = brandType;
		}
		if (StringTool.checkEmptyString(qryStruct.status)) {
			qryStruct.status = status;
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
		InitServlet.init(super.config, this, "groupService");
		if (null != groupService) {
			GroupInfo entity = new GroupInfo();
			if (!StringTool.checkEmptyString(groupName)) {
				entity.setGroupName(groupName);
			}
			if (!StringTool.checkEmptyString(status)) {
				entity.setStatus(status);
			}
			if (!StringTool.checkEmptyString(groupType)) {
				entity.setGroupTypeId(Integer.parseInt(groupType));
			}else{
				entity.setGroupTypeId(-999);
			}

			if (!StringTool.checkEmptyString(createType)) {
				entity.setCreateType(createType.charAt(0));
			}else{
				entity.setCreateType('-');
			}

			if (!StringTool.checkEmptyString(brandType)) {
				entity.setBrandOf(Integer.parseInt(brandType));
			}else{
				entity.setBrandOf(-999);
			}
			if ("search".equals(doType)) {
				entity.setDelFlag('1');
				List<GroupInfo> list = groupService.getAll(entity, 0);
				request.setAttribute("groupList", list);
			}else if("delect".equals(doType)) {
			}else if("add".equals(doType)) {
			}else if("modify".equals(doType)) {
			}else if("save".equals(doType)) {
			}else if("removeForActivity".equals(doType)) {
				@SuppressWarnings({"unchecked" })
				List<GroupInfo> list = (List<GroupInfo>) session.getAttribute("groups");
				String id = request.getParameter("removeId");
				if(null==list){
					list = new ArrayList<GroupInfo>();
				}
				//为已有的list赋值
				String[] numCountArr =request.getParameterValues("numCount");
				if(null!=numCountArr){
					if(list.size()==numCountArr.length){
						for(int i=0;i<numCountArr.length;i++){
							list.get(i).numCount = Integer.parseInt(numCountArr[i]);
						}
					}
				}
				for(int i=0;i<list.size();i++){
					if(list.get(i).getGroupId().equals(id)){
						list.remove(i);
						break;
					}
				}
				session.setAttribute("groups",list);
			}else if("addForActivity".equals(doType)) {
				@SuppressWarnings({"unchecked" })
				List<GroupInfo> list = (List<GroupInfo>) session.getAttribute("groups");
				if(null==list){
					list = new ArrayList<GroupInfo>();
				}
				String addids = request.getParameter("addids");
				//为已有的list赋值
				String[] numCountArr =request.getParameterValues("numCount");
				if(null!=numCountArr){
					if(list.size()==numCountArr.length){
						for(int i=0;i<numCountArr.length;i++){
							list.get(i).numCount = Integer.parseInt(numCountArr[i]);
						}
					}
				}
				//添加新的
				if (!StringTool.checkEmptyString(addids)) {
					String [] ids =addids.split(",");
					for(int i=0;i<ids.length;i++){
						if(!isExist(list,ids[i]))	{
							GroupInfo info = groupService.getById(ids[i]);
							info.numCount=0;
							list.add(info);
						}
					}
					session.setAttribute("groups",list);
				}
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
