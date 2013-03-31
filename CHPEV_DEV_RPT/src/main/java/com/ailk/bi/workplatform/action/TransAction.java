package com.ailk.bi.workplatform.action;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ailk.bi.marketing.service.IChannleService;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.ailk.bi.workplatform.entity.OrderInfo;
import com.ailk.bi.workplatform.entity.TransInfo;
import com.ailk.bi.workplatform.entity.TransListInfo;
import com.ailk.bi.workplatform.entity.TransPassInfo;
import com.ailk.bi.workplatform.entity.UserInfo;
import com.ailk.bi.workplatform.service.IOrderInfoService;
import com.ailk.bi.workplatform.service.ITransService;
import com.ailk.bi.workplatform.service.IUserService;
import com.ailk.bi.workplatform.service.impl.TransListServiceImpl;
import com.ailk.bi.workplatform.service.impl.TransPassServiceImpl;
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
public class TransAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	@Resource(name = "transService")
	private ITransService transService;
	@Resource(name = "userService")
	private IUserService userService;
	@Resource(name = "transPassService")
	private TransPassServiceImpl transPassService;
	@Resource(name = "transListService")
	private TransListServiceImpl transListService;
	@Resource(name = "orderInfoService")
	private IOrderInfoService orderInfoService;
	@Resource(name = "channleService")
	private IChannleService channleService;


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
		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}
		InitServlet.init(super.config, this, "userService");
		//以下是获得查询类表页面的查询条件//
		String showType = request.getParameter("showType");//获得制定的的JSP页面
		String transState = request.getParameter("transState");//获得具体的操作
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
		if (StringTool.checkEmptyString(qryStruct.showType)) {
			qryStruct.showType = showType;
		}
		if (StringTool.checkEmptyString(qryStruct.transState)) {
			qryStruct.transState = transState;
		}
		//加载查询条件
		 TransInfo entity = new  TransInfo();
			if (!StringTool.checkEmptyString(showType)) {
				//showType=1 我是申请人 ；2 ：我是审批人
				entity.showType=Integer.parseInt(showType);
			}else{
				entity.showType=-999;
			}
			if (!StringTool.checkEmptyString(transState)) {
				entity.setState(Integer.parseInt(transState));
			}else{
				entity.setState(-999);
			}
			 entity.loginUser =loginUser.user_id;//保存登录用户
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
		InitServlet.init(super.config, this, "transService");
		InitServlet.init(super.config, this, "userService");
		InitServlet.init(super.config, this, "transPassService");
		InitServlet.init(super.config, this, "transListService");
		InitServlet.init(super.config, this, "orderInfoService");
		InitServlet.init(super.config, this, "channleService");





		if (null != transService) {
			 if("search".equals(doType)) {
				 List <TransInfo>transList = transService.getAll(entity, 0);
				 request.setAttribute("transList", transList);
			}else if("pass1".equals(doType)) {//提交给审批人
				String myTransId = request.getParameter("myTransId");
				String myPassUser = request.getParameter("myPassUser");
				UserInfo passUser = null;
				if (!StringTool.checkEmptyString(myPassUser)) {
					 passUser = userService.getById(myPassUser);
				}
				if (!StringTool.checkEmptyString(myTransId)) {
					TransInfo transOO = transService.getById(Integer.parseInt(myTransId));
					if(transOO!=null){
						transOO.setState(0);
						transOO.setPassUser(passUser);
						transService.save(transOO);
					}
				}
				 List <TransInfo>transList = transService.getAll(entity, 0);
				 request.setAttribute("transList", transList);
			}else if("passBack".equals(doType)) {//工单申请撤回
				String myTransId = request.getParameter("myTransId");
				if (!StringTool.checkEmptyString(myTransId)) {
					TransInfo transOO = transService.getById(Integer.parseInt(myTransId));
					if(transOO!=null){
						transOO.setState(3);//撤回
						transService.save(transOO);
					}
					//同时将工单状态修改
					List<TransListInfo>transListInfos = transListService.getAllByTransId(transOO.getTransId());
					if(transListInfos!=null){
						for(int i=0;i<transListInfos.size();i++){
							OrderInfo oo =transListInfos.get(i).getOrderInfo();
							if(oo!=null){
								oo.setOrder_state(1);//将工单状态改回为（待办：1）
								orderInfoService.save(oo);
							}
						}
					}
				}
				 List <TransInfo>transList = transService.getAll(entity, 0);
				 request.setAttribute("transList", transList);
			}else if("modify".equals(doType)){
				String   myTransId = request.getParameter("myTransId");
				if (!StringTool.checkEmptyString(myTransId)) {
					session.setAttribute("myTransId", myTransId);
					TransInfo transInfo =	 (TransInfo)session.getAttribute("transInfo");
					if(null==transInfo){
						 transInfo = transService.getById(Integer.parseInt(myTransId));
					}
					List<TransListInfo>tls = transListService.getAllByTransId(Integer.parseInt(myTransId));
					@SuppressWarnings("unchecked")
					Map<Integer,OrderInfo> map = (Map<Integer,OrderInfo>)	session.getAttribute("orderMap");
					if(null==map){
						 map = new HashMap<Integer,OrderInfo>();
					}
					if(null!=tls){
						for(int i=0;i<tls.size();i++){
							OrderInfo oo = tls.get(i).getOrderInfo();
							if(null!=oo){
								//orders.add(oo);
								 map.put(oo.getOrder_no(),oo);
							}
						}
					}
					session.setAttribute("transInfo", transInfo);
					session.setAttribute("orderMap", map);
				}

			}else if("delOrder".equals(doType)){
				String   delOrderId = request.getParameter("delOrderId");
				if (!StringTool.checkEmptyString(delOrderId)) {
					@SuppressWarnings("unchecked")
					Map<Integer,OrderInfo>map = (Map<Integer,OrderInfo>)	session.getAttribute("orderMap");
					if(null!=map){
						OrderInfo delOrder = orderInfoService.getOneById(Integer.parseInt(delOrderId));
						if(null!=delOrder){
							map.remove(Integer.parseInt(delOrderId));
							delOrder.setOrder_state(1);//改委代办
							orderInfoService.save(delOrder);
						}
					}
					session.setAttribute("orderMap", map);
				}
			}else if("addOrder".equals(doType)){
				String [] orderIDS = request.getParameterValues("OrderCheckbox");
				@SuppressWarnings("unchecked")
				Map<Integer,OrderInfo> map = (Map<Integer,OrderInfo>)	session.getAttribute("orderMap");
				if(null==map){
				     map = new HashMap<Integer,OrderInfo>();
				}
				if(null!=orderIDS){
					for(int i=0;i<orderIDS.length;i++){
						OrderInfo  newOO = orderInfoService.getOneById(Integer.parseInt(orderIDS[i]));
						if(null!=newOO){
							newOO.setOrder_state(2);
							orderInfoService.save(newOO);
							map.put(newOO.getOrder_no(), newOO);
						}
					}
				}
				session.setAttribute("orderMap", map);
			}else if("saveTrans".equals(doType)) {
				 String transName = request.getParameter("txt_transName");
				 String custType =request.getParameter("txt_custType");
				 String channleId = request.getParameter("txt_channleId");
				 String content = request.getParameter("txt_content");
				 TransInfo	transInfo = new TransInfo();
				 transInfo =	 (TransInfo)session.getAttribute("transInfo");
				 @SuppressWarnings("unchecked")
				Map <Integer,OrderInfo> map = (Map <Integer,OrderInfo>)	session.getAttribute("orderMap");
					session.setAttribute("transInfo", null);
					session.setAttribute("orderMap", null);
				 if(transInfo==null){
					 transInfo = new TransInfo();
				 }
				 if(null==map){
					  map = new HashMap <Integer,OrderInfo>();
				 }
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
					transService.save(transInfo);
					//开始像列表中添加信息TransList
					transListService.deleteByTransId(transInfo.getTransId());
					if(null!=map){
						 for(Integer o: map.keySet()){
							TransListInfo listInfo = new TransListInfo();
							listInfo.setCustType(transInfo.getCustType());
							OrderInfo order =orderInfoService.getOneById(o);
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
					//刷新
					List <TransInfo>transList = transService.getAll(entity, 0);
					 request.setAttribute("transList", transList);
			}else if("save".equals(doType)) {
				String   passResult = request.getParameter("passResult");
				String   passDesc = request.getParameter("passDesc");
				String transId = request.getParameter("transId");
				TransPassInfo passInfo = new TransPassInfo();
				if (!StringTool.checkEmptyString(passResult)) {
					passInfo.setDecision(Integer.parseInt(passResult));
				}
				if (!StringTool.checkEmptyString(passDesc)) {
					passInfo.setDesc(passDesc);
				}
				TransInfo info =null;
				if (!StringTool.checkEmptyString(transId)) {
					info = transService.getById(Integer.parseInt(transId));
					if(null!=info){
						passInfo.setTransInfo(info);
						passInfo.setUserInfo(info.getPassUser());
					}
				}
				passInfo.setPid("暂无");
				passInfo.setpDate(new Date());
				passInfo.setStepFlag("暂无");
				passInfo.setStepNext("暂无");
				transPassService.save(passInfo);
				//修改派单申请表
				info.setState(passInfo.getDecision());
				transService.save(info);
				//修改所有的工单状态
				List<TransListInfo> myList =transListService.getAllByTransId(info.getTransId());
				if(null!=myList){
					for(int i=0;i<myList.size();i++){
						if(passInfo.getDecision()==1){
							OrderInfo oo= myList.get(i).getOrderInfo();
							if(null!=oo){
								oo.setOuter_channel(1);
								oo.setOrder_state(1);//转回为待办
								oo.setOldChannelInfo(oo.getChannelInfo());//修改原来工单的状态
								oo.setChannelInfo(info.getNewChannle());
								orderInfoService.save(oo);
							}
						}
					}
					//结束刷新
					 List <TransInfo>transList = transService.getAll(entity, 0);
					 request.setAttribute("transList", transList);
				}
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
