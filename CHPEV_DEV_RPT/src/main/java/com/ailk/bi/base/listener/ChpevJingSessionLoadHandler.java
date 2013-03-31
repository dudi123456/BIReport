package com.ailk.bi.base.listener;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import waf.controller.web.util.WebKeys;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppConst;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.WebChecker;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.bisso.ailk.client.inner.SessionLoadHandler;

public class ChpevJingSessionLoadHandler implements SessionLoadHandler{
	private static final Logger logger = Logger.getLogger(ChpevDevSessionLoadHandler.class);
	
	public boolean isSessionLoaded(HttpServletRequest req,HttpServletResponse res){
	  InfoOperTable loginUser = (InfoOperTable) req.getSession().getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
	  if (loginUser != null) {
		return true;
	  }else
		return false;
	}
   
	/**
	 * 加载用户信息，登录状态，用户权限
	 * author:tanglian
	 */
	public void loadSession(HttpServletRequest req, HttpServletResponse res) throws Exception {
		logger.debug("效能评估加载用户信息");
		HttpSession session = req.getSession();
		String userCode = session.getAttribute("BISSO_USERCODE")!=null?session.getAttribute("BISSO_USERCODE").toString():"";
		/* 如果断言信息存在，则获取用户信息 */
		if (userCode != null && !userCode.equals("")) {
			logger.debug("1----------userCode=[" + userCode + "]----------");
			InfoOperTable userInfo = new InfoOperTable();
			UserCtlRegionStruct region = new UserCtlRegionStruct();
			userInfo.user_id=userCode;
			
			/* 打包操作员其他信息 */
			this.repackUser(userInfo, region);
			
			/*将登录用户信息添加到session中*/
			session.setAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE, userInfo);
			/*将登录标志添加到session中*/
			session.setAttribute(WebChecker.LOGIN_FLAG, "1");
			session.setAttribute(WebKeys.SYS_LOGINFLAG, "true");
			
			/*将用户权限添加到session中*/
			session.setAttribute(WebConstKeys.ATTR_C_UserCtlStruct, region);
		}		
	}
	
	/**
     * 封装登录角色等信息
     * 
     * @param userCode
     * @return
     * @author tanglian
	 * @throws AppException 
     */
    @SuppressWarnings("unchecked")
	private Map<String, Object> getUserInfoDetail(String userCode) throws AppException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT US.USER_ID,US.USER_CODE, US.USER_NAME,");
        sql.append(" US.USER_STATUS,");
        sql.append(" US.CITY_CODE,");
        sql.append(" US.COUNTYID,");
        sql.append(" US.DEPART_ID,");
        sql.append(" US.PROVINCE_CODE ,");
        sql.append(" TO_CHAR(US.USER_CREATE_TIME,'YYYYMMDDHH24MISS') AS USER_CREATE_TIME,");
        sql.append(" US.MOBILE_PHONE,");
        sql.append(" US.CONTACT_EMAIL");
        sql.append(" FROM");
        sql.append(" V_USER_INFO US");
        sql.append(" WHERE US.USER_CODE =?");
        String[] params = { userCode };
        List<Map<String, Object>> userInfoDetail = WebDBUtil.execQryArrayMap(sql.toString(),params);

        /* 以用户编码作为唯一键, 所以一个用户编码只对应一个用户 */
        return ((null == userInfoDetail || 0 == userInfoDetail.size()) ? null : userInfoDetail
                .get(0));
    }
    
	/**
     * 将当前登录用户信息重新打包封装
     * 
     * @param userInfo
     * @param userAttrMap
     * @throws Exception
     * @author zhangchao
	 * @throws AppException 
     */
    private void repackUser(InfoOperTable userInfo, UserCtlRegionStruct region) throws AppException {
        String userid = userInfo.user_id;
        if (StringUtils.isBlank(userid)) {
            throw new AppException("登陆失败，没有获取到操作员编码");
        }
        /* 1. 获取用户编码对应的用户信息 */
        Map<String, Object> userInfoDetail = this.getUserInfoDetail(userid);
        if (null == userInfoDetail) {
            throw new AppException("登陆失败，无法获取有效的操作员信息");
        }
        /* 2. 封装用户属性 */
          userInfo.user_id= getString(userInfoDetail.get("USER_CODE"));
          userInfo.status = getString(userInfoDetail.get("USER_STATUS"));
          userInfo.user_name = getString(userInfoDetail.get("USER_NAME"));
          userInfo.region_id = getString(userInfoDetail.get("CITY_CODE"));
          userInfo.dept_id = getString(userInfoDetail.get("DEPART_ID"));
          userInfo.create_time = getString(userInfoDetail.get("USER_CREATE_TIME"));
          userInfo.mobile_phone = getString(userInfoDetail.get("MOBILE_PHONE")); 
          userInfo.email = getString(userInfoDetail.get("CONTACT_EMAIL"));
          userInfo.system_id = "1";
          /*3.封装用户的权限*/  
          String provinceid = getString(userInfoDetail.get("PROVINCE_CODE"));
          String countyid = getString(userInfoDetail.get("COUNTYID"));
          if(AppConst.Province_ZB.equals(provinceid)){
        	  region.setCtl_lvl("0"); //0表示总部
          }else{
        	  if(AppConst.City_ALL.equals(userInfo.region_id)){
        		  region.setCtl_lvl("1"); //1表示省级
        		  region.ctl_metro_str = provinceid;
        		  region.ctl_metro_str_add = "'"+provinceid+"'";
        	  }else{
        		 if(AppConst.County_ALL.equals(countyid)) {
        			 region.setCtl_lvl("2"); //2表示地市分公司;
        			 region.ctl_metro_str = provinceid;
           		     region.ctl_metro_str_add = "'"+provinceid+"'";
           		     region.ctl_city_str = userInfo.region_id;
           		     region.ctl_city_str_add = "'"+userInfo.region_id+"'";
        		 }else{
        			 region.setCtl_lvl("3"); //3区县分公司;
        			 region.ctl_metro_str = provinceid;
           		     region.ctl_metro_str_add = "'"+provinceid+"'";
           		     region.ctl_city_str = userInfo.region_id;
           		     region.ctl_city_str_add = "'"+userInfo.region_id+"'";
        			 region.ctl_county_str = countyid;
        			 region.ctl_county_str_add = "'"+countyid+"'";
        		 }
        	  }
          }      
    }
    
    /*将对象转换成字符串*/
    public String getString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }
}
