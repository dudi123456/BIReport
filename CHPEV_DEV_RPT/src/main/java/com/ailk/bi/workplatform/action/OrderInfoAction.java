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
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.marketing.entity.ActivityInfo;
import com.ailk.bi.marketing.entity.ChannleInfo;
import com.ailk.bi.marketing.entity.ContactPlyInfo;
import com.ailk.bi.marketing.entity.TacticInfo;
import com.ailk.bi.marketing.entity.TargetOpInfo;
import com.ailk.bi.marketing.service.IActivityService;
import com.ailk.bi.marketing.service.IChannleService;
import com.ailk.bi.marketing.service.IContactPlyService;
import com.ailk.bi.marketing.service.ITargetOpService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.ailk.bi.workplatform.entity.ContactInfo;
import com.ailk.bi.workplatform.entity.OrderAdjustInfo;
import com.ailk.bi.workplatform.entity.OrderInfo;
import com.ailk.bi.workplatform.entity.TransInfo;
import com.ailk.bi.workplatform.entity.TransListInfo;
import com.ailk.bi.workplatform.entity.UserInfo;
import com.ailk.bi.workplatform.service.IAdjustService;
import com.ailk.bi.workplatform.service.IContactService;
import com.ailk.bi.workplatform.service.IOrderInfoService;
import com.ailk.bi.workplatform.service.ITransListService;
import com.ailk.bi.workplatform.service.ITransService;
import com.ailk.bi.workplatform.service.IUserService;

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
public class OrderInfoAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "orderInfoService")
	private IOrderInfoService orderInfoService;
	@Resource(name = "activityService")
	private IActivityService activityService;
	@Resource(name = "targetOpService")
	private ITargetOpService targetOpService;
	@Resource(name = "contactPlyService")
	private IContactPlyService contactPlyService;
	@Resource(name = "contactService")
	private IContactService contactService;
	@Resource(name = "userService")
	private IUserService userService;
	@Resource(name = "adjustService")
	private IAdjustService adjustService;
	@Resource(name = "channleService")
	private IChannleService channleService;
	@Resource(name = "transService")
	private ITransService transService;
	@Resource(name = "transListService")
	private ITransListService transListService;

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
		String setType = request.getParameter("setType");
		//获得所有的查询条件（保存的之在save方法中）
		String custName = request.getParameter("qry__order_custName");
		String servNumber = request.getParameter("qry__order_servNumber");
		String orderState = request.getParameter("qry__order_state");
		String myOrderNo = request.getParameter("qry__order_no");//
		String orderCusSvcMgr = request.getParameter("qry__order_cusSvcMgr");//
		String orderlevel = request.getParameter("qry__order_level");
		String activityId = request.getParameter("qry__activityID");
		String activityName = request.getParameter("txt_activityName");
		String date1 = request.getParameter("txt_date01");
		String date2 = request.getParameter("txt_date02");
		String contact2 = request.getParameter("txt_contact2");
		String date3 = request.getParameter("txt_date03");
		String date4 = request.getParameter("txt_date04");
		String outChannle = request.getParameter("outChannle");

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
		if (StringTool.checkEmptyString(qryStruct.custName)) {
			qryStruct.custName = custName;
		}
		if (StringTool.checkEmptyString(qryStruct.outChannle)) {
			qryStruct.outChannle = outChannle;
		}

		if (StringTool.checkEmptyString(qryStruct.servNumber)) {
			qryStruct.servNumber = servNumber;
		}
		if (StringTool.checkEmptyString(qryStruct.orderState)) {
			qryStruct.orderState = orderState;
		}
		if (StringTool.checkEmptyString(qryStruct.setType)) {
			qryStruct.setType = setType;
		}
		if (StringTool.checkEmptyString(qryStruct.orderCusSvcMgr)) {
			qryStruct.orderCusSvcMgr = orderCusSvcMgr;
		}
		if (StringTool.checkEmptyString(qryStruct.orderlevel)) {
			qryStruct.orderlevel = orderlevel;
		}
		if (StringTool.checkEmptyString(qryStruct.activityId)) {
			qryStruct.activityId = activityId;
		}
		if (StringTool.checkEmptyString(qryStruct.activityName)) {
			qryStruct.activityName = activityName;
		}

		if (StringTool.checkEmptyString(qryStruct.date1)) {
			qryStruct.date1 = date1;
		}

		if (StringTool.checkEmptyString(qryStruct.date2)) {
			qryStruct.date2 = date2;
		}
		if (StringTool.checkEmptyString(qryStruct.date3)) {
			qryStruct.date3 = date3;
		}
		if (StringTool.checkEmptyString(qryStruct.date4)) {
			qryStruct.date4 = date4;
		}
		if (StringTool.checkEmptyString(qryStruct.contact2)) {
			qryStruct.contact2 = contact2;
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
		InitServlet.init(super.config, this, "orderInfoService");
		InitServlet.init(super.config, this, "targetOpService");
		InitServlet.init(super.config, this, "contactPlyService");
		InitServlet.init(super.config, this, "contactService");
		InitServlet.init(super.config, this, "userService");
		InitServlet.init(super.config, this, "adjustService");
		InitServlet.init(super.config, this, "channleService");
		InitServlet.init(super.config, this, "transService");
		InitServlet.init(super.config, this, "transListService");



		//从工单修改页面跳转过来
		String fromUpdate = request.getParameter("fromUpdate");
		if (!StringTool.checkEmptyString(fromUpdate)) {
			if("true".equals(fromUpdate)){
				String transName = request.getParameter("txt_transName");
				 String custType =request.getParameter("txt_custType");
				 String channleId = request.getParameter("txt_channleId");
				 String content = request.getParameter("txt_content");
				 TransInfo	transInfo = (TransInfo)session.getAttribute("transInfo");
				 session.setAttribute("transInfo", null);
				 if (!StringTool.checkEmptyString(transName)) {
						transInfo.setTransName(transName);
					}
					if (!StringTool.checkEmptyString(content)) {
						transInfo.setTransUse(content);
					}
					if (!StringTool.checkEmptyString(custType)) {
						transInfo.setCustType(Integer.parseInt(custType));
					}
					if (!StringTool.checkEmptyString(channleId)) {
						ChannleInfo channle =channleService.getById(Integer.parseInt(channleId));
						transInfo.setNewChannle(channle);
					}
				session.setAttribute("transInfo", transInfo);
			}
		}





		if (null != orderInfoService) {
			OrderInfo entity = new OrderInfo();
			if (!StringTool.checkEmptyString(custName)) {
				entity.setCust_name(custName);
			}
			if (!StringTool.checkEmptyString(outChannle)) {
				entity.setOuter_channel(Integer.parseInt(outChannle));
			}
			if (!StringTool.checkEmptyString(servNumber)) {
				entity.setServ_number(servNumber);
			}
			if (!StringTool.checkEmptyString(orderState)) {
				entity.setOrder_state(Integer.parseInt(orderState));
			}else{
				entity.setOrder_state(-999);//不做查询条件
			}
			if (!StringTool.checkEmptyString(myOrderNo)) {
				entity.setOrder_no(Integer.parseInt(myOrderNo));
			}else{
				entity.setOrder_no(-999);
			}
			if (!StringTool.checkEmptyString(setType)) {
				entity.setType = setType;
			}
			if (!StringTool.checkEmptyString(orderCusSvcMgr)) {
				entity.setCust_svc_mgr_id(orderCusSvcMgr);
			}

			if (!StringTool.checkEmptyString(orderlevel)) {
				entity.setCust_level(orderlevel);
			}

			if (!StringTool.checkEmptyString(activityId)) {
				ActivityInfo activity = activityService.getById(Integer.parseInt(activityId));
				entity.setActivityInfo(activity);
			}
			//是否再次接触
			if (!StringTool.checkEmptyString(contact2)) {
				entity.setPerform_state(Integer.parseInt(contact2));
			}else
			{
				entity.setPerform_state(-999);
			}

			SimpleDateFormat   formatter   =   new   SimpleDateFormat   ( "yyyy-MM-dd");
			Date dt1 = null;
			Date date = new Date();
			if (!StringTool.checkEmptyString(date1)) {

				try {
					dt1 = formatter.parse(date1);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				entity.date01=dt1;
			}else{
				entity.date01=null;
			}
			if (!StringTool.checkEmptyString(date2)) {
				Date dt2 = date;
				try {
					dt2 = formatter.parse(date2);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				entity.date02=dt2;
			}else{
				entity.date02=null;
			}

			if (!StringTool.checkEmptyString(date3)) {
				Date dt3 = null;
				try {
					dt3 = formatter.parse(date3);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				entity.date03=dt3;
			}else{
				entity.date03=null;
			}

			if (!StringTool.checkEmptyString(date4)) {
				Date dt4 = null;
				try {
					dt4 = formatter.parse(date4);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				entity.date04=dt4;
			}else{
				entity.date04=null;
			}
			//判断是不是普通会员（1：普通会员；2：渠道经理）
			UserInfo loginUU = userService.getById(loginUser.user_id);
			if("1".equals(loginUser.duty_id)){
				entity.setPerformer_id(loginUU);
			}else	if("2".equals(loginUser.duty_id)){
				entity.setPerformer_id(null);
				entity.channleAdmin=true;
				entity.setChannelInfo(loginUU.getChannleInfo());
			}

			if ("search".equals(doType)) {
				if (!StringTool.checkEmptyString(setType)) {
					if(setType.equals("2")){
						entity.setPerformer_id(null);
					}
				}
				List<OrderInfo> list = orderInfoService.getAllListBySearch(entity, 0);
				request.setAttribute("orderList", list);
			}else if("searchOne".equals(doType)) {
				String orderId = request.getParameter("orderId");
				String custId = request.getParameter("custId");
				if (!StringTool.checkEmptyString(orderId)) {
					int orderNo = Integer.parseInt(orderId);
					OrderInfo orderInfo = orderInfoService.getOneById(orderNo);
					TacticInfo	tacticInfo = orderInfo.getTacticInfo();
					List<TargetOpInfo> arrList1 =targetOpService.getAllByTacticID(tacticInfo.getTacticId());
					List<ContactPlyInfo> contacList =contactPlyService.getAllByTacticID(tacticInfo.getTacticId());
					entity.setCust_id(custId);
					entity.setCreatedate(null);
					entity.setNext_contact_date(null);
					List<OrderInfo> orderlistCust = orderInfoService.getAllListBySearch(entity, 0);
					request.setAttribute("orderlistCust", orderlistCust);
					request.setAttribute("orderInfo",orderInfo);
					request.setAttribute("TargetOpInfoLis1",arrList1);
					request.setAttribute("ContactPlyInfoList",contacList);
					if (!StringTool.checkEmptyString(custId)) {
						List<ContactInfo> list = contactService.getAllByUserId(custId,orderNo);
						request.setAttribute("contactList", list);
					}
				}
			}else if("setPerformer".equals(doType)) {
				String[]orderArr =request.getParameterValues("OrderCheckbox");//获得选中的工单
				String performer_id = request.getParameter("myUserId");//获取用户字符串
				String [] userArr = null;
				if (!StringTool.checkEmptyString(performer_id)) {
					userArr = performer_id.split(",");//得到用户集合
				}
				int index = 0;
				if(null!=orderArr&&null!=userArr){
					for(int i=0;i<orderArr.length;i++){
						int orderNo = Integer.parseInt(orderArr[i]);
						OrderInfo  oo =orderInfoService.getOneById(orderNo);
						if(orderArr.length>userArr.length){
							if(index==userArr.length){
								index = 0;
							}
						}
						UserInfo uu =userService.getById(userArr[index]);
						if("2".equals(setType)){
							oo.setPerformer_id(uu);
						}else if("3".equals(setType)){
							oo.setOld_performer_id(oo.getPerformer_id().getUserId());
							oo.setPerformer_id(uu);
						}
						index++;
						//开始添加流水单
						OrderAdjustInfo adjustInfo = new OrderAdjustInfo();
						adjustInfo.setChannleInfo(oo.getChannelInfo()!=null?oo.getChannelInfo():null);
						adjustInfo.setOldChannler(oo.getOldChannelInfo()!=null?oo.getOldChannelInfo().getChannleId():0);
						String oldUserId = oo.getOld_performer_id();
						if (!StringTool.checkEmptyString(oldUserId)) {
							UserInfo u1 = userService.getById(oldUserId);
							adjustInfo.setOldperformer(u1);
						}
						adjustInfo.setPerformer(oo.getPerformer_id());
						adjustInfo.setOrderId(oo.getOrder_no());
						adjustInfo.setStatus(1);
						UserInfo creator = userService.getById(loginUser.user_id);
						adjustInfo.setUpdatePersonal(creator);
						Date myDate = new Date();
						adjustInfo.setUpdateTime(myDate);
						adjustService.save(adjustInfo);
					}
				}
				List<OrderInfo> list = orderInfoService.getAllListBySearch(entity, 0);
				request.setAttribute("orderList", list);
			}else if("add".equals(doType)) {
			}else if("modify".equals(doType)) {
			}else if("saveTrans".equals(doType)) {
				 String transName = request.getParameter("transName");
				 String custType =request.getParameter("custType");
				 String channleId = request.getParameter("channleId");
				 String passUser = request.getParameter("passUser");
				 String content = request.getParameter("content");
				 String [] arr = request.getParameterValues("OrderCheckbox");
				 TransInfo transInfo = new TransInfo();
					if (!StringTool.checkEmptyString(transName)) {
						transInfo.setTransName(transName);
					}
					if (!StringTool.checkEmptyString(content)) {
						transInfo.setTransUse(content);
					}
					if (!StringTool.checkEmptyString(custType)) {
						transInfo.setCustType(Integer.parseInt(custType));
					}
					if (!StringTool.checkEmptyString(channleId)) {
						ChannleInfo channle =channleService.getById(Integer.parseInt(channleId));
						transInfo.setNewChannle(channle);
					}
					UserInfo uu = userService.getById(loginUser.user_id);
					transInfo.setCreator(uu);
					Date newDate = new Date();
					transInfo.setTransDate(newDate);
					transInfo.setState(-1);//表示未提及
					transInfo.setStatus(1);
					if (!StringTool.checkEmptyString(passUser)) {
						UserInfo uu2 = userService.getById(passUser);
						transInfo.setPassUser(uu2);
					}
					transService.save(transInfo);
					//开始像列表中添加信息TransList
					if(null!=arr){
						for(int i=0;i<arr.length;i++){
							TransListInfo listInfo = new TransListInfo();
							listInfo.setCustType(transInfo.getCustType());
							OrderInfo order = orderInfoService.getOneById(Integer.parseInt(arr[i]));
							order.setOrder_state(2);
							orderInfoService.save(order);
							listInfo.setTransId(transInfo.getTransId());
							listInfo.setOrderInfo(order);
							listInfo.setStatus(1);
							listInfo.setUpdateDate(newDate);
							listInfo.setUpdateUser(transInfo.getCreator());
							transListService.save(listInfo);
						}
					}
					//结束后刷新
					if (!StringTool.checkEmptyString(setType)) {
						if(setType.equals("2")){
							entity.setPerformer_id(null);
						}
					}
					List<OrderInfo> list = orderInfoService.getAllListBySearch(entity, 0);
					request.setAttribute("orderList", list);
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
