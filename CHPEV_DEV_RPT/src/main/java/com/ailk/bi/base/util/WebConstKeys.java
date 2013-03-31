package com.ailk.bi.base.util;

import java.io.Serializable;

/**
 * 存放session属性名字的常量定义 命名规则 ATTR_表名(s) 如果为复数(数组或vector)则后面要加s
 * <p>
 * Title: asiabi BI System
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class WebConstKeys implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4992383728154015069L;

	/**
	 * BSS平台登陆地址
	 */
	public final static String BSS_URL = "http://bss.cq.unicom.local:8200/";

	// 登录操作员的信息
	public static final String ATTR_C_SSTUSERTABLE = "FRONT.CONST.SSTUSERTABLE";

	// 预警类型
	public static final String ATTR_C_RISKKND = "FRONT.CONST.ATTR_C_RISKKND";

	// 业务类型
	public static final String ATTR_C_SVCKND = "FRONT.CONST.ATTR_C_SVCKND";

	// 用户状态
	public static final String ATTR_C_USERSTA = "FRONT.CONST.ATTR_C_USERSTA";

	// 用户权限资源MAP
	public static final String ATTR_C_RES_RIGHTS_MAP = "ATTR_C_RES_RIGHTS_MAP";

	// 资源标识和名称对照MAP
	public static final String ATTR_C_RES_ID_VS_NAME = "ATTR_C_RES_ID_VS_NAME";

	// 反馈对象标识
	public static final String ATTR_C_RES_BACK_ID = "ATTR_C_RES_BACK_ID";

	// 日志类型
	public static final String ATTR_C_LOG_LOGIN = "1";

	//
	public static final String ATTR_C_LOG_ACCESS = "2";

	// 用户控制区域
	public static final String ATTR_C_UserCtlStruct = "ATTR_C_UserCtlStruct";

	// 区域ID和名称
	public static final String ATTR_C_REGION_ID_VS_NAME = "ATTR_C_REGION_ID_VS_NAME";

	// 部门ID和名称
	public static final String ATTR_C_DEPT_ID_VS_NAME = "ATTR_C_DEPT_ID_VS_NAME";

	// 品牌
	public static final String ATTR_C_BRAND_ID_VS_NAME = "ATTR_C_BRAND_ID_VS_NAME";

	// 子品牌
	public static final String ATTR_C_SUBBRAND_ID_VS_NAME = "ATTR_C_SUBBRAND_ID_VS_NAME";

	// 产品
	public static final String ATTR_C_PRODUCT_ID_VS_NAME = "ATTR_C_PRODUCT_ID_VS_NAME";

	// 入网渠道
	public static final String ATTR_C_CHANNEL_ID_VS_NAME = "ATTR_C_CHANNEL_ID_VS_NAME";

	// 销售模式
	public static final String ATTR_C_SALE_ID_VS_NAME = "ATTR_C_SALE_ID_VS_NAME";

	// 手机机型
	public static final String ATTR_C_MAC_ID_VS_NAME = "ATTR_C_MAC_ID_VS_NAME";

	// 业务类型
	public static final String ATTR_C_SVC_ID_VS_NAME = "ATTR_C_SVC_ID_VS_NAME";

	// 用户等级
	public static final String ATTR_C_VIP_ID_VS_NAME = "ATTR_C_VIP_ID_VS_NAME";

	// 在网时长
	public static final String ATTR_C_DURA_ID_VS_NAME = "ATTR_C_DURA_ID_VS_NAME";

	//
	// 系统所有资源信息
	public static final String ATTR_C_INFORESSTRUCT = "ATTR_C_INFORESSTRUCT";

	public static final String ATTR_C_INFOMenuSTRUCT = "ATTR_C_INFOMenuSTRUCT";

	// 业务类型G/C两网权限值
	public static final String RIGHT_G_C_VALUES_ALL = "all";

	public static final String RIGHT_G_C_VALUES_C = "10";

	public static final String RIGHT_G_C_VALUES_G = "20";

	// 用户组
	public static final String ATTR_C_SST_USER_GROUP = "FRONT.CONST.ATTR_C_SST_USER_GROUP";

	// 考核部门信息(BF)
	public static final String ATTR_C_DEVALCHANNEL = "front.const.ATTR_C_DEVALCHANNEL";

	// 政策类型(BF)
	public static final String ATTR_C_DUserDnnr = "front.const.ATTR_C_DUserDnnr";

	// 营销政策(BF)
	public static final String ATTR_C_DOldUserDnnr = "front.const.ATTR_C_DOldUserDnnr";

	// 短信条数分档(BF)
	public static final String ATTR_C_PtpSmsLvl = "front.const.ATTR_C_PtpSmsLvl";

	// 预警类型
	public static final String ATTR_C_RiskKnd = "front.const.ATTR_C_RiskKnd";

	// 业务类型
	public static final String ATTR_C_SvcKnd = "front.const.ATTR_C_SvcKnd";

	// 用户状态
	public static final String ATTR_C_UserSta = "front.const.ATTR_C_UserSta";

	// 用户菜单资源
	public static final String ATTR_C_MenuInfo = "front.const.ATTR_C_MenuInfo";

	// 用户归属子系统
	public static final String ATTR_C_LoginSystem = "ATTR_C_LoginSystem";

	/********************************* 参数配置引擎 *****************************************/
	public static final String ATTR_C_CODE_LIST = "ATTR_C_CODE_LIST";

	public static final String LOGIN_USER_MENU_LIST = "LOGIN_USER_MENU_LIST";

	/**
	 * 渠道连锁级别
	 */
	public static final String SUBJECT_CHANNEL_CHAIN_LEVEL = "CHAIN_LEVEL";

	/**
	 * 考核区域
	 */
	public static final String SUBJECT_CHANNEL_AREA_DEF = "AREA_DEF";
	
	/**
	 * 连锁级别
	 */
	public static final String SOC_CHANNEL_CHAIN_LEVEL = "CHAIN_LEVEL";
	
	/**
	 * 排名波动
	 */
	public static final String SOC_RANK_WAVE = "RANK_WAVE";
	
	/**
	 * 自有渠道门店级别
	 */
	public static final String SELF_CHNL_LEVEL = "SELF_CHNL_LEVEL";
	
	/**
	 * 自有渠道得分等级
	 */
	public static final String SELF_SCORE_RANK = "SCORE_RANK";
	/**
	 * 社会渠道门店级别
	 */
	public static final String SOC_CHNL_LEVEL = "SOC_CHNL_LEVEL";
	
	/**
	 * 归属本部
	 */
	public static final String SOC_CHNL_OWE_CODE = "OWE_CODE";

}