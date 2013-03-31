package com.ailk.bi.system.common;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;

import com.ailk.bi.base.struct.UserRoleStruct;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.MD5;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.entity.InfoMenu;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.ailk.bi.system.service.impl.UserServiceImpl;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoUser {

	public static InfoOperTable getUserInfo(String user_id) {
		StringBuffer sql = new StringBuffer();
		InfoOperTable sstUser = new InfoOperTable();
		try {
			sql.append(
					"select a.USER_ID,REGION_ID,DEPT_ID,DUTY_ID,USER_NAME,PWD,STATUS,BEGIN_DATE,END_DATE,CREATE_TIME,BIRTHDAY,MOBILE_PHONE,HOME_PHONE,OFFICE_PHONE,")
					.append("OFFICE_FAX,EMAIL,SEX,AGE,POSTALCODE,ADDRESS,NATION,PASSPORTNO,DIRECTOR,QUESTION,ANSWER,RES_CHAR,b.group_id ,channle_id ")
					.append("from UI_INFO_USER a,UI_RULE_USER_GROUP b where a.user_id=b.user_id and a.user_id='")
					.append(user_id).append("'");

			Vector v = WebDBUtil.execQryVector(sql.toString(), "");
			if (v.size() > 0) {
				Vector tempv = (Vector) v.get(0);
				sstUser.user_id = tempv.get(0).toString();
				sstUser.region_id = tempv.get(1).toString();
				sstUser.dept_id = tempv.get(2).toString();
				sstUser.duty_id = tempv.get(3).toString();
				sstUser.user_name = tempv.get(4).toString();
				sstUser.pwd = MD5.getInstance().getMD5ofStr(
						CommTool.getDecrypt(tempv.get(25).toString()).trim());
				sstUser.status = tempv.get(6).toString();
				sstUser.begin_date = tempv.get(7).toString();
				sstUser.end_date = tempv.get(8).toString();
				sstUser.create_time = tempv.get(9).toString();
				sstUser.birthday = tempv.get(10).toString();
				sstUser.mobile_phone = tempv.get(11).toString();
				sstUser.home_phone = tempv.get(12).toString();
				sstUser.office_phone = tempv.get(13).toString();
				sstUser.office_fax = tempv.get(14).toString();
				sstUser.email = tempv.get(15).toString();
				sstUser.sex = tempv.get(16).toString();
				sstUser.age = tempv.get(17).toString();
				sstUser.postalcode = tempv.get(18).toString();
				sstUser.address = tempv.get(19).toString();
				sstUser.nation = tempv.get(20).toString();
				sstUser.passportno = tempv.get(21).toString();
				sstUser.director = tempv.get(22).toString();
				sstUser.question = tempv.get(23).toString();
				sstUser.answer = tempv.get(24).toString();
				sstUser.res_char = tempv.get(25).toString();
				sstUser.group_id = tempv.get(26).toString();
				sstUser.channleId =  tempv.get(27).toString();
			}

		} catch (AppException ex) {
		}
		return sstUser;

	}

	/**
	 * 增加操作员信息,同时增加维系人员信息
	 *
	 * @param session
	 * @param infoOper
	 * @throws HTMLActionException
	 */
	public static void addNewOper(HttpServletRequest request,
			InfoOperTable infoOper) throws HTMLActionException {
		HttpSession session = request.getSession();
		// 取得当前的操作员信息
		InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		Vector sqlV = new Vector();

		try {
			Vector newSqls = getAddNewOperSqls(infoOper, request, loginUser);
			sqlV.addAll(newSqls);
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "操作员信息新增失败！"
							+ ex.getMessage());
		}

		// 转化
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();
			System.out.println("sql==================" + sqlArr[i]);
		}

		// 执行(移到相应的action中)
		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}

		if (i > 0) {
			// 返回
			String url = "UserView.screen?submitType=3&user_id="
					+ infoOper.user_id + "&region_id=" + infoOper.region_id
					+ "&dept_id=" + infoOper.dept_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "操作员信息新增成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "操作员信息新增失败！");
		}

	}

	/**
	 * 变更操作员信息
	 *
	 * @param session
	 * @param infoOper
	 * @throws HTMLActionException
	 */
	public static void updateOperInfo(HttpServletRequest request,
			InfoOperTable infoOper) throws HTMLActionException {
		// 会话
		HttpSession session = request.getSession();
		// 登陆操作员
		// 通用信息
		String user_id = infoOper.user_id;
		String region_id = infoOper.region_id;
		String dept_id = infoOper.dept_id;
		// SQL容器
		Vector sqlv = new Vector();

		// 更新操作员
		sqlv.addAll(getSqlvUpdateOper(request, infoOper));

		// 转化
		String sqlArr[] = new String[sqlv.size()];
		for (int i = 0; i < sqlv.size(); i++) {
			sqlArr[i] = sqlv.get(i).toString();
			System.out.println("updateOperInfo sqlArr[" + i
					+ "]==================" + sqlArr[i]);
		}

		// 执行(移到相应的action中)
		try {

			int i = 1;
			try {
				WebDBUtil.execTransUpdate(sqlArr);
			} catch (Exception e) {
				i = -1;
			}

			if (i > 0) {

				//
				String url = "UserView.screen?submitType=3&user_id=" + user_id
						+ "&region_id=" + region_id + "&dept_id=" + dept_id;
				if (user_id.equals(CommonFacade.getLoginId(session))) {
					setUserInSession(session, user_id);

				}
				throw new HTMLActionException(session,
						HTMLActionException.SUCCESS_PAGE, "操作员信息变更成功！", url);
			} else {

				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "操作员信息变更失败！");
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 删除操作员信息
	 *
	 * @param session
	 * @param infoOper
	 * @throws HTMLActionException
	 */
	public static void deleteOperInfo(HttpServletRequest request,
			InfoOperTable infoOper) throws HTMLActionException {
		HttpSession session = request.getSession();
		// String userID = infoOper.user_id;
		Vector sqlv = new Vector();
		// 删除操作员信息
		sqlv.addAll(getSqlvDeleteOper(request, infoOper));

		// 转化
		String sqlArr[] = new String[sqlv.size()];
		for (int i = 0; i < sqlv.size(); i++) {
			sqlArr[i] = sqlv.get(i).toString();
			System.out.println("sql[i]================" + sqlArr[i]);
		}
		// 执行

		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}

		if (i > 0) {
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "操作员信息删除成功！",
					"UserView.screen?submitType=2");
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "操作员信息删除失败！");
		}

	}

	/**
	 * 获得删除操作员的组装sql Vector
	 *
	 * @param request
	 * @param infoOper
	 * @return
	 */
	public static Vector getSqlvDeleteOper(HttpServletRequest request,
			InfoOperTable infoOper) {
		Vector sqlv = new Vector();
		/*
		 * // 记录删除日志 String log_sql_a = getLogSql(request, "C2212", "3",
		 * infoOper.user_id); sqlv.add(log_sql_a); // 删除用户控制区域表 String sqlb =
		 * ""; try { sqlb = SQLGenator.genSQL("D2202", infoOper.user_id); }
		 * catch (AppException e) { e.printStackTrace(); } sqlv.add(sqlb);
		 */
		// 删除用户角色
		String log_sql_b = getLogSql(request, "C2211", "3", infoOper.user_id);
		sqlv.add(log_sql_b);
		//
		String sqlc = "delete from UI_RULE_USER_ROLE where user_id ='"
				+ infoOper.user_id + "'";

		sqlv.add(sqlc);

		String log_sql2 = getLogSql(request, "C2212", "3", infoOper.user_id);
		sqlv.add(log_sql2);
		// 删除用户组关系
		sqlc = "delete from UI_RULE_USER_GROUP where user_id ='"
				+ infoOper.user_id + "'";

		String log_sql_c = getLogSql(request, "C2210", "3", infoOper.user_id);
		sqlv.add(log_sql_c);

		sqlv.add(sqlc);
		// 删除用户信息，设置失效标记
		String sqla = "delete from ui_info_user where user_id ='"
				+ infoOper.user_id + "'";
		sqlv.add(sqla);

		// 删除用户密码流水
		String sqlt = "delete from ui_info_user_pwd_his where user_id ='"
				+ infoOper.user_id + "'";
		sqlv.add(sqlt);
		return sqlv;
	}

	/**
	 * 更新操作员组装sql 密码pwd为明码
	 *
	 * @param request
	 * @param infoOper
	 * @return
	 */
	public static Vector getSqlvUpdateOper(HttpServletRequest request,
			InfoOperTable infoOper) {
		Vector sqlv = new Vector();
		String oldUserId = StringB.NulltoBlank(request
				.getParameter("hid_old_user_id"));
		System.out.println("oldUserId:" + oldUserId);
		InfoOperTable oldUser = getUserInfo(oldUserId);

		// 判断密码是否变更
		//
		if (!infoOper.pwd.equals(oldUser.pwd)) {
			// 记录密码变更
			String encode = CommTool.getEncrypt(infoOper.pwd).trim();
			String pwd = MD5.getInstance().getMD5ofStr(infoOper.pwd);
			String dateStr = DateUtil.dateToString(DateUtil.getNowDate());
			// 密码变更记录
			String pwdStr = "INSERT INTO ui_info_user_pwd_his(user_id,user_name,pwd,system_id,op_time,unicode) values('"
					+ infoOper.user_id
					+ "','"
					+ infoOper.user_name
					+ "','"
					+ pwd + "','1'," + dateStr + ",'" + encode + "')";
			sqlv.add(pwdStr);

		}

		// 变更字段信息
		String upStr = getUpdateFiledStr(infoOper, oldUser);
		String sqlC = "";
		if (upStr.length() > 0) {
			sqlC = " UPDATE ui_info_user SET " + upStr + "  WHERE user_id = '"
					+ oldUser.user_id + "'";

			sqlv.add(sqlC);

		}
		String log_sql1 = getLogSql(request, "C2212", "3", infoOper.user_id);
		sqlv.add(log_sql1);

		sqlC = " delete from UI_RULE_USER_GROUP where user_id='"
				+ oldUser.user_id + "'";
		sqlv.add(sqlC);
		sqlC = "insert into UI_RULE_USER_GROUP values ('" + infoOper.user_id
				+ "','" + infoOper.group_id + "')";
		sqlv.add(sqlC);

		String log_sql2 = getLogSql(request, "C2212", "1", infoOper.user_id);
		sqlv.add(log_sql2);
		// 记录变更操作员信息日志
		String log_sql = getLogSql(request, "C2210", "2", infoOper.user_id);
		sqlv.add(log_sql);
		return sqlv;
	}

	/**
	 * 取得更新字段
	 *
	 * @param userInfo
	 * @param user
	 * @return
	 */
	public static String getUpdateFiledStr(InfoOperTable userInfo,
			InfoOperTable olduser) {
		String upStr = "";

		if (!userInfo.dept_id.equals(olduser.dept_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " dept_id = '" + userInfo.dept_id + "'";

		}
		if (!userInfo.region_id.equals(olduser.region_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " region_id = '" + userInfo.region_id + "'";

		}

		if (!userInfo.status.equals(olduser.status)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " status = '" + userInfo.status + "'";

		}
		if (!userInfo.email.equals(olduser.email)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " email = '" + userInfo.email + "'";
		}

		if (!userInfo.user_name.equals(olduser.user_name)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " user_name = '" + userInfo.user_name + "'";
		}
		if (!userInfo.duty_id.equals(olduser.duty_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " duty_id = '" + userInfo.duty_id + "'";
		}

		if (!userInfo.begin_date.equals(olduser.begin_date)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " begin_date = '" + userInfo.begin_date + "'";
		}

		if (!userInfo.create_time.equals(olduser.create_time)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " create_time = '" + userInfo.create_time + "'";
		}
		if (!userInfo.question.equals(olduser.question)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " question = '" + userInfo.question + "'";
		}
		if (!userInfo.answer.equals(olduser.answer)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " answer = '" + userInfo.answer + "'";
		}

		if (!userInfo.birthday.equals(olduser.birthday)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " birthday = '" + userInfo.birthday + "'";

		}

		if (!userInfo.mobile_phone.equals(olduser.mobile_phone)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " mobile_phone = '" + userInfo.mobile_phone + "'";
		}
		if (!userInfo.home_phone.equals(olduser.home_phone)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " home_phone = '" + userInfo.home_phone + "'";
		}
		if (!userInfo.pwd.equals(olduser.pwd)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}

			String encode = CommTool.getEncrypt(userInfo.pwd).trim();
			String pwd = MD5.getInstance().getMD5ofStr(userInfo.pwd);
			upStr += " pwd = '" + pwd + "'";
			// upStr += " ,pwd_encode = '" + encode + "'";
			upStr += " ,res_char = '" + encode + "'";

			String dateStr = DateUtil.dateToString(DateUtil.getNowDate());

			String expdate = DateUtil.getDiffDay(90, dateStr);
			upStr += " ,end_date = '" + expdate + "'";

		}

		if (!userInfo.office_phone.equals(olduser.office_phone)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " office_phone = '" + userInfo.office_phone + "'";
		}
		if (!userInfo.office_fax.equals(olduser.office_fax)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " office_fax = '" + userInfo.office_fax + "'";
		}

		if (!userInfo.sex.equals(olduser.sex)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " sex = '" + userInfo.sex + "'";
		}
		if (!userInfo.age.equals(olduser.age)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " age = '" + userInfo.age + "'";
		}
		if (!userInfo.passportno.equals(olduser.passportno)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " passportno = '" + userInfo.passportno + "'";
		}

		if (!userInfo.address.equals(olduser.address)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " address = '" + userInfo.address + "'";
		}
		if (!userInfo.nation.equals(olduser.nation)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " nation = '" + userInfo.nation + "'";
		}

		if (!userInfo.director.equals(olduser.director)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " director = '" + userInfo.director + "'";
		}

		if (!userInfo.postalcode.equals(olduser.postalcode)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " postalcode = '" + userInfo.postalcode + "'";
		}

		if (!userInfo.user_id.equals(olduser.user_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " user_id = '" + userInfo.user_id + "'";
		}
		if (!userInfo.channleId.equals(olduser.channleId)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += "CHANNLE_ID = '" + userInfo.channleId + "'";
		}
		return upStr;
	}

	/**
	 * 获得sql vector,增加操作员并连同人员
	 *
	 * @param infoOper
	 * @param infoStaff
	 * @param request
	 * @param loginUser
	 * @return
	 * @throws AppException
	 */
	public static Vector getAddNewOperSqls(InfoOperTable infoOper,
			HttpServletRequest request, InfoOperTable loginUser)
			throws AppException {

		// SQL容器
		Vector sqlv = new Vector();

		// 增加操作员信息
		sqlv.addAll(getSqlvAddNewOper(request, infoOper));

		return sqlv;
	}

	/**
	 * 新增操作员组装sql 密码pwd为明码
	 *
	 * @param request
	 * @param infoOper
	 * @return
	 */
	public static Vector getSqlvAddNewOper(HttpServletRequest request,
			InfoOperTable infoOper) {
		Vector sqlv = new Vector();
		InfoOperTable sstUser = getUserInfo(infoOper.user_id);
		InfoOperTable user = (InfoOperTable) request.getSession().getAttribute(
				WebConstKeys.ATTR_C_SSTUSERTABLE);
		// 插入基本信息

		String pwd = MD5.getInstance().getMD5ofStr(infoOper.pwd);
		String encode = CommTool.getEncrypt(infoOper.pwd).trim();
		String dateStr = DateUtil.getDiffDay(90, DateUtil.getNowDate());
		String sqlA = "INSERT INTO ui_info_user(user_id,region_id,pwd,res_char,dept_id,duty_id,user_name,system_id,end_date) "
				+ "values('"
				+ infoOper.user_id
				+ "','"
				+ infoOper.region_id
				+ "','"
				+ pwd
				+ "','"
				+ encode
				+ "','"
				+ infoOper.dept_id
				+ "','"
				+ infoOper.duty_id
				+ "','"
				+ infoOper.user_name
				+ "','"
				+ user.system_id
				+ "','"
				+ dateStr
				+ "')";
		sqlv.add(sqlA);
		sqlA = " delete from UI_RULE_USER_GROUP where user_id='"
				+ infoOper.user_id + "'";
		sqlv.add(sqlA);
		sqlA = "insert into UI_RULE_USER_GROUP values ('" + infoOper.user_id
				+ "','" + infoOper.group_id + "')";
		sqlv.add(sqlA);

		String log_group = getLogSql(request, "C2212", "1", infoOper.user_id);
		sqlv.add(log_group);

		// 变更字段信息
		String upStr = getUpdateFiledStr(infoOper, sstUser);
		if (upStr.length() > 0) {
			String sqlC = "UPDATE ui_info_user SET " + upStr
					+ "  WHERE user_id = '" + infoOper.user_id + "'";
			sqlv.add(sqlC);
		}
		// 密码变更记录
		String pwdStr = "INSERT INTO ui_info_user_pwd_his(user_id,user_name,pwd,system_id,op_time,unicode) values('"
				+ infoOper.user_id
				+ "','"
				+ infoOper.user_name
				+ "','"
				+ pwd
				+ "','1'," + dateStr + ",'" + encode + "')";
		sqlv.add(pwdStr);

		// 日志

		String log_oper = getLogSql(request, "C2210", "1", infoOper.user_id);
		System.out.println("log_oper:" + log_oper);
		sqlv.add(log_oper);
		return sqlv;
	}

	/**
	 * 功能： 取得登陆用户信息放入session
	 *
	 * @param session
	 * @param userID
	 *            用户ID
	 * @param userPwd
	 *            用户密码 相关表 SST_USER
	 */
	public static void setUserInSession(HttpSession session, String user_id)
			throws AppException, HTMLActionException {

		UserServiceImpl info = new UserServiceImpl();
		InfoOperTable user = info.getUserInfo(user_id);
		session.setAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE, user);
	}

	/**
	 * 用户结果集转换为sstUser结构
	 *
	 * @param res
	 * @return
	 */
	@SuppressWarnings("unused")
	private static InfoOperTable toInfoOper(String[][] res) {
		if (res == null) {
			return null;
		}
		InfoOperTable loginUser = new InfoOperTable();
		int i = 0;

		loginUser.user_id = res[0][i++];
		loginUser.region_id = res[0][i++];
		loginUser.dept_id = res[0][i++];
		loginUser.duty_id = res[0][i++];
		loginUser.user_name = res[0][i++];
		loginUser.pwd = CommTool.getdesecret(res[0][i++]).trim();
		loginUser.status = res[0][i++];
		loginUser.begin_date = res[0][i++];
		loginUser.end_date = res[0][i++];
		loginUser.create_time = res[0][i++];
		loginUser.birthday = res[0][i++];
		loginUser.mobile_phone = res[0][i++];
		loginUser.home_phone = res[0][i++];
		loginUser.office_phone = res[0][i++];
		loginUser.office_fax = res[0][i++];
		loginUser.email = res[0][i++];
		loginUser.sex = res[0][i++];
		loginUser.age = res[0][i++];
		loginUser.postalcode = res[0][i++];
		loginUser.address = res[0][i++];
		loginUser.nation = res[0][i++];
		loginUser.passportno = res[0][i++];
		loginUser.director = res[0][i++];
		loginUser.question = res[0][i++];
		loginUser.answer = res[0][i++];
		loginUser.res_char = res[0][i++];
		loginUser.group_id = res[0][i++];
		return loginUser;

	}

	/**
	 * 取得日志信息SQL
	 *
	 * @param request
	 * @param user_id
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_user_id = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_user_id, ip, type,
					para_a);
			System.out.println(sql);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

	/**
	 * 取得日志信息SQL
	 *
	 * @param request
	 * @param user_id
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a, String para_b) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_user_id = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_user_id, ip, type,
					para_a, para_b);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

	/**
	 * 从数据库序列中获得操作员工号
	 *
	 * @return
	 */
	public static String getAutoNewOperNo() {
		String newOperNo = "";
		String sql = "select seq_info_user.nextval from dual";
		String[][] rs = null;
		try {
			rs = WebDBUtil.execQryArray(sql, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != rs && rs.length > 0) {
			newOperNo = rs[0][0];
		}
		return newOperNo;
	}

	/**
	 * 判断人员编号是否已经存在
	 *
	 * @param user_id
	 * @return
	 */
	public static boolean isUserExists(String user_id, InfoOperTable loginUser) {
		String sql = "";
		boolean re = false;
		if (loginUser == null) {
			return re;
		}

		try {
			sql = "select * from ui_info_user  where user_id='" + user_id + "'";
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				re = true;
			}
		} catch (AppException ex) {
			System.out.println("isStaffExists sql error ====== " + sql);
			ex.printStackTrace();
		}
		return re;

	}

	/**
	 * 取得用户相关角色
	 *
	 * @param user_id
	 * @param region_id
	 * @return
	 */
	public static UserRoleStruct[] getRolesByUserRegion(String user_id,
			String region_id, String dept_id, String login_dept_id,
			String login_user_id) {
		UserRoleStruct userRoleStruct[] = null;
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q1026", region_id, dept_id, dept_id,
					login_dept_id, login_user_id, user_id);
			// System.out.println("Q1026==============" + sql);
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v.size() > 0) {
				userRoleStruct = new UserRoleStruct[v.size()];
				for (int i = 0; i < v.size(); i++) {
					Vector tempv = (Vector) v.get(i);
					userRoleStruct[i] = new UserRoleStruct();
					userRoleStruct[i].role_id = (String) tempv.get(0);
					userRoleStruct[i].role_name = (String) tempv.get(1);
					userRoleStruct[i].comments = (String) tempv.get(2);
					userRoleStruct[i].user_id = (String) tempv.get(3);
					userRoleStruct[i].belong_type = (String) tempv.get(4);
					userRoleStruct[i].role_level = (String) tempv.get(5);
					userRoleStruct[i].isactive = (String) tempv.get(6);
				}
			}
		} catch (AppException ex) {
			System.out.println("Q1026==error============" + sql);
			ex.printStackTrace();
		}
		return userRoleStruct;
	}

	/**
	 * 查询用户角色数组
	 *
	 * @param user_id
	 * @return
	 */
	public static String[] _getUserRoles(String user_id) {
		String sql = "";
		String arr[] = null;
		try {
			sql = SQLGenator.genSQL("Q1049", user_id);
			// ////System.out.println("U0022===========" + sql);
			String[][] res = WebDBUtil.execQryArray(sql, "");
			if (res != null) {
				arr = new String[res.length];
				for (int i = 0; i < res.length; i++) {
					arr[i] = res[i][0];
				}
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return arr;
	}

	/**
	 * 转换结构
	 *
	 * @param sourceOrderArr
	 * @return
	 */
	public static Hashtable _roleOrderArrToHashArr(String[] sourceOrderArr) {
		Hashtable arr = new Hashtable();
		for (int i = 0; sourceOrderArr != null && i < sourceOrderArr.length; i++) {
			arr.put(sourceOrderArr[i], "1");
		}
		return arr;
	}

	/**
	 *
	 * @param user_id
	 * @param pwd
	 * @return
	 * @desc:判断用户登录是否成功
	 */
	public static InfoOperTable loginValid(String user_id, String pwd) {

		InfoOperTable loginUser = null;
		String md5Pwd = MD5.getInstance().getMD5ofStr(pwd);
		String sql = "select * from info_oper where login_name='" + user_id
				+ "' and pwd='" + md5Pwd + "'";
		try {

			// ////System.out.println("U0022===========" + sql);
			String[][] res = WebDBUtil.execQryArray(sql, "");
			if (res != null && res.length > 0) {
				loginUser = new InfoOperTable();
				loginUser.user_id = res[0][0];
				loginUser.region_id = res[0][1];
				loginUser.dept_id = res[0][2];
				loginUser.user_name = res[0][3];
				loginUser.user_name = res[0][4];
				loginUser.pwd_encode = res[0][6];

				return loginUser;
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return loginUser;

	}

	/**
	 *
	 * @param loginUser
	 * @return
	 * @Desc:获取登录用户横向Menu,menu_col = 0表示横向一级菜单
	 */
	public static InfoMenu[] loginUserColMenu(InfoOperTable loginUser) {
		String sql = " select menu_code,parent_menu_code,menu_row,menu_col,menu_url,menu_title from info_menu where parent_menu_code is null and menu_col=0 "
				+ "and menu_code in(select K.MENU_CODE from rule_oper_role m,info_role n,info_menu k,rule_role_func g where '"
				+ loginUser.user_id
				+ "'=m.user_id and m.role_code=n.role_code "
				+ "and g.role_code=m.role_code and g.entity_code=k.menu_code and g.entity_type='M') order by menu_row";
		try {

			System.out.println("===========" + sql);
			String[][] res = WebDBUtil.execQryArray(sql, "");

			if (res != null && res.length > 0) {
				InfoMenu[] menu = new InfoMenu[res.length];
				for (int i = 0; i < res.length; i++) {
					InfoMenu obj = new InfoMenu();
					obj.setMenu_code(res[i][0]);
					obj.setParent_menu_code(res[i][1]);
					obj.setMenu_Row(res[i][2]);
					obj.setMenu_Col(res[i][3]);
					obj.setMenu_Url(res[i][4]);
					obj.setMenu_Title(res[i][5]);
					menu[i] = obj;
				}

				return menu;
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return null;

	}

	public static String printMenuColScript(InfoOperTable loginUser) {
		InfoMenu[] menu = loginUserColMenu(loginUser);
		String strOut = "var menuArray=new Array(\"\"";
		if (menu != null) {
			for (int i = 0; i < menu.length; i++) {

				strOut += ",new Array(\"" + menu[i].getMenu_Title()
						+ "\",\"\",\"\",\"\"";
				strOut += ",new Array(\""
						+ menu[i].getMenu_Title()
						+ "\",\"top.mainFrame.location.href='menuNavInfo.rptdo?menu_code="
						+ menu[i].getMenu_code() + "'\",\"\")";
				strOut += ")";

			}
		}
		strOut += ");";
		return strOut;
	}

	public static String[][] getGroupRole(String user_id) {

		String sql = "";
		String[][] result = null;
		try {

			sql = "select a.role_id,b.role_name from ui_rule_group_role a,ui_info_role b,UI_RULE_USER_GROUP c "
					+ "where a.role_id=b.role_id and a.group_id=c.group_id and c.user_id='"
					+ user_id + "'";
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String[][] getUserRole(String user_id) {

		String sql = "";
		String[][] result = null;
		try {

			sql = "select a.role_id,b.role_name from UI_RULE_USER_ROLE a,ui_info_role b where a.role_id=b.role_id and a.user_id='"
					+ user_id + "'";
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getRoleTree(String user_id) {
		String[][] userRole = getUserRole(user_id);
		String[][] groupRole = getGroupRole(user_id);

		StringBuffer jsStr = new StringBuffer();
		jsStr.append("<script type=\"text/javascript\"> \n")
				.append("d = new dTree('d'); \n")
				.append("d.add(0,-1,'用户对应的角色','#'); \n");
		for (int i = 0; userRole != null && i < userRole.length; i++) {
			jsStr.append("d.add('" + userRole[i][0] + "',0,'" + userRole[i][1]
					+ "'); \n");

		}
		jsStr.append("document.write(d); \n").append("</script> \n")
				.append("<script type=\"text/javascript\"> \n")
				.append("d2 = new dTree('d2'); \n")
				.append("d2.add(0,-1,'组对应的角色','#'); \n");
		for (int i = 0; groupRole != null && i < groupRole.length; i++) {
			jsStr.append("d2.add('" + groupRole[i][0] + "','0','"
					+ groupRole[i][1] + "'); \n");

		}
		jsStr.append("document.write(d2); \n").append("</script>");

		return jsStr.toString();
	}

	public static String[][] getNoUserRole(String user_id) {

		String sql = "";
		String[][] result = null;
		try {

			sql = "select b.role_id,b.role_name from (select * from UI_RULE_USER_ROLE where user_id='"
					+ user_id
					+ "') a,ui_info_role b where a.role_id(+)=b.role_id and a.role_id is null ";
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void saveUserRole(HttpServletRequest request,
			InfoOperTable userInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		Vector sqlv = new Vector();

		String log_sql1 = getLogSql(request, "C2211", "3", userInfo.user_id);
		sqlv.add(log_sql1);
		String delSql = "delete from ui_rule_user_role where user_id='"
				+ userInfo.user_id + "'";

		String[] roleArr = userInfo.role_id.split(",");
		for (int i = 0; i < roleArr.length; i++) {
			String sql = "insert into ui_rule_user_role values ('"
					+ userInfo.user_id + "','" + roleArr[i] + "')";
			sqlv.add(sql);
		}

		String log_sql2 = getLogSql(request, "C2211", "1", userInfo.user_id);
		sqlv.add(log_sql2);
		String sqlArr[] = new String[sqlv.size() + 1];
		sqlArr[0] = delSql;
		for (int i = 0; i < sqlv.size(); i++) {
			sqlArr[i + 1] = sqlv.get(i).toString();
			// //System.out.println("sqlArr[i]====="+sqlArr[i]);
		}
		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}
		if (i > 0) {
			String url = "userview.rptdo?submitType=userRole&user_id="
					+ userInfo.user_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "用户组角色保存成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "用户组角色保存失败！");
		}
	}

	public static String[][] getUserRoleList(String group_id, String user_id) {

		String sql = "";
		String[][] result = null;
		try {
			sql = SQLGenator.genSQL("userRole001", group_id, user_id);
			// System.out.println("sql=="+sql);
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getBiSys() {

		String biSys = "";
		String[][] rs;
		try {
			rs = WebDBUtil
					.execQryArray(
							"select system_id from ui_info_sub_system where sequence=1",
							"");
			if (rs != null && rs.length > 0) {
				biSys = rs[0][0];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return biSys;
	}

	public static String[][] getLoUser(String user_id, String biSys,
			String system_id) {

		String sql = "";
		String[][] result = null;
		String str = "";
		try {
			if (system_id.equals(biSys)) {
				str = " bi_user='" + user_id + "'";
			} else {
				str = " lo_user='" + user_id + "'";
			}
			sql = "select bi_user,lo_user from ui_sso_user where " + str;
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void addLoUser(HttpServletRequest request,
			InfoOperTable userInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		String bi_user = request.getParameter("bi_user");
		String lo_user = request.getParameter("lo_user");
		String sys_user = request.getParameter("sys_user");
		try {
			String[][] rs = WebDBUtil.execQryArray(
					"select system_id from ui_info_user where user_id in ('"
							+ bi_user + "','" + lo_user + "')", "");
			if (rs == null || rs.length != 2) {
				throw new HTMLActionException(session,
						HTMLActionException.SUCCESS_PAGE, "对不起，用户不存在！");
			}
		} catch (AppException e1) {

			e1.printStackTrace();
		}
		String sql = "insert into ui_sso_user values ('" + bi_user + "','"
				+ lo_user + "')";

		String sqlArr[] = new String[1];
		sqlArr[0] = sql;

		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}
		if (i > 0) {
			String url = "loUserList.jsp?user_id=" + sys_user;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "保存成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "保存失败！");
		}
	}

	public static void delLoUser(HttpServletRequest request,
			InfoOperTable userInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		String bi_user = request.getParameter("bi_user");
		String lo_user = request.getParameter("lo_user");
		String sys_user = request.getParameter("sys_user");
		String delSql = "delete from ui_sso_user where bi_user='" + bi_user
				+ "' and lo_user='" + lo_user + "'";

		String sqlArr[] = new String[1];
		sqlArr[0] = delSql;

		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}
		if (i > 0) {
			String url = "loUserList.jsp?user_id=" + sys_user;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "保存成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "保存失败！");
		}
	}

	public static String last3UserPwdStr(String user_id)
			throws HTMLActionException {

		String selectStr = "select unicode from ui_info_user_pwd_his where user_id = '"
				+ user_id + "' order by op_time desc ";
		String pwdStr = "";
		@SuppressWarnings("unused")
		String sqlArr[] = new String[1];
		try {
			System.out.println("last3UserPwdStr===============" + selectStr);
			String[][] arr = WebDBUtil.execQryArray(selectStr);
			if (arr != null && arr.length > 0) {
				for (int i = 0; i < arr.length; i++) {
					if (i >= 3) {
						break;
					}
					if (pwdStr.length() > 0) {
						pwdStr += ",";
					}
					pwdStr += arr[i][0];

				}
			}
		} catch (Exception e) {
			pwdStr = "";
		}

		return "," + pwdStr + ",";
	}

	public static int updateUserStatus(String user_id,int state) {

		int i = 0;

		try {
			String sql = "update ui_info_user set status = "+state+" where user_id='"
					+ user_id + "'";
			i = WebDBUtil.execUpdate(sql);

		} catch (AppException e) {
			e.printStackTrace();
			i = -1;
		}
		return i;
	}

}
