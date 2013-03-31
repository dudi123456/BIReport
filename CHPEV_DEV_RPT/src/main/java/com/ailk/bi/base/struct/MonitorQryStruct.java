package com.ailk.bi.base.struct;

import com.ailk.bi.common.event.JBTableBase;

/**
 * 本结构是运营监控专用 由于涉及数据权限过滤比较特殊 因此单独定义一个查询结构
 * 
 * @author jcm
 * 
 */
public class MonitorQryStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8966017988250439086L;

	public String gather_day = ""; // 统计日

	public String gather_mon = ""; // 统计月

	public String begin_mon = ""; // 统计月

	public String end_mon = ""; // 统计月

	public String begin_day = ""; // 起始日期

	public String end_day = ""; // 起始日期

	public String svc_knd = "";// 业务类型

	public String vprepay_knd = "";// 智能网类型

	public String svc_knd_lvl = "";// 业务大类

	public String svc_level = "";// 抽象业务等级

	public String svc_id = "";// 抽象业务等级

	public String svcknd_desc = ""; // 业务类型

	public String vprepayknd_desc = ""; // 智能网类型描述

	public String svckndlvl_desc = ""; // 业务大类描述

	public String svc_name = "";// 抽象业务名称

	public String product_id = "";// 产品

	public String product_desc = "";// 产品

	public String product_level = "";// 产品

	public String brand_knd = "";// 品牌

	public String brandknd_desc = "";// 品牌

	public String sub_brand_knd = "";// 子品牌

	public String subbrandknd_desc = "";// 子品牌

	public String mac_type = "";// 手机类型

	public String mactype_desc = "";// 手机类型

	public String sale_type = "";// 销售模式

	public String saletype_desc = "";// 销售模式

	public String vip_level = "";// 用户等级

	public String viplevel_desc = "";// 用户等级描述

	public String onl_dura = "";// 在网时长

	public String onldura_desc = "";// 在网时长描述

	public String table_id = "";// 表格字典

	public String datasource_table = "";// 表格数据表

	public String channel_id = ""; // 入网渠道

	public String channel_desc = ""; // 入网渠道

	public String channel_level = ""; // 入网渠道

	public String dim_col = "";// 传递角度

	public String dim_value = "";// 传递角度值

	public String data_rights = "";// 数据权限条件

	public String svc_rights = "";// 数据权限条件

	public String product_rights = "";// 数据权限条件

	public String channel_rights = "";// 数据权限条件

	public String msu_id = "";// 分析指标

	public String msu_name = "";// 分析指标名称

	public KeyValueStruct msu_list[] = null;// 指标列表

	public String area_str = "";// 对比分析区域字符串

	public String svc_str = "";// 对比分析区域字符串

	public String brand_str = "";// 对比分析区域字符串

	public String product_str = "";// 对比分析区域字符串

	public String channel_str = "";// 对比分析区域字符串

	public String saletype_str = "";// 对比分析区域字符串

	public String mactype_str = "";// 对比分析区域字符串

	public String vip_str = "";// 对比分析区域字符串

	public String dura_str = "";// 对比分析区域字符串

	public String region_id = "";// 区域编码

	public String region_name = "";// 区域名称

	public String region_level = "";// 区域等级1，2，3，4，5

	public KeyValueStruct regionArr[] = null;// 对比区域

	public KeyValueStruct svcArr[] = null;// 对比业务类型

	public KeyValueStruct brandArr[] = null;// 对比品牌

	public KeyValueStruct subbrandArr[] = null;// 对比子品牌

	public KeyValueStruct productArr[] = null;// 对比产品

	public KeyValueStruct saleArr[] = null;// 对比销售模式

	public KeyValueStruct macArr[] = null;// 对比手机机型

	public KeyValueStruct channelArr[] = null;// 对比入网渠道

	public KeyValueStruct vipArr[] = null;// 对比用户等级

	public KeyValueStruct duraArr[] = null;// 对比在网时长

	//

}
