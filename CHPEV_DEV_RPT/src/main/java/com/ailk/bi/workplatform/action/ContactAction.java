package com.ailk.bi.workplatform.action;
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
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.marketing.entity.ActivityInfo;
import com.ailk.bi.marketing.service.IActivityService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.workplatform.entity.ContactInfo;
import com.ailk.bi.workplatform.entity.OrderInfo;
import com.ailk.bi.workplatform.service.IContactService;
import com.ailk.bi.workplatform.service.IOrderInfoService;

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
public class ContactAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "contactService")
	private IContactService contactService;
	@Resource(name = "orderInfoService")
	private IOrderInfoService orderInfoService;
	@Resource(name = "activityService")
	private IActivityService activityService;

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
		//获得所有的查询条件（保存的之在save方法中）
		String custId = request.getParameter("custId");//获得具体的操作
		String orderId = request.getParameter("orderId");//获得具体的操作
		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}
		//以下是获得查询类表页面的查询条件//
		String qry_custName = request.getParameter("qry_custName");
		String qry_servNumber = request.getParameter("qry_servNumber");
		String qry_orderType = request.getParameter("qry_orderType");
		String qry_interviewNature= request.getParameter("qry_interviewNature");//访问性质
		String qry_interviewType = request.getParameter("qry_interviewType");//访问类型
		String qry_contactMode = request.getParameter("qry_contactMode");
		String qry_activityId = request.getParameter("qry__activityID");
		String qry_activityName = request.getParameter("txt_activityName");
		String qry_interviewState= request.getParameter("qry_interviewState");//是否接触成功
		String qry_pleasedState = request.getParameter("qry_pleasedState");//是否满意

		ContactInfo entity = new ContactInfo();
		//客户名称
		if (!StringTool.checkEmptyString(qry_custName)) {
			entity.setCust_name(qry_custName);
		}
		if (!StringTool.checkEmptyString(qry_servNumber)) {
			entity.setServ_number(qry_servNumber);
		}
		if (!StringTool.checkEmptyString(qry_orderType)) {
			entity.setOrder_type(Integer.parseInt(qry_orderType));
		}else{
			entity.setOrder_type(-999);
		}
		if (!StringTool.checkEmptyString(qry_interviewNature)) {
			entity.setInterview_nature(Integer.parseInt(qry_interviewNature));
		}else{
			entity.setInterview_nature(-999);
		}
		if (!StringTool.checkEmptyString(qry_interviewType)) {
			entity.setInterview_type(Integer.parseInt(qry_interviewType));
		}else{
			entity.setInterview_type(-999);
		}
		if (!StringTool.checkEmptyString(qry_contactMode)) {
			entity.setContactMode(Integer.parseInt(qry_contactMode));
		}else{
			entity.setContactMode(-999);
		}
		if (!StringTool.checkEmptyString(qry_activityId)) {
			entity.setActivity_id(Integer.parseInt(qry_activityId));
		}else{
			entity.setActivity_id(-999);
		}
		if (!StringTool.checkEmptyString(qry_interviewState)) {
			entity.setInterview_state(Integer.parseInt(qry_interviewState));
		}else{
			entity.setInterview_state(-999);
		}
		if (!StringTool.checkEmptyString(qry_pleasedState)) {
			entity.setPleased_state(Integer.parseInt(qry_pleasedState));
		}else{
			entity.setPleased_state(-999);
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
		if (StringTool.checkEmptyString(qryStruct.qry_custName)) {
			qryStruct.qry_custName = qry_custName;
		}
		if (StringTool.checkEmptyString(qryStruct.qry_servNumber)) {
			qryStruct.qry_servNumber = qry_servNumber;
		}
		if (StringTool.checkEmptyString(qryStruct.qry_orderType)) {
			qryStruct.qry_orderType = qry_orderType;
		}
		if (StringTool.checkEmptyString(qryStruct.qry_interviewNature)) {
			qryStruct.qry_interviewNature = qry_interviewNature;
		}
		if (StringTool.checkEmptyString(qryStruct.qry_interviewType)) {
			qryStruct.qry_interviewType = qry_interviewType;
		}
		if (StringTool.checkEmptyString(qryStruct.qry_contactMode)) {
			qryStruct.qry_contactMode = qry_contactMode;
		}
		if (StringTool.checkEmptyString(qryStruct.qry_activityId)) {
			qryStruct.qry_activityId = qry_activityId;
		}
		if (StringTool.checkEmptyString(qryStruct.qry_interviewState)) {
			qryStruct.qry_interviewState = qry_interviewState;
		}
		if (StringTool.checkEmptyString(qryStruct.qry_pleasedState)) {
			qryStruct.qry_pleasedState = qry_pleasedState;
		}
		if (StringTool.checkEmptyString(qryStruct.qry_activityName)) {
			qryStruct.qry_activityName = qry_activityName;
		}

		/**
		 * 业务开始
		 *@author f00211612
		 * */
		InitServlet.init(super.config, this, "contactService");
		InitServlet.init(super.config, this, "orderInfoService");
		InitServlet.init(super.config, this, "activityService");


		if (null != contactService) {
			if ("search".equals(doType)) {
				if (!StringTool.checkEmptyString(orderId)) {
					int custNo = Integer.parseInt(orderId);
					OrderInfo orderInfo = orderInfoService.getOneById(custNo);
					request.setAttribute("orderInfo", orderInfo);
					if (!StringTool.checkEmptyString(custId)) {
						List<ContactInfo> list = contactService.getAllByUserId(custId,custNo);
						request.setAttribute("contactList", list);
					}
				}
			}else if("searchAll".equals(doType)) {
				List<ContactInfo> list = contactService.getAll(entity, 0);
				request.setAttribute("contactList", list);
			}else if("newActivity".equals(doType)) {
			}else if("add".equals(doType)) {
			}else if("modify".equals(doType)) {
			}else if("save".equals(doType)) {
				ContactInfo into = new ContactInfo();
				String activityId = request.getParameter("txt_activityid");
				if (!StringTool.checkEmptyString(activityId)) {
					int id = Integer.parseInt(activityId);
					ActivityInfo activity = activityService.getById(id);
					if(null!=activity){
						into.setActivity_id(activity.getActivityId());
						into.setActivity_name(activity.getActivityName());
					}
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				into.setContact_date(date);
				String contactModeId = request.getParameter("txt_contactModeId");
				if (!StringTool.checkEmptyString(contactModeId)) {
					into.setContactMode(Integer.parseInt(contactModeId));
				}
				String orderType = request.getParameter("txt_orderType");
				if (!StringTool.checkEmptyString(orderType)) {
					into.setOrder_type(Integer.parseInt(orderType));
				}
				String custid = request.getParameter("txt_custid");
				if (!StringTool.checkEmptyString(custid)) {
					into.setCust_id(custid);
				}
				String custName = request.getParameter("txt_custName");
				if (!StringTool.checkEmptyString(custName)) {
					into.setCust_name(custName);
				}
				String custSex = request.getParameter("txt_cust_sex");
				if (!StringTool.checkEmptyString(custSex)) {
					into.setGender(custSex);
				}
				String number = request.getParameter("txt_number");
				if (!StringTool.checkEmptyString(number)) {
					into.setServ_number(number);
				}
				OrderInfo newOrderInfo = null;
				String neworderId = request.getParameter("txt_orderId");//工单编号
				if (!StringTool.checkEmptyString(neworderId)) {
					into.setOrder_no(Integer.parseInt(neworderId));
					newOrderInfo = orderInfoService.getOneById(Integer.parseInt(neworderId));
					into.setOrder_type(newOrderInfo.getOrder_type());
				}
				String nature = request.getParameter("txt_nature");//访问性质
				if (!StringTool.checkEmptyString(nature)) {
					into.setInterview_nature(Integer.parseInt(nature));
				}
				String ntervienType = request.getParameter("txt_ntervienType");
				if (!StringTool.checkEmptyString(ntervienType)) {
					into.setInterview_type(Integer.parseInt(ntervienType));
				}

				String reply = request.getParameter("txt_isreply");//是否访问成功
				if (!StringTool.checkEmptyString(reply)) {
					into.setInterview_state(Integer.parseInt(reply));
				}
				String noreplyReason = request.getParameter("txt_noreplyReason");//失败原因
				if (!StringTool.checkEmptyString(noreplyReason)) {
					into.setNoreply_reason(Integer.parseInt(noreplyReason));
				}
				String contact2 = request.getParameter("txt_contact2");//是否2次接触
				if (!StringTool.checkEmptyString(contact2)) {
					int c2 = Integer.parseInt(contact2);
					into.setNeed_repeat(c2);
					if(c2 !=1){
						newOrderInfo.setOrder_state(3);//3表示已结束
					}
				}
				String date02 = request.getParameter("txt_date02");
				if (!StringTool.checkEmptyString(date02)) {
					Date dd=null;
					try {
						dd = sdf.parse(date02);
						into.setNext_contact_date(dd);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(Integer.parseInt(contact2)==1){
						newOrderInfo.setNext_contact_date(dd);
						newOrderInfo.setPerform_state(2);
					}
				}

				String pleased = request.getParameter("txt_pleased");//是否满意
				if (!StringTool.checkEmptyString(pleased)) {
					into.setPleased_state(Integer.parseInt(pleased));
				}
				String interest = request.getParameter("txt_interest");//是否愿意参加活动
				if (!StringTool.checkEmptyString(interest)) {
					into.setInterest(Integer.parseInt(interest));
				}
				String isaway = request.getParameter("txt_isaway");
				if (!StringTool.checkEmptyString(isaway)) {
					into.setAway_trend(Integer.parseInt(isaway));
				}
				String awayReason = request.getParameter("txt_awayReason");
				if (!StringTool.checkEmptyString(awayReason)) {
					into.setAway_reason(Integer.parseInt(awayReason));
				}
				String content = request.getParameter("txt_content");
				if (!StringTool.checkEmptyString(content)) {
					into.setContact_content(content);
				}else{
					into.setContact_content("暂无");
				}
				contactService.save(into);
				orderInfoService.save(newOrderInfo);
				if (!StringTool.checkEmptyString(into.getCust_id())) {
					List<ContactInfo> list = contactService.getAllByUserId(into.getCust_id(),into.getOrder_no());
					request.setAttribute("contactList", list);
				}
					OrderInfo orderInfo = orderInfoService.getOneById(into.getOrder_no());
					request.setAttribute("orderInfo", orderInfo);
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
