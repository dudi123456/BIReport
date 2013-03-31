package com.ailk.bi.login.action;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.struct.InfoResStruct;
import com.ailk.bi.base.struct.LoginSystemStruct;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.MD5;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.login.UserInfoConst;
import com.ailk.bi.login.dao.IUserDao;
import com.ailk.bi.login.dao.impl.UserDaoImpl;
import com.ailk.bi.system.common.LSInfoUser;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.ailk.bi.system.facade.impl.ServiceFacade;
import com.ailk.bi.userlog.entity.UiInfoUserLog;
import com.eetrust.plugin.UidPlugin;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LoginHTMLAction extends HTMLActionSupport {

	public static final String USER_TABLE_PREFIX = "UI_BIBBS_";

	private static final long serialVersionUID = -2417048084399177216L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		String opType = StringB.NulltoBlank(request.getParameter("opType"));
		HttpSession session = request.getSession(true);
		// 对于登录系统的处理
		String strReturn = "";
		if ("LOGIN".equals(opType)) {
			strReturn = userLogin(request, response, session);
			setNextScreen(request, strReturn + ".screen");
		} else if ("changpwd".equals(opType)) {// 修改密码
			updatePwd(request, response, session);
		} else if ("forgetpwd".equals(opType)) {// 忘记密码
			setNextScreen(request, "forgetpwd.screen");
		} else if ("getpwd".equals(opType)) {// 取得密码
			strReturn = getUserPassword(request, response, session);
			setNextScreen(request, strReturn + ".screen");
		} else if ("logout".equals(opType)) {// 退出
			logout(request, response, session);
		} else if ("getmd5".equals(opType)) {// 获取MD5密码
			getPwd2MD5(request, response);
		} else if ("index".equals(opType)) {//
			if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,
					response)) {
				return;
			}
			setNextScreen(request, "loginindex.screen");
		} else if ("sso".equals(opType)) {// SSO登录
			strReturn = userLoginSSO(request, response, session);

			setNextScreen(request, strReturn + ".screen");
		}
	}

	private void getPwd2MD5(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			request.setCharacterEncoding("gb2312");
			response.setContentType("text/xml;charset=GB2312");
			response.setHeader("Cache-Control", "no-cache");
			String srcpwd = (String) request.getParameter("srcpwd");
			String md5pwd = MD5.getInstance().getMD5ofStr(srcpwd);
			response.getWriter().println(
					"<?xml　version='1.0'　encoding='GB2312' ?>");
			response.getWriter().println("<root>");
			response.getWriter().println("<content>");
			response.getWriter().print(md5pwd);
			response.getWriter().println("</content>");
			response.getWriter().println("</root>");
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void logout(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HTMLActionException {
		// 插入日志
		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		UiInfoUserLog obj = new UiInfoUserLog();
		obj.setUserId(loginUser.oper_no);
		obj.setSessionId(session.getId());
		DAOFactory.getUserLogDao().update(obj);
		request.getSession().invalidate();
	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HTMLActionException
	 * @desc:获取用户密码
	 */
	private String getUserPassword(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HTMLActionException {
		String authCode = request.getParameter("authCode");
		String strReturn = "";
		// 取得广播操作员工号
		String oper_no = StringB.NulltoBlank(request.getParameter("oper_no"));
		String question = StringB.NulltoBlank(request.getParameter("question"));
		String answer = StringB.NulltoBlank(request.getParameter("answer"));
		if (null == oper_no || "".equals(oper_no)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "请输入操作员工号");
		}
		String auCo = (String) session.getAttribute("forgetPwdCode");
		if (!auCo.equals(authCode)) {// 验证码不正确
			request.setAttribute("COMMON_MSG", "验证码输入不正确，请重新输入");
			strReturn = "unvalidVerifyCode";
		} else {
			String sql = "select user_id,region_id,dept_id,duty_id,user_name,pwd,status,question,answer,res_char ,channle_id from ui_info_user t"
					+ " where t.user_id=? and question=? and answer=?";
			String strWhere[] = new String[] { oper_no, question, answer };
			InfoOperTable userInfo = DAOFactory.getUserDao().getUserPassword(
					sql, strWhere);
			if (userInfo == null) {
				request.setAttribute("COMMON_MSG", "没找到相关信息");
				strReturn = "unvalidVerifyCode";
			} else {
				session.setAttribute("VIEW_TREE_LIST", userInfo);
				strReturn = "getorginpwd";
			}
		}
		return strReturn;
	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HTMLActionException
	 * @desc:用户SSO登录
	 */
	private String userLoginSSO(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HTMLActionException {

		System.out.println("login from sso");

		String token = request.getParameter("EIACToken");

		System.out.println("sso token:" + token);

		String strReturn = "";
		UidPlugin uidp = new UidPlugin(token);
		int i = uidp.getAuthorization();

		System.out.println("login flag:" + i);

		if (i == 1) {// eiac服务器验证成功

			// 验证成功后可取得参数

			String role = uidp.getRole();// 用户角色
			String oper_no = role;// 用户姓名
			// ……进一步处理成功信息
			ServiceFacade serviceFac = new ServiceFacade();
			String sql = "select user_id,region_id,dept_id,duty_id,user_name,pwd,status,question,answer ,channle_id from ui_info_user t"
					+ " where t.user_id=?";
			System.out.println("sql sso:" + sql + ":" + role);

			String strWhere[] = new String[] { role };
			String[][] userInfo = serviceFac.UserLogin(sql, strWhere);
			if (userInfo == null || userInfo.length == 0) {// 没有该用户
				request.setAttribute("COMMON_MSG", "用户名，请重新输入");
				strReturn = "unvalidCheckPwd";
			} else {
				String system_id = "2";
				session.setAttribute("system_id", system_id);
				String user_id = role;
				String bi_user = role;
				IUserDao userDao = new UserDaoImpl();
				String lo_user = userDao.getLoUser(role);

				if (lo_user == null) {
					lo_user = user_id;
					bi_user = userDao.getBiUser(lo_user);
					if (bi_user == null) {
						bi_user = user_id;
					}
				}
				String[][] logSys = userDao.getSystemLogin(
						userDao.getSqlIn(bi_user), userDao.getSqlIn(lo_user));
				StringBuffer strB = new StringBuffer("");
				HashMap map = new HashMap();
				if (logSys != null && logSys.length > 0) {
					strB.append("系　　统：");
					for (int j = 0; j < logSys.length; j++) {

						LoginSystemStruct ls = new LoginSystemStruct();
						ls.system_id = logSys[j][0];
						ls.system_name = logSys[j][1];
						ls.user_id = logSys[j][2];
						map.put(logSys[j][0], ls);
					}
					request.getSession().setAttribute(
							WebConstKeys.ATTR_C_LoginSystem, map);
				}

				LoginSystemStruct ls = (LoginSystemStruct) map.get(system_id);
				oper_no = ls.user_id;

				InfoOperTable loginUser = serviceFac.getUserInfo(oper_no);
				InfoOperTable boUser = serviceFac.qryBOUserInfo(loginUser);
				loginUser.bo_user = boUser.bo_user;
				loginUser.bo_pwd = boUser.bo_pwd;
				String login_ip = request.getRemoteAddr();
				loginUser.login_ip = login_ip;
				// 植入会话保持状态
				session.setAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE,
						loginUser);
				// session.setAttribute(WebConstKeys.LOGIN_USER_MENU_LIST,
				// serviceFac.getUserMenuInfo(oper_no));

				session.setAttribute(WebConstKeys.LOGIN_USER_MENU_LIST,
						serviceFac.getUserMenuInfo(oper_no, system_id));

				// 加载系统资源表信息！
				ArrayList list = (ArrayList) new CommonFacade()
						.getInfoResStruct();
				InfoResStruct[] infoRes = (InfoResStruct[]) list
						.toArray(new InfoResStruct[list.size()]);
				session.setAttribute(WebConstKeys.ATTR_C_INFORESSTRUCT, infoRes);

				// 加载系统资源表信息！
				// ArrayList listMenu = (ArrayList) new
				// CommonFacade().getInfoMenuStruct();
				ArrayList listMenu = (ArrayList) new CommonFacade()
						.getInfoMenuStruct(system_id);

				InfoResStruct[] infoMenu = (InfoResStruct[]) listMenu
						.toArray(new InfoResStruct[listMenu.size()]);
				session.setAttribute(WebConstKeys.ATTR_C_INFOMenuSTRUCT,
						infoMenu);
				// 加入登录日志
				insertUserLoginLog(request, response, session);
				// MVFORUM LOGIN

				// int sex = 0;
				// if (StringB.NulltoBlank(loginUser.sex).length() > 0) {
				// sex = Integer.parseInt(StringB.NulltoBlank(loginUser.sex));
				// }

				session.setAttribute(
						com.ailk.bi.common.app.WebChecker.LOGIN_FLAG, "1");
				strReturn = "LoginInOK";
				UserCtlRegionStruct region = serviceFac
						.getUserRegionInfo(oper_no);
				try {
					System.out.println(region.toXml());
				} catch (AppException e) {
					e.printStackTrace();
				}
				session.setAttribute(WebConstKeys.ATTR_C_UserCtlStruct, region);
			}

		} else {// eiac服务器验证失败

			String error = uidp.getError();// 可得到验证错误
			request.setAttribute("COMMON_MSG", error);
			strReturn = "unvalidCheckPwd";

		}

		System.out.println(strReturn + ":sso========");
		return strReturn;
	}

	/**
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws HTMLActionException
	 * @desc:用户登录
	 */
	private String userLogin(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HTMLActionException {
		String screenXY = StringB.NulltoBlank(request.getParameter("screenXY"));
		String authCode = request.getParameter("authCode");
		String system_id = request.getParameter("system_id");
		session.setAttribute("system_id", system_id);

		String strReturn = "";
		// 取得广播操作员工号
		String oper_no = request.getParameter("oper_no");
		String pwd = request.getParameter("txtPassword");
		System.out.println("pwd is =" + pwd + "--111111====-,"
				+ MD5.getInstance().getMD5ofStr(pwd));
		if (!StringUtils.isBlank(pwd)) {
			pwd = pwd.trim();
		}
		if (null == oper_no || "".equals(oper_no)) {
			request.getParameter("/login/login.jsp");
			// throw new HTMLActionException(session,
			// HTMLActionException.WARN_PAGE, "请输入操作员工号");
		}

		InfoOperTable user = LSInfoUser.getUserInfo(oper_no);
		if ("99".equals(user.status)) {
			request.setAttribute("COMMON_MSG", "该用户已经被锁定，登录请联系系统管理员！");
			strReturn = "unvalidCheckPwd";
		} else {
			String auCo = (String) session.getAttribute("authCode");
			if (!auCo.equals(authCode)) {// 验证码不正确
				request.setAttribute("COMMON_MSG", "验证码输入不正确，请重新输入");
				strReturn = "unvalidCheckPwd";
			} else {
				ServiceFacade serviceFac = new ServiceFacade();
				String sql = "select user_id,region_id,dept_id,duty_id,user_name,pwd,status,question,answer,channle_id from ui_info_user t"
						+ " where t.user_id=? and pwd=?";
				String strWhere[] = new String[] { oper_no,
						MD5.getInstance().getMD5ofStr(pwd) };
				String[][] userInfo = serviceFac.UserLogin(sql, strWhere);
				if (userInfo == null || userInfo.length == 0) {// 没有该用户
					int num = 0;
					// System.out.println(session.getAttribute("wrong_num"));
					if (session.getAttribute("wrong_num") == null) {
						num = 1;
					} else {
						num = Integer.parseInt(session
								.getAttribute("wrong_num") + "") + 1;
					}
					session.setAttribute("wrong_num", num + "");
					if (num == 1) {
						request.setAttribute("COMMON_MSG",
								"用户名、密码不正确 （第一次，最大三次），请重新输入！");
					} else if (num == 2) {
						request.setAttribute("COMMON_MSG",
								"用户名、密码不正确 （第二次，最大三次），请重新输入！");
					} else if (num == 3) {
						request.setAttribute("COMMON_MSG",
								"用户名、密码不正确 （第三次，用户被锁定），请通知系统管理员！");
						// 置禁用状态
						LSInfoUser.updateUserStatus(oper_no,
								UserInfoConst.USER_STATE.LOCKED.getState());

					}
					strReturn = "unvalidCheckPwd";
				} else {
					// 屏蔽多系统登陆
					// HashMap map = (HashMap)
					// request.getSession().getAttribute(WebConstKeys.ATTR_C_LoginSystem);
					// LoginSystemStruct ls = (LoginSystemStruct)
					// map.get(system_id);
					// oper_no = ls.user_id;

					InfoOperTable loginUser = serviceFac.getUserInfo(oper_no);
					// 屏蔽bo用户信息
					// InfoOperTable boUser =
					// serviceFac.qryBOUserInfo(loginUser);
					// loginUser.bo_user = boUser.bo_user;
					// loginUser.bo_pwd = boUser.bo_pwd;
					if (screenXY.length() > 0) {
						session.setAttribute(WebKeys.Screenx,
								screenXY.split("_")[0].toString());
						session.setAttribute(WebKeys.Screeny,
								screenXY.split("_")[1].toString());
					}
					/*
					 * List roleInfo = serviceFac.getUserRoleInfo(oper_no); if
					 * (roleInfo!=null && roleInfo.size()>0){ InfoRoleTable
					 * roleObj = (InfoRoleTable)roleInfo.get(0); }
					 */
					// loginUser.role_code = "1";
					String login_ip = request.getRemoteAddr();
					loginUser.login_ip = login_ip;
					// 植入会话保持状态

					session.setAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE,
							loginUser);

					// 验证密码失效时长
					// 判断用户密码是否过期
					String UT_date = loginUser.end_date;
					String curr_date = com.ailk.bi.common.app.DateUtil
							.dateToString(com.ailk.bi.common.app.DateUtil
									.getNowDate());
					// String length =
					// com.asiabi.common.app.DateUtil.getDateSpans(curr_date,
					// UT_date,"yyyyMMdd");
					int length = StringB.toInt(UT_date, 0)
							- StringB.toInt(curr_date, 0);
					System.out.println("length==============" + length);
					if (length <= 0) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE,
								"<font size=2><b>您的密码已经过期，请重新设定密码！</b></font>",
								"ChgPwd.screen");
					} else if (length <= 15) {
						throw new HTMLActionException(session,
								HTMLActionException.WARN_PAGE,
								"<font size=2><b>您的密码还有" + length
										+ "天将要过期，请重新设定密码！</b></font>",
								"ChgPwd.screen");
					}

					// session.setAttribute(WebConstKeys.LOGIN_USER_MENU_LIST,
					// serviceFac.getUserMenuInfo(oper_no));

					session.setAttribute(WebConstKeys.LOGIN_USER_MENU_LIST,
							serviceFac.getUserMenuInfo(oper_no, system_id));

					// 加载系统资源表信息！
					ArrayList list = (ArrayList) new CommonFacade()
							.getInfoResStruct();
					InfoResStruct[] infoRes = (InfoResStruct[]) list
							.toArray(new InfoResStruct[list.size()]);
					session.setAttribute(WebConstKeys.ATTR_C_INFORESSTRUCT,
							infoRes);

					// 加载系统资源表信息！
					// ArrayList listMenu = (ArrayList) new
					// CommonFacade().getInfoMenuStruct();
					ArrayList listMenu = (ArrayList) new CommonFacade()
							.getInfoMenuStruct(system_id);

					InfoResStruct[] infoMenu = (InfoResStruct[]) listMenu
							.toArray(new InfoResStruct[listMenu.size()]);
					session.setAttribute(WebConstKeys.ATTR_C_INFOMenuSTRUCT,
							infoMenu);
					// 加入登录日志
					insertUserLoginLog(request, response, session);
					// MVFORUM LOGIN
					// int sex = 0;
					// if (StringB.NulltoBlank(loginUser.sex).length() > 0) {
					// sex = Integer.parseInt(StringB
					// .NulltoBlank(loginUser.sex));
					// }

					// 配置本系统登录标志
					session.setAttribute(
							com.ailk.bi.common.app.WebChecker.LOGIN_FLAG, "1");
					strReturn = "LoginInOK";
					UserCtlRegionStruct region = serviceFac
							.getUserRegionInfo(oper_no);
					try {
						System.out.println(region.toXml());
					} catch (AppException e) {
						e.printStackTrace();
					}
					region.setCtl_lvl("0");
					session.setAttribute(WebConstKeys.ATTR_C_UserCtlStruct,
							region);
					// 成功登陆清除错误累计
					LSInfoUser.updateUserStatus(oper_no,
							UserInfoConst.USER_STATE.NORMAL.getState());
				}
			}
		}

		return strReturn;
	}

	private void insertUserLoginLog(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		if (loginUser == null) {
			return;
		}
		// 更新当前用户没有正常退出的信息
		String sql = "update ui_sys_log set leave_time=(log_oper_time + interval '2' hour) where log_oper_no='"
				+ loginUser.oper_no
				+ "' and log_type='1' and leave_time is null and  (sysdate-log_oper_time)*24>=2";
		DAOFactory.getUserDao().excuteSql(sql);

		sql = "update ui_sys_log set leave_time=sysdate where log_oper_no='"
				+ loginUser.oper_no
				+ "' and log_type='1' and leave_time is null and  (sysdate-log_oper_time)*24<2";
		DAOFactory.getUserDao().excuteSql(sql);

		UiInfoUserLog obj = new UiInfoUserLog();
		obj.setSessionId(session.getId());
		obj.setUserId(loginUser.oper_no);
		obj.setUserName(loginUser.user_name);
		obj.setClientAddress(request.getRemoteAddr());
		obj.setOperation("1");
		obj.setResourceId("");
		obj.setMsg("用户登录");

		DAOFactory.getUserLogDao().insert(obj);
	}

	/**
	 * 更改密码
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @throws HTMLActionException
	 */
	private void updatePwd(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws HTMLActionException {
		String oldpwd = StringB.NulltoBlank(request.getParameter("oldpwd"));
		String newpwd = StringB.NulltoBlank(request.getParameter("password"));
		String md5Pwd = MD5.getInstance().getMD5ofStr(oldpwd);
		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		if (md5Pwd.equals(loginUser.pwd_encode)) {
			String newEncodePwd = MD5.getInstance().getMD5ofStr(newpwd);
			String sql = "update ui_info_user set pwd='"
					+ newEncodePwd
					+ "',RES_CHAR='"
					+ CommTool.getEncrypt(newpwd)
					+ "',end_date=to_char(to_date(end_date,'yyyymmdd')+90,'yyyymmdd') where user_id='"
					+ loginUser.oper_no + "'";
			DAOFactory.getUserDao().excuteSql(sql);
			// 变更内存用户信息
			loginUser.pwd = newEncodePwd;
			session.removeAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
			session.setAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE, loginUser);

			// 变更密码
			// 密码变更记录
			String dateStr = com.ailk.bi.common.app.DateUtil
					.dateToString(com.ailk.bi.common.app.DateUtil.getNowDate());
			String pwdStr = "INSERT INTO ui_info_user_pwd_his(user_id,user_name,pwd,system_id,op_time,unicode) values('"
					+ loginUser.oper_no
					+ "','"
					+ loginUser.user_name
					+ "','"
					+ newEncodePwd
					+ "','1',"
					+ dateStr
					+ ",'"
					+ CommTool.getEncrypt(newpwd) + "')";
			DAOFactory.getUserDao().excuteSql(pwdStr);

			request.setAttribute("CONST_SUBJECT_DEAL_INFO", "密码修改成功，请重新登录!");
			setNextScreen(request, "sysAdminCtrlDealOk.screen");
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"<font size=2><b>原密码不正确,请重新输入</b></font>",
					request.getContextPath() + "/system/changepwd.jsp");
		}
	}

}